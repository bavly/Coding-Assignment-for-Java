package com.test;

import static org.junit.Assert.assertEquals;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDatabaseTable {

	@BeforeClass
	public static void init() throws SQLException, ClassNotFoundException, IOException {
		Class.forName("org.hsqldb.jdbc.JDBCDriver");

		// initialize database
		initDatabase();
	}

	@AfterClass
	public static void destroy() throws SQLException, ClassNotFoundException, IOException {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			statement.executeUpdate("DROP TABLE EVENTLOGS");
			connection.commit();
		}
	}

	/**
	 * Database initialization for testing i.e.
	 * <ul>
	 * <li>Creating Table</li>
	 * <li>Inserting record</li>
	 * </ul>
	 * 
	 * @throws SQLException
	 */
	private static void initDatabase() throws SQLException {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			System.out.println("creating table EVENTLOGS");
			int insertTable = statement.executeUpdate("CREATE TABLE EVENTLOGS " + "( " + "ID VARCHAR(250) NOT NULL, "
					+ "EVENTDURATION INTEGER NOT NULL, " + "TYPE VARCHAR(250) NOT NULL, "
					+ "HOST VARCHAR(250) NOT NULL," + "ALERT  BOOLEAN DEFAULT FALSE NOT NULL, " + ");" + "");
			System.out.println(insertTable);
			connection.commit();
			System.out.println("table created in EVENTLOGS");
		}
	}

	/**
	 * Create a connection
	 * 
	 * @return connection object
	 * @throws SQLException
	 */
	private static Connection getConnection() throws SQLException {
		return DriverManager.getConnection("jdbc:hsqldb:hsql://localhost/aliasdb", "SA", "");
	}

	/*
	 * Need to add test for insert data
	 * 
	 */

	@Test
	private int getDataFromEVENTLOGS() throws SQLException {
		int row = 0;
		String query = " insert into EVENTLOGS (ID, EVENTDURATION, TYPE, HOST, ALERT)" + " values (?, ?, ?, ?, ?)";

		// create the sql insert preparedstatement
		Connection connection = getConnection();
		PreparedStatement preparedStatement = connection.prepareStatement(query);

		Queue<LogEvent> queue = new LinkedList<LogEvent>();
		ReadAndParseJson test = new ReadAndParseJson();
		queue = test.readLineByLineAndParseJson(App.fileName);

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

			row = preparedStatement.executeUpdate();
			System.out.println(row);
		}
		
		assertEquals(row, 1);
		
		return row;

	}

	/**
	 * Get total records in table
	 * 
	 * @return number of added logs in the database
	 */
	@Test
	private int getTotalRecords() {
		try (Connection connection = getConnection(); Statement statement = connection.createStatement();) {
			ResultSet result = statement.executeQuery("SELECT count(*) as total FROM EVENTLOGS");
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

}
