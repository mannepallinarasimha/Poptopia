package com.kelloggs.promotions.lib.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.logging.LogLevel;

/**
 * ApiLogger : The logger class is used to handle API logging operations
 * 
 * @author UDIT NAYAK (M1064560)
 * @since 06-03-2022
 */
public class ApiLogger {

	
	/**
	 * Properties of the logger
	 */
	private Logger logger;
	private Class<?> logClass;
	private LogLevel logLevel;

	
	/**	
	 * Instantiate the logger
	 * 
	 * @param logClass	class for which log to be generate, default level is INFO
	 */
	public ApiLogger(Class<?> logClass) {
		super();
		this.logClass = logClass;
		this.logLevel = LogLevel.INFO;
		this.logger = LoggerFactory.getLogger(logClass);
	}


	/**	
	 * Instantiate the logger
	 * 
	 * @param logClass	class for which log to be generate
	 * @param logLevel	log level to be use for logging type, default level is INFO
	 */
	public ApiLogger(Class<?> logClass, LogLevel logLevel) {
		super();
		this.logClass = logClass;
		this.logLevel = logLevel;
		this.logger = LoggerFactory.getLogger(logClass);
	}
	
	
	/**
	 * @return the logClass
	 */
	public Class<?> getLogClass() {
		return logClass;
	}

	/**
	 * @return the logLevel
	 */
	public LogLevel getLogLevel() {
		return logLevel;
	}

	/**
	 * @param logLevel the logLevel to set
	 */
	public ApiLogger setLogLevel(LogLevel logLevel) {
		this.logLevel = logLevel;
		return this;
	}
	
	/**
	 * Generate the log 
	 * 
	 * @param message	message to be log
	 */
	public void log(String message) {
		this.log(logLevel, message);
	}

	/**
	 * Generate the log
	 * 
	 * @param level	log level to be use for logging
	 * @param message	message to be log
	 */
	public void log(LogLevel level, String message) {
		
		switch (level) {
			case INFO:
				logger.info(message);
				break;
			case WARN: 
				logger.warn(message);
				break;
			case ERROR: 
				logger.error(message);
				break;
			case DEBUG: 
				logger.debug(message);
				break;
			case TRACE: 
				logger.trace(message);
				break;
			default:
				throw new IllegalArgumentException(
						String.format("Bad logging level: %s", level));
		}
	}
	
}
