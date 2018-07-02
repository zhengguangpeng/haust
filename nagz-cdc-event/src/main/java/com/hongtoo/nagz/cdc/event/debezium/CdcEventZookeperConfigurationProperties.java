package com.hongtoo.nagz.cdc.event.debezium;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("eventcdc.zookeeper")
public class CdcEventZookeperConfigurationProperties {

	@NotBlank
	private String connectionString;

	public String getConnectionString() {
		return connectionString;
	}

	public void setConnectionString(String connectionString) {
		this.connectionString = connectionString;
	}
}
