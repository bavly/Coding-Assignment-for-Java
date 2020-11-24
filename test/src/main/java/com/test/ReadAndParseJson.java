package com.test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Stream;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ReadAndParseJson {

	
	/*
	 * I have read this to make sure I am using stream in java 
	 * https://www.baeldung.com/java-read-lines-large-file
	 * To make sure I can not have a problem in large files
	 * 
	 */
	private static final Logger logger = LoggerFactory.getLogger(ReadAndParseJson.class);

	Map<String, Long> hashmapTimestamp = new HashMap<>();

	Queue<LogEvent> queue = new LinkedList<LogEvent>();

	protected Queue<LogEvent> readLineByLineAndParseJson(String filePath) {

		JSONParser parser = new JSONParser();
		try (Stream<String> stream = Files.lines(Paths.get(filePath))) {
			logger.info("The file parser process started with File path: {}", filePath);
			stream.filter(line -> line.endsWith("}")).filter(line -> line.startsWith("{")).forEach(line -> {
				LogEvent logInstance = new LogEvent();
				Object objectJson = null;
				try {
					logger.debug("proccessing data file reading line correct end with json format...");
					objectJson = parser.parse(line);
					JSONObject jsonObject = (JSONObject) objectJson;

					logInstance.setId((String) jsonObject.get("id"));
					logInstance.setState((String) jsonObject.get("state"));

					if ((String) jsonObject.get("type") != null || (String) jsonObject.get("type") != "")
						logInstance.setType((String) jsonObject.get("type"));
					if ((String) jsonObject.get("host") != null || (String) jsonObject.get("host") != "")
						logInstance.setHost((String) jsonObject.get("host"));

					logInstance.setTimestamp((long) jsonObject.get("timestamp"));
					logger.info("The data in the file correct & saved in class Logevent : {}", logInstance.getClass());
					logger.debug("All data is set on classInstance called logInstance ");

					// to check if the Id already exist or not
					// if not save it in the map
					if (!hashmapTimestamp.containsKey(logInstance.getId())) {
						hashmapTimestamp.put(logInstance.getId(), logInstance.getTimestamp());

					}
					// else means Id already exist in map then check it to get duration
					// & save the object data in the queue to be able to iterate on it
					else {
						logInstance.duration = (int) Math
								.abs(logInstance.getTimestamp() - (hashmapTimestamp.get(logInstance.getId())));

						if (logInstance.duration > 4)
							logInstance.alert = true;
						else
							logInstance.alert = false;
						queue.add(logInstance);
					}

				} catch (ParseException exception) {
					logger.error("The file parser proccess error occure in parsing file: {}", exception);
				}

			});

		} catch (IOException ex) {
			logger.error("The file parser proccess error occure in Input/Output file: {}", ex);
		}

	
		return queue;

	}

}
