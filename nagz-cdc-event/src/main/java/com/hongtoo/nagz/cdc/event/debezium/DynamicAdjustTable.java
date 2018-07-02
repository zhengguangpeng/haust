package com.hongtoo.nagz.cdc.event.debezium;

 
import java.nio.charset.Charset;

import javax.annotation.PostConstruct;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hongtoo.nagz.cdc.event.common.CdcEventConst;

/**
 * 动态调整监控表
 * @author 肥羊
 *
 */
public class DynamicAdjustTable {

	private static final Logger logger = LoggerFactory.getLogger(DynamicAdjustTable.class);
	
	private final EventTableChanges2TopicRelay relay;	
	private final CuratorFramework curatorClient;
	private final long startTime = System.currentTimeMillis();
	
	public DynamicAdjustTable(EventTableChanges2TopicRelay changes2TopicRelay,
			CuratorFramework client){
		this.relay = changes2TopicRelay;
		this.curatorClient = client;
		 
	}
	
	
	@PostConstruct
	public void start() {
		//判断节点是否存在,如果不存在则先创建
		try {
			boolean isNodeExist = curatorClient.checkExists().forPath(CdcEventConst.TABLE_CHANGED_NODEPATH) != null;
			if (!isNodeExist){
				String value = System.currentTimeMillis()+"";
				curatorClient.create().creatingParentsIfNeeded().forPath(CdcEventConst.TABLE_CHANGED_NODEPATH,					
						value.getBytes(Charset.forName("UTF-8")));
			} 
		} catch (Exception e) {
			logger.error("创建监听表变化的zk node 出错:　"+e.getLocalizedMessage());
		}
		  
		//监听节点的变化事件,如果变化了，就要动态去处理表
		NodeCache nodeCache = new NodeCache(curatorClient, CdcEventConst.TABLE_CHANGED_NODEPATH);
		NodeCacheListener nodeCacheListener = new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                ChildData childData = nodeCache.getCurrentData();
                
                //只有是leader节点才响应
                if (!relay.isLeader()){
                	return;
                }
                
                String value = new String(childData.getData(),Charset.forName("UTF-8"));
                if (Long.parseLong(value) < startTime){
                	//早先的节点变化事件不关注
                	return;
                }
                logger.info("监控表节点 收到变化事件通知,leader 进行相应处理 ");
                //更新监控表
                relay.resetTablesCaptured();                
            }
        };
        nodeCache.getListenable().addListener(nodeCacheListener);
        try {
			nodeCache.start();
		} catch (Exception e) {
			logger.error("监控表 节点注册");
		}
		  
	}
	
	
}
