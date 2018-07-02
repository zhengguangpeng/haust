package com.hongtoo.nagz.cdc.event.consumer;

 

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class TestDebeziumDate {

	static final long MILLISECONDS_PER_SECOND = TimeUnit.SECONDS.toMillis(1);
    static final long MICROSECONDS_PER_MILLISECOND = TimeUnit.MILLISECONDS.toMicros(1);
    static final long NANOSECONDS_PER_MILLISECOND = TimeUnit.MILLISECONDS.toNanos(1);
    
	public static void main(String[] args) throws Exception{
		System.out.println(System.currentTimeMillis());
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		Date date =  format.parse("1968-03-28 08:52:11.419");
		 
        
        System.out.println(new Date(1506582466006l));
	}
}
