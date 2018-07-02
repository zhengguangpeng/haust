package com.hongtoo.nagz.cdc.event.debezium;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongtoo.nagz.cdc.event.common.CdcEventConst;
import com.hongtoo.nagz.cdc.event.common.PublishedEvent;
import com.hongtoo.nagz.cdc.event.common.PublishedEvent.Operation;
import com.hongtoo.nagz.cdc.event.common.UpdateBoEvent;
import com.hongtoo.nagz.cdc.event.kafka.producer.CdcEventKafkaProducer;
import com.hongtoo.nagz.redis.utils.RedisUtils;

import io.debezium.config.Configuration;
import io.debezium.connector.mysql.MySqlConnectorTask;
import io.debezium.data.Envelope.FieldName;
import io.debezium.embedded.EmbeddedEngine;

import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListener;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.apache.kafka.connect.storage.KafkaOffsetBackingStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Subscribes to changes made to EVENTS table and publishes them to aggregate
 * topics
 */
public class EventTableChanges2TopicRelay {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final ObjectMapper om = new ObjectMapper();

	private CdcEventKafkaProducer producer;
	private EmbeddedEngine engine;

	private CdcStartupValidator cdcStartupValidator;
	private final String kafkaBootstrapServers;
	private final JdbcUrl jdbcUrl;
	private final String dbUser;
	private final String dbPassword;
	private final String serverId;
	private final String serverName;
	private final LeaderSelector leaderSelector;
	private Configuration config;
	//是否是leader节点
	private boolean isLeader = false;
		
	public boolean isLeader(){
		return isLeader;
	}
	
	public EmbeddedEngine getEngine(){
		return engine;
	}

	public EventTableChanges2TopicRelay(String kafkaBootstrapServers,
			JdbcUrl jdbcUrl, String dbUser, String dbPassword,
			String serverId,String serverName,
			CuratorFramework client, CdcStartupValidator cdcStartupValidator) {
		this.kafkaBootstrapServers = kafkaBootstrapServers;
		this.jdbcUrl = jdbcUrl;
		this.dbUser = dbUser;
		this.dbPassword = dbPassword;
		this.serverId = serverId;
		this.serverName = serverName;
		this.cdcStartupValidator = cdcStartupValidator;

		leaderSelector = new LeaderSelector(client,"/nagz_cdc_event/cdc/leader", 
				new LeaderSelectorListener() {

					@Override
					public void takeLeadership(CuratorFramework client) throws Exception {
						takeLeadership();
					}

					private void takeLeadership() throws InterruptedException {
						logger.info("Taking leadership");
						isLeader = true;
						try {
							CompletableFuture<Object> completion = startCapturingChanges();
							try {
								completion.get();
							} catch (InterruptedException e) {
								logger.error("Interrupted while taking leadership");
							}
						} catch (Throwable t) {
							logger.error("In takeLeadership", t);
							throw t instanceof RuntimeException ? (RuntimeException) t : new RuntimeException(t);
						} finally {
							logger.debug("TakeLeadership returning");
						}
					}

					@SuppressWarnings("incomplete-switch")
					@Override
					public void stateChanged(CuratorFramework client,ConnectionState newState) {

						logger.debug("StateChanged: {}", newState);

						switch (newState) {
						case SUSPENDED:
							resignLeadership();
							break;

						case RECONNECTED:
							try {
								takeLeadership();
							} catch (InterruptedException e) {
								logger.error("While handling RECONNECTED", e);
							}
							break;

						case LOST:
							resignLeadership();
							break;
						}
					}

					private void resignLeadership() {
						logger.info("Resigning leadership");
						isLeader = false;
						try {
							stopCapturingChanges();
						} catch (InterruptedException e) {
							logger.error("While handling SUSPEND", e);
						}
					}
				});
	}

	@PostConstruct
	public void start() {
		logger.info("CDC initialized. Ready to become leader");
		leaderSelector.start();
	}

	public CompletableFuture<Object> startCapturingChanges() throws InterruptedException {

		logger.debug("Starting to capture changes");
		cdcStartupValidator.validateEnvironment();

		producer = new CdcEventKafkaProducer(kafkaBootstrapServers);

		String connectorName = "my_sql_connector";
		//从缓存中添加监控表,为空就是监控所有表
		String capturedTables = RedisUtils.getObjectByKey(String.class, CdcEventConst.KEY_CAPTRUE_TALBES);
		if(capturedTables == null){
			capturedTables = "";
		}
		config = Configuration
				.create()
				/* begin engine properties */
				.with("connector.class","io.debezium.connector.mysql.MySqlConnector")
				.with("group.id","nagz-cdc-event")
 				.with("offset.storage", KafkaOffsetBackingStore.class.getName())
 				.with("offset.storage.partitions",1)
 				.with("offset.storage.replication.factor",1)
				.with("bootstrap.servers", kafkaBootstrapServers)
				.with("offset.storage.topic","cdc.event." + connectorName + ".offset.storage")

				.with("poll.interval.ms", 50)
				.with("offset.flush.interval.ms", 6000)
				/* begin connector properties */
				.with("name", connectorName)
				.with("database.hostname", jdbcUrl.getHost())
				.with("database.port", jdbcUrl.getPort())
				.with("database.user", dbUser)
				.with("database.password", dbPassword)				 
				.with("database.server.id", serverId)
				.with("database.server.name", serverName)
				//.with("database.whitelist", "NAGZ_348064282690211840,nagz_348064282690211840")
				.with("table.whitelist",capturedTables)
				.with("database.history",io.debezium.relational.history.KafkaDatabaseHistory.class.getName())
				.with("database.history.kafka.topic","cdc.event." + connectorName + ".history.kafka.topic")
				.with("database.history.kafka.bootstrap.servers",kafkaBootstrapServers)
				.build();

		CompletableFuture<Object> completion = new CompletableFuture<>();
		engine = EmbeddedEngine
				.create()
				.using((success, message, throwable) -> {
					if (success)
						completion.complete(null);
					else
						completion.completeExceptionally(new RuntimeException(
								"Engine failed to start" + message, throwable));
				}).using(config).notifying(this::handleEvent).build();

		Executor executor = Executors.newCachedThreadPool();
		executor.execute(() -> {
			try {
				engine.run();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		});

		logger.debug("Started engine");
		return completion;
	}
	
	/**
	 * 重新设置被监控的表
	 */
	public void resetTablesCaptured(){
		
		//重新设置config中要监控的表
		String newCapturedTables = RedisUtils.getObjectByKey(String.class, CdcEventConst.KEY_CAPTRUE_TALBES);	
		logger.info("新的监控表: " + newCapturedTables);
		//String newCapturedTables =  "msgsteal.user_trade,msgsteal.user_custom";
		
		Configuration newConfiguration = Configuration.copy(config).with("table.whitelist",newCapturedTables).build();
		
//		MySqlConnectorTask task = (MySqlConnectorTask)engine.getSourceTask();
//		//重新配置表
//		task.getTaskContext().dbSchema().filters().reBuildTableFilter(newConfiguration);
//		
//		task.getTaskContext().makeRecord().regenerate();
	}

	@PreDestroy
	public void stop() throws InterruptedException {
		// stopCapturingChanges();
		leaderSelector.close();
	}

	public void stopCapturingChanges() throws InterruptedException {

		logger.debug("Stopping to capture changes");

		if (producer != null)
			producer.close();

		if (engine != null) {

			logger.debug("Stopping Debezium engine");
			engine.stop();

			try {
				while (!engine.await(30, TimeUnit.SECONDS)) {
					logger.debug("Waiting another 30 seconds for the embedded engine to shut down");
				}
			} catch (InterruptedException e) {
				Thread.interrupted();
			}
		}
	}

	/**
	 * 处理监听事件
	 * @param sourceRecord
	 */
	private void handleEvent(SourceRecord sourceRecord) {
		try {
			
			String topic = sourceRecord.topic();
			// TODO 可能会有不了解造成的问题，如果发现再去解决

			// 如果是表的记录发生改变，topic 的格式应该是 <prefix>.<databaseName>.<tableName>
			if (topic.split("\\.").length < 3) {
				logger.error("topic 格式不符合记录改变的事件 " + topic);
				return;
			}
			
			// 有数据库记录发生改变
			PublishedEvent event = new PublishedEvent();

			Struct value = (Struct) sourceRecord.value();
			
			// OptionType
			// 参考框架中的实现-Envelope (RecordMakers.RecordsForTable)--- CREATE("c"),
			// UPDATE("u"), DELETE("d");
			String optionCode = value.get(FieldName.OPERATION).toString();
			if (optionCode.equals("c")) {
				event.setOperationType(Operation.CREATE);
			} else if (optionCode.equals("u")) {
				event.setOperationType(Operation.UPDATE);
			} else if (optionCode.equals("d")) {
				event.setOperationType(Operation.DELETE);
			}
			// database &&　table
			Struct source = value.getStruct("source");
			String db = source.getString("db");
			event.setDatabase(db);
			String table = source.getString("table");
			event.setTable(table);
			// instanceID
			//如果是添加或者修改,则取值after 中，如果是删除则是去before
			if (optionCode.equals("d")){
				Struct before = value.getStruct("before");
				String entityID = "" + before.getInt32("id");
				event.setEntityID(entityID);
			}else{			
				Struct after = value.getStruct("after");
				String entityID = "" + after.getInt32("id");
				event.setEntityID(entityID);				
			}
			//如果是修改操作,则生成修改记录事件发送到对应topic进行处理
			if (optionCode.equals("u")){
				Struct after = value.getStruct("after");
				Struct before = value.getStruct("before");
				
				 
				UpdateBoEvent updateBoEvent = new UpdateBoEvent();
				updateBoEvent.setDatabase(db);
				updateBoEvent.setTable(table);
				String entityID = "" + after.getInt32("id");
				updateBoEvent.setEntityID(entityID);	
				Long timeInMs = (Long)value.get(FieldName.TIMESTAMP);
				updateBoEvent.setTimeInMs(timeInMs);
				//获取所有的列
				//TODO
				List<Field> fieldList = after.schema().fields();
				//逐一比较前后的列的值
				for(int i=0;i<fieldList.size();i++){
					Field field = fieldList.get(i);
					//如果前后不相等
					if (!after.get(field).equals(before.get(field))){
						//添加修改的列名
						updateBoEvent.getFieldNameList().add(field.name());
					} 
				}
				//发送到对应topic
				// 发送到主题
				try {
					producer.send(CdcEventConst.TOPIC_UPDATE_OPERATION, updateBoEvent.getKey(),toJson(updateBoEvent)).get(10, TimeUnit.SECONDS);
				} catch (Exception e) {
					logger.error("error publishing to "	+ CdcEventConst.TOPIC_UPDATE_OPERATION, e);
					//日志记录，不抛出异常
				} 
			}
			
			logger.info("received an event :" + event);
			// 发送到主题
			try {
				producer.send(CdcEventConst.TOPIC_EVENT_CDC, event.getKey(),toJson(event)).get(10, TimeUnit.SECONDS);
			} catch (RuntimeException e) {
				logger.error("error publishing to "	+ CdcEventConst.TOPIC_EVENT_CDC, e);
				throw e;
			} catch (Throwable e) {
				logger.error("error publishing to "	+ CdcEventConst.TOPIC_EVENT_CDC, e);
				throw new RuntimeException(e);
			}

		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}

	public static String toJson(Object eventInfo) {
		
		try {
			return om.writeValueAsString(eventInfo);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}

	// public static class MyKafkaOffsetBackingStore extends
	// KafkaOffsetBackingStore {
	//
	// @Override
	// public void configure(WorkerConfig configs) {
	// Map<String, Object> updatedConfig = new HashMap<>(configs.originals());
	// updatedConfig.put("bootstrap.servers", kafkaBootstrapServers);
	// super.configure(new WorkerConfig(configs.));
	// }
	// }

}
