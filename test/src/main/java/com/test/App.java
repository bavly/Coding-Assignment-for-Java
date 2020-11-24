package com.test;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Hello world!
 *
 */
public class App {

	final static String fileName = "c://logs/logfile.txt";

	private static final Logger logger = LoggerFactory.getLogger(App.class);
	private static Queue<LogEvent> resultQueue = new LinkedList<LogEvent>();

	public static void main(String[] args) throws IOException {

		// Lambda Runnable
		// thread can not be parallel here as database need data from Queue<LogEvent>
		// Runnable task1 = () -> {//}; //new Thread(task1).start();
		//logger.info(Thread.currentThread().getName() + " is running 1");
		
		logger.info("The file path is: {}", fileName);
		logger.debug("Debugging application will start proccessing data...");
		ReadAndParseJson test = new ReadAndParseJson();
		resultQueue = test.readLineByLineAndParseJson(fileName);
		SaveInDatabase resultSaveInDatabase = new SaveInDatabase();
		resultSaveInDatabase.saveInLogEvents(resultQueue);

	}

}
