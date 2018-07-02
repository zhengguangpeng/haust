package com.hongtoo.nagz.cdc.event.kafka;

import org.hibernate.validator.constraints.NotBlank;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("eventcdc.kafka")
public class CdcEvetKafkaConfigurationProperties {

	@NotBlank
	private String bootstrapServers;

	private long connectionValidationTimeout = 1000;

	public String getBootstrapServers() {
		return bootstrapServers;
	}

	public void setBootstrapServers(String bootstrapServers) {
		this.bootstrapServers = bootstrapServers;
	}

	public long getConnectionValidationTimeout() {
		return connectionValidationTimeout;
	}

	public void setConnectionValidationTimeout(long connectionValidationTimeout) {
		this.connectionValidationTimeout = connectionValidationTimeout;
	}
}
