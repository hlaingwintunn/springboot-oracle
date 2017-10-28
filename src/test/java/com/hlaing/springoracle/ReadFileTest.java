package com.hlaing.springoracle;

import static org.testng.Assert.assertNotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.hlaing.springoracle.config.ConfigProperties;
import com.hlaing.springoracle.service.LocationService;

@ContextConfiguration (classes = SpringOracleApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
public class ReadFileTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private ConfigProperties configProperties;
	
	@Test (enabled = true)
	public void readDataFromFileTest() {
		locationService.init();
		assertNotNull(configProperties.getGeoFile());
	}
}
