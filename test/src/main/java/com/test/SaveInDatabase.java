package com.test;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Queue;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Statement;

public class SaveInDatabase {
	
	/*
	 * check the link to install & make sure hsqldb is installed correctly
	 * https://stackoverflow.com/questions/7053721/concept-of-in-memory-database-and
	 * -how-to-see-if-my-data-is-being-populated-in-hsq/7122466#7122466
	 * 
	 */
	
	private static final Logger logger = LoggerFactory.getLogger(SaveInDatabase.class);


	protected void saveInLogEvents(Queue<LogEvent> queue) {
		Connection connection = null;
		Statement statement = null;
		try {
			Class.forName("org.hsqldb.jdbc.JDBCDriver");
			connection = DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/aliasdb", "SA", "");
			statement = connection.createStatement();
			int databaseNameAlreadyExist = 0;

			ResultSet checkTableExistedOrNot = statement.executeQuery(
					"SELECT COUNT(*) FROM information_schema.tables  WHERE table_schema = 'PUBLIC'  AND table_name = 'EVENTLOGS';");

			while (checkTableExistedOrNot.next()) {
				databaseNameAlreadyExist = checkTableExistedOrNot.getInt(1);
			}

			if (databaseNameAlreadyExist != 1) {
				logger.info("creating table EVENTLOGS");
				int insertTable = statement.executeUpdate("CREATE TABLE EVENTLOGS " + "( "
						+ "ID VARCHAR(250) NOT NULL, " + "EVENTDURATION INTEGER NOT NULL, "
						+ "TYPE VARCHAR(250) NOT NULL, " + "HOST VARCHAR(250) NOT NULL,"
						+ "ALERT  BOOLEAN DEFAULT FALSE NOT NULL, " + ");" + "");
				System.out.println(insertTable);
				connection.commit();
				logger.info("table created in EVENTLOGS");
			}else {
				logger.info("Table & Database already existed");

			}
			String query = " insert into EVENTLOGS (ID, EVENTDURATION, TYPE, HOST, ALERT)" + " values (?, ?, ?, ?, ?)";

			// create the sql insert preparedstatement
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			for (LogEvent logItems : queue) {

				// process each result
				String eventID = logItems.getId();
				preparedStatement.setString(1, eventID);
				int eventDuration = logItems.duration;
				preparedStatement.setInt(2, eventDuration);
				if (logItems.getType() != null) {
					String eventType = logItems.getType();
					preparedStatement.setString(3, eventType);
				} else
					preparedStatement.setString(3, "noData");

				if (logItems.getHost() != null) {
					String eventHost = logItems.getHost();
					preparedStatement.setString(4, eventHost);
				} else
					preparedStatement.setString(4, "noData");

				boolean eventAlert = logItems.alert;
				preparedStatement.setBoolean(5, eventAlert);

				int row = preparedStatement.executeUpdate();
				System.out.println(row);
			}

			// to make sure date is set in database
			String checkDatainDatabase = "SELECT * FROM PUBLIC.EVENTLOGS";
			PreparedStatement preparedStatementother = connection.prepareStatement(checkDatainDatabase);

			ResultSet result = preparedStatementother.executeQuery();
			int i = 1;
			if (result.next()) {
				System.out.println("Checking DATA IN TABLE");
				System.out.println(result.getRow());
				System.out.println(result.getString(i));
				i++;

			}

			connection.commit();
			statement.close();
			connection.close();

		} catch (SQLException e) {
			//System.err.format("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
			logger.error("SQL State: %s\n%s", e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			logger.error("Error in saving in DB: %s\n%s", e);
		}

		// System.out.println("Table created successfully");
	}

}
