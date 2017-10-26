package com.hlaing.springoracle;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.annotations.Test;

import com.hlaing.springoracle.service.LocationService;

@ContextConfiguration (classes = SpringOracleApplication.class)
public class ReadFileTest extends AbstractTestNGSpringContextTests {
	
	@Autowired
	private LocationService locationService;
	
	@Test (enabled = true)
	public void test() {
		System.err.println("Test method");
	}
}
