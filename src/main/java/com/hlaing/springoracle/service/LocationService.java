package com.hlaing.springoracle.service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
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

	private static final String createTableSQL = "CREATE TABLE GEOLOCATION(" + "ID CHAR(50) NOT NULL, "
			+ "POSTALCODE NUMBER, " + "LATITUDE BINARY_DOUBLE, " + "LONGITUDE BINARY_DOUBLE, " + "PRIMARY KEY (ID) "
			+ ")";

	private static final String insertTableSQL = "INSERT INTO GEOLOCATION"
			+ "(ID, POSTALCODE, LATITUDE, LONGITUDE) VALUES" 
			+ "(?,?,?,?)";
	
	private static final String truncateTableSQL = "TRUNCATE TABLE GEOLOCATION";

	@Autowired
	private OracleProperties oracleProperties;

	@Autowired
	private ConfigProperties configProperties;

	public void init() {
		readAlldataFromfile();
	}

	public List<Location> readAlldataFromfile() {
		String fullFileName = null;
		final List<Location> locations = new ArrayList<>();

		try {

			final Path currentRelativePath = Paths.get("");
			final String s = currentRelativePath.toAbsolutePath().toString();
			final String fileName = configProperties.getGeoFile();
			fullFileName = s.concat(fileName);

			final BufferedReader br = new BufferedReader(new FileReader(fullFileName));

			String line = null;
			while ((line = br.readLine()) != null) {
				final String[] field = line.split(";");
				final long postalCode = Long.parseLong(field[0]);
				final double longitude = Double.parseDouble(field[1]);
				final double latitude = Double.parseDouble(field[2]);

				Location location = new Location().setLogitude(longitude).setLatitude(latitude)
						.setPostalCode(postalCode).computeId();
				locations.add(location);
			}

			logger.debug("loaded {} geoData from file", locations.size());
		} catch (Exception e) {
			logger.error("failed to read data from file {}", e);
		}

		return locations;

	}

	public void insertGeoLocation(final List<Location> locations) {
		final Optional<Connection> conOption = openConnection();
		final Connection connection = conOption.get();

		for (Location location : locations) {
			try (PreparedStatement stmt = connection.prepareStatement(insertTableSQL)) {
				stmt.setString(1, location.getId());
				stmt.setLong(2, location.getPostalCode());
				stmt.setDouble(3, location.getLatitude());
				stmt.setDouble(4, location.getLogitude());

				stmt.executeUpdate();
				//connection.commit();

			} catch (SQLException e) {
				logger.error("Failed to insert geoData to oracle {}", e);
			}
		}
		closeConnection(connection);
		logger.info("GeoLocation data {} are inserted to Oracle", locations.size());
	}
	
	public boolean truncateTable() {
		final Optional<Connection> conOption = openConnection();
		final Connection connection = conOption.get();
		
		try(PreparedStatement stmt = connection.prepareStatement(truncateTableSQL)) {
			stmt.executeUpdate();
			connection.commit();
			logger.info("Cleared all geoLocation data");
		}catch (SQLException e) {
			logger.error("Failed to delete table data {}", e);
			return false;
		} finally {
			closeConnection(connection);
		}
		
		return true;
	}

	public void createTable() throws SQLException {
		Connection connection = null;
		Statement stmt = null;

		try {
			final Optional<Connection> connOption = openConnection();
			connection = connOption.get();

			stmt = connection.createStatement();

			stmt.execute(createTableSQL);
			logger.info("Table is created");

		} catch (SQLException e) {
			logger.error("Oracle connection failed {}", e);
		} finally {
			if (stmt != null) {
				stmt.close();
			}

			closeConnection(connection);
		}
	}

	public Optional<Connection> openConnection() {
		Connection connection = null;

		try {
			connection = DriverManager.getConnection(oracleProperties.getUrl(), oracleProperties.getUsername(),
					oracleProperties.getPassword());

		} catch (SQLException e) {
			logger.error("Failed to connect oracle {}", e);
		}

		return Optional.ofNullable(connection);

	}

	public boolean closeConnection(final Connection connection) {
		try {
			if (!connection.isClosed()) {
				connection.close();
			}
		} catch (SQLException e) {
			logger.error("Failed to close oracle connection {}", e);
			return false;
		}

		return true;
	}

}
