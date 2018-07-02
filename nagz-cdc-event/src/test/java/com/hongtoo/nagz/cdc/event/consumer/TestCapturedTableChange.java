package com.hongtoo.nagz.cdc.event.consumer;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;

import com.hongtoo.nagz.cdc.event.common.CdcEventConst;
import com.hongtoo.nagz.redis.utils.RedisUtils;

public class TestCapturedTableChange {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//更新缓存
		RedisUtils.pushObjectByKey(CdcEventConst.KEY_CAPTRUE_TALBES,"msgsteal.user_trade,msgsteal.user_custom");
		
		//修改zk 节点		
		 CuratorFramework client = CuratorFrameworkFactory.newClient("127.0.0.1:2181", 
				 new ExponentialBackoffRetry(1000,3));
         client.start(); 
         try {
			client.setData().forPath(CdcEventConst.TABLE_CHANGED_NODEPATH,(""+System.currentTimeMillis()).getBytes());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("update over");
	}

}
