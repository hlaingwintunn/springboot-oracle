package com.hlaing.springoracle;

import static org.junit.Assert.assertTrue;
import static org.testng.Assert.assertNotNull;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import org.apache.catalina.util.SystemPropertyReplacerListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.hlaing.springoracle.config.ConfigProperties;
import com.hlaing.springoracle.config.OracleProperties;
import com.hlaing.springoracle.model.Location;
import com.hlaing.springoracle.service.LocationService;

@ContextConfiguration (classes = SpringOracleApplication.class, initializers = ConfigFileApplicationContextInitializer.class)
public class ReadFileTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private LocationService locationService;
	
	@Autowired
	private ConfigProperties configProperties;
	
	@Autowired
	private OracleProperties oracleProperties;
	
	@Test (enabled = false)
	public void readDataFromFileTest() {
		final List<Location> locations = locationService.readAlldataFromfile();
		
       for(int i=0; i < 100; i++) {
    	   locationService.insertGeoLocation(locations.get(i));
       }
		
		assertNotNull(configProperties.getGeoFile());
	}
	
	@Test (enabled = false)
	public void oracleConnectionTest() throws SQLException {
		assertNotNull(oracleProperties.getUrl());
		assertNotNull(oracleProperties.getPassword());
		assertNotNull(oracleProperties.getUsername());
		assertNotNull(oracleProperties.getTableName());
		
		System.err.println("=========================");
		System.err.println("create oracle table");
		System.err.println("=========================");
		
        locationService.createTable();
	}
	
	@Test (enabled = false)
	public void computeIdTest() {
		final long postalCode = 234566;
		final double latitude = 1.344444;
		final double longitude = 103.324432;
		
		final Location location = new Location()
				.setPostalCode(postalCode)
				.setLatitude(latitude)
				.setLogitude(longitude)
				.computeId();
		
		System.err.println(location.getId());
	}
	
	@Test (enabled = false)
	public void truncateTableTest() {
		final boolean result = locationService.dropTable();
		
		assertTrue(result);
	}
	
	@Test (enabled = true)
	public void selectTableTest() {
		final List<Location> result = locationService.getAllDataFromOracle();
		
		assertTrue(result.size() == 100);
	}
}
