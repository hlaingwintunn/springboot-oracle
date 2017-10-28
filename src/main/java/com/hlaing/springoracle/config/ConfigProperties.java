package com.hlaing.springoracle.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="config")
public class ConfigProperties {

	private String geoFile;

	public String getGeoFile() {
		return geoFile;
	}

	public void setGeoFile(String geoFile) {
		this.geoFile = geoFile;
	}

}
