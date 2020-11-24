package com.test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.TemporaryFolder;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {

	@Rule
	public TemporaryFolder tempFolder = new TemporaryFolder();

	public void testApp() throws IOException {
		File file = tempFolder.newFile(App.fileName);
		assertTrue(file.exists());

	}

	// assigning the values
	@Before
	protected void setUp() {
		
	}

	public void testStreamIsJsonFile() {

		try (Stream<String> stream = Files.lines(Paths.get(App.fileName))) {
			stream.filter(line -> line.endsWith("}")).filter(line -> line.startsWith("{"));
		} catch (IOException e) {
			fail("File is not start with json format");
		}

	}
	
	
	public void testDataReturnedbackFromDB()  {
		
		//compare between resultQueue & database	
		
//		for (LogEvent result : queue) {
//			// process each result
//			logger.info("The data in the file correct & saved in class Logevent : {");
//			logger.info("The value in Queue eventID : {}", result.getId());
//			logger.info("The value in Queue eventDuration : {}", result.duration);
//			logger.info("The value in Queue eventType : {}", result.getType());
//			logger.info("The value in Queue eventHost : {}", result.getHost());
//			logger.info("The value in Queue eventAlert : {}", result.alert);
//			logger.info("The data in the file correct & saved in class Logevent : }");
//
//		}
		
	}

	/**
	 * Create the test case
	 *
	 * @param testName name of the test case
	 */

	public AppTest(String testName) {
		super(testName);
	}

	/**
	 * @return the suite of tests being tested
	 */
	public static Test suite() {
		return new TestSuite(AppTest.class);
	}

	/**
	 * adding test to make sure File existed
	 */

}
