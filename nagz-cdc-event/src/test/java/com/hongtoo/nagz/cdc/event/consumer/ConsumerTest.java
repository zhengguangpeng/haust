package com.hongtoo.nagz.cdc.event.consumer;
 

import java.util.ArrayList;
import java.util.List;
 
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.function.Function;

import org.apache.kafka.clients.consumer.ConsumerRecord;
   
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hongtoo.nagz.cdc.event.common.CdcEventConst;
import com.hongtoo.nagz.cdc.event.common.PublishedEvent;
import com.hongtoo.nagz.cdc.event.kafka.consumer.CdcEventKafkaConsumer;

public class ConsumerTest {

	public static void main(String[] args) throws Exception{
		// TODO Auto-generated method stub
		String subscriberId = CdcEventConst.SUBSCRIBE_GROUP_ID ;
		CountDownLatch latch = new CountDownLatch(20);
		BlockingQueue<PublishedEvent> result = new LinkedBlockingDeque<>();
 
		subscribe(subscriberId,pe -> {        
              result.add(pe);
              latch.countDown();
            return CompletableFuture.completedFuture(null);
          });
		
		
		System.out.println(result.size());
		for(PublishedEvent event:result){
			System.out.println(event);
		}
		System.out.println("test over");
		
		latch.await();
		
	}
	
	private static PublishedEvent toPublishedEvent(ConsumerRecord<String, String> record) {
		try {
			ObjectMapper om = new ObjectMapper();
			PublishedEvent pe = om.readValue(record.value(),PublishedEvent.class);
			return pe;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static CompletableFuture<?> subscribe(String subscriberId,			 
			Function<PublishedEvent, CompletableFuture<?>> handler) {

		List<String> topics = new ArrayList<String>();
		topics.add(CdcEventConst.TOPIC_EVENT_CDC);
		String bootstrapServers = "im01.demo.com:9092";
		CdcEventKafkaConsumer consumer = new CdcEventKafkaConsumer(
				subscriberId, (record, callback) -> {
					PublishedEvent se = toPublishedEvent(record);					
						handler.apply(se).whenComplete((result, t) -> {
							callback.accept(null, t);
						});
				}, topics,
				bootstrapServers);

		 
		consumer.start();

		return CompletableFuture.completedFuture(null);

	}

}
