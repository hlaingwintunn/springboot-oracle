package com.hlaing.springoracle.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hlaing.springoracle.config.ConfigProperties;
import com.hlaing.springoracle.config.OracleProperties;
import com.hlaing.springoracle.model.Location;

@Service
public class LocationService {
	private static final Logger logger = LoggerFactory.getLogger(LocationService.class);
	
	@Autowired
	private OracleProperties oracleProperties;
	
	@Autowired
	private ConfigProperties configProperties;
	
	public void init() {
		readAlldataFromfile();
	}

	private List<Location> readAlldataFromfile() {
		 String fullFileName = null;
		 final List<Location> locations = new ArrayList<>();
		 
		 try {
			 
			 final Path currentRelativePath = Paths.get("");
			 final String s = currentRelativePath.toAbsolutePath().toString();
			 final String fileName = configProperties.getGeoFile();
			 fullFileName = s.concat(fileName);
			 
			 final BufferedReader br = new BufferedReader(new FileReader(fullFileName));
			 
			 String line = null;
			 while((line = br.readLine()) != null) {
				 final String[] field = line.split(";");
				 final String postalCode = field[0];
				 final double longitude = Double.parseDouble(field[1]);
				 final double latitude = Double.parseDouble(field[2]);
				 
				 final Location location = new Location(latitude, longitude);
				 locations.add(location);
			 }
			 
			 logger.debug("loaded {} geoData from file", locations.size());
		 }catch(Exception e) {
			 logger.error("failed to read data from file {}", e);
		 }
		 
		 return locations;
		
	}
	
	public Optional<Connection> openConnection() {
		Connection connection = null;
		
		try {
			connection = DriverManager.getConnection(oracleProperties.getUrl(), oracleProperties.getUsername(), oracleProperties.getPassword());
			
		}catch(SQLException e) {
			logger.error("Failed to connect oracle {}", e);
		}
		
		return Optional.ofNullable(connection);
		
	}
	
	public boolean closeConnection(final Connection connection) {
		try {
			if(!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.error("Failed to close oracle connection {}", e);
			return false;
		}
		
		return true;
	}

}
