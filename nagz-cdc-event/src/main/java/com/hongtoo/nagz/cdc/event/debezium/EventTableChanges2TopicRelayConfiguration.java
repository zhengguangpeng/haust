package com.hongtoo.nagz.cdc.event.debezium;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.hongtoo.nagz.cdc.event.kafka.CdcEvetKafkaConfigurationProperties;

@Configuration
@EnableConfigurationProperties({
		EventTableChanges2TopicRelayConfigurationProperties.class,
		CdcEvetKafkaConfigurationProperties.class,
		CdcEventZookeperConfigurationProperties.class,
		CdcStartupValidatorConfigurationProperties.class })
public class EventTableChanges2TopicRelayConfiguration {

	@Bean
	public EventTableChanges2TopicRelay embeddedDebeziumCDC(
			@Value("${spring.datasource.url}") String dataSourceURL,
			EventTableChanges2TopicRelayConfigurationProperties eventTableChangesToAggregateTopicRelayConfigurationProperties,
			CdcEvetKafkaConfigurationProperties eventuateKafkaConfigurationProperties,
			CuratorFramework client, CdcStartupValidator cdcStartupValidator) {
		
		JdbcUrl jdbcUrl = JdbcUrlParser.parse(dataSourceURL);

		return new EventTableChanges2TopicRelay(
				eventuateKafkaConfigurationProperties.getBootstrapServers(),
				jdbcUrl,
				eventTableChangesToAggregateTopicRelayConfigurationProperties.getDbUserName(),
				eventTableChangesToAggregateTopicRelayConfigurationProperties.getDbPassword(),
				eventTableChangesToAggregateTopicRelayConfigurationProperties.getServerId(),
				eventTableChangesToAggregateTopicRelayConfigurationProperties.getServerName(),
				client, cdcStartupValidator);
	}

	@Bean
	public CdcStartupValidator cdcStartupValidator(
			@Value("${spring.datasource.url}") String dataSourceURL,
			EventTableChanges2TopicRelayConfigurationProperties eventTableChangesToAggregateTopicRelayConfigurationProperties,
			CdcEvetKafkaConfigurationProperties eventuateKafkaConfigurationProperties,
			CdcStartupValidatorConfigurationProperties cdcStartupValidatorConfigurationProperties) {
		JdbcUrl jdbcUrl = JdbcUrlParser.parse(dataSourceURL);

		CdcStartupValidator cdcStartupValidator = new CdcStartupValidator(
				jdbcUrl,
				eventTableChangesToAggregateTopicRelayConfigurationProperties.getDbUserName(),
				eventTableChangesToAggregateTopicRelayConfigurationProperties.getDbPassword(),
				eventuateKafkaConfigurationProperties.getBootstrapServers());

		cdcStartupValidator
				.setMySqlValidationMaxAttempts(cdcStartupValidatorConfigurationProperties
						.getMySqlValidationMaxAttempts());
		cdcStartupValidator
				.setMySqlValidationTimeoutMillis(cdcStartupValidatorConfigurationProperties
						.getMySqlValidationTimeoutMillis());
		cdcStartupValidator
				.setKafkaValidationMaxAttempts(cdcStartupValidatorConfigurationProperties
						.getKafkaValidationMaxAttempts());
		cdcStartupValidator
				.setKafkaValidationTimeoutMillis(cdcStartupValidatorConfigurationProperties
						.getKafkaValidationTimeoutMillis());

		return cdcStartupValidator;
	}

	@Bean(destroyMethod = "close")
	public CuratorFramework curatorFramework(
			CdcEventZookeperConfigurationProperties eventuateLocalZookeperConfigurationProperties) {
		String connectionString = eventuateLocalZookeperConfigurationProperties.getConnectionString();
		return makeStartedCuratorClient(connectionString);
	}

	static CuratorFramework makeStartedCuratorClient(String connectionString) {
		RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
		CuratorFramework client = CuratorFrameworkFactory.builder()
				.retryPolicy(retryPolicy).connectString(connectionString)
				.build();
		client.start();
		return client;
	}

	
	@Bean
	public DynamicAdjustTable  dynamicAdjustTable(EventTableChanges2TopicRelay relay,CuratorFramework client){
		return new DynamicAdjustTable(relay,client);
	}
}
