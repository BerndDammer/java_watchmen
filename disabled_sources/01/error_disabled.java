package watchmen.logging;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import wefwefwe.common;

public class error {

	private static final Logger consoleLogger = Logger.getLogger("CONSOLE");
	private final static Logger fileLogger = Logger.getLogger("ERROR");
	private static String lineSeparator = System.getProperty("line.separator");
	private final static String logTitle = lineSeparator
			+ "*****************************************************************************" + lineSeparator
			+ "************************      IO-MASTER        ******************************" + lineSeparator
			+ "************************    CONSOLE LOGGER     ******************************" + lineSeparator
			+ "*****************************************************************************" + lineSeparator;
	private final static String errorTitle = lineSeparator
			+ "*****************************************************************************" + lineSeparator
			+ "************************      IO-MASTER        ******************************" + lineSeparator
			+ "************************     ERROR LOGGER      ******************************" + lineSeparator
			+ "*****************************************************************************" + lineSeparator;

	

	public static void loadProperties() {

	}

	static {
		//create logging properties for defaul logger, which can be called by getanonymousLogger
		Properties myLogPoperties = new Properties();

		
		//output file for the logging information
		FileHandler fh = null;
		ConsoleHandler ch = null;
		try {
			//configurate simpleformmater
			SingleLineFormatter sf = new SingleLineFormatter();
//			sf.format(new LogRecord(Level.ALL, "%4$s: [%1$tc]  <<<%5$s>>>%n"));
			
			//configurate filehandler
			String OS = System.getProperty("os.name").toLowerCase();
			if (OS.contains("linux")) {
				fh = new FileHandler(common.LINUX_ERROR_FILE);
			} else if (OS.contains("win")) {
				fh = new FileHandler(common.WINDOW_ERROR_FILE);
			}
			fh.setLevel(Level.ALL);
			fh.setFormatter(sf);
			
			//configurate consolehandler
			ch = new ConsoleHandler();
			ch.setLevel(Level.ALL);
			ch.setFormatter(sf);
		} catch (SecurityException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//			myLogPoperties.setProperty("handlers", "java.util.logging.FileHandler");
//			myLogPoperties.setProperty("java.util.logging.FileHandler.level", "ALL");
//			myLogPoperties.setProperty("java.util.logging.FileHandler.formatter", "java.util.logging.SimpleFormatter");
//			String OS = System.getProperty("os.name").toLowerCase();
//			if (OS.contains("linux")) {
//				myLogPoperties.setProperty("java.util.logging.FileHandler.pattern", common.LINUX_ERROR_FILE);
//			} else if (OS.contains("win")) {
//				myLogPoperties.setProperty("java.util.logging.FileHandler.pattern", common.WINDOW_ERROR_FILE);
//			}
		
//		myLogPoperties.setProperty(".level", "INFO");
//		myLogPoperties.setProperty("handlers", "java.util.logging.ConsoleHandler");
//		myLogPoperties.setProperty("java.util.logging.ConsoleHandler.level", "ALL");
//		myLogPoperties.setProperty("java.util.logging.ConsoleHandler.formatter", "java.util.logging.SimpleFormatter");
//		myLogPoperties.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: [%1$tc]  <<<%5$s>>>%n");
		//myLogPoperties.setProperty("useParentHandlers", "false");
		//make input stream of properties
		try {
			ByteArrayOutputStream output = new ByteArrayOutputStream();
			myLogPoperties.store(output, null);
			ByteArrayInputStream input = new ByteArrayInputStream(output.toByteArray());
			LogManager.getLogManager().readConfiguration(input);
			// LogManager.getLogManager().reset();
		} catch (IOException xe) {
			// TODO what to do here
			end();
		}
		consoleLogger.addHandler(ch);
		consoleLogger.info(logTitle);
		fileLogger.addHandler(fh);
		fileLogger.info(errorTitle);

	}

//	private error() {
//		configuration(position);
//	}

	public static void log(String position, String s) {
		consoleLogger.log(Level.INFO, "<" + position + "> : " +  s);
		fileLogger.log(Level.INFO, "<" + position + "> : " + s);
	}
	
	public static void warning(String position, String s) {
		consoleLogger.log(Level.WARNING, "<" + position + "> : " + s);
		fileLogger.log(Level.WARNING, "<" + position + "> : " + s);
	}

	public static void notNull(Object o) {
		if (o == null) {
			consoleLogger.log(Level.SEVERE, "Object must not be NULL ");
			fileLogger.log(Level.SEVERE, "Object must not be NULL ");
			end();
		}
	}

	public static void exception(Exception e) {
		e.printStackTrace();
		consoleLogger.throwing("error classs", "error.throwing", e);
		fileLogger.throwing("error classs", "error.throwing", e);
		end();
	}

	public static void exit(String position, String text) {
		consoleLogger.severe("<" + position + "> : " + text);
		fileLogger.severe("<" + position + "> : " + text);
		end();
	}

	public static void configuration(String position, String text) {
		consoleLogger.severe("<" + position + "> : configuration error: "+ text);
		fileLogger.severe("<" + position + "> : configuration error: "+ text);
//		end();
	}

	public static void notimplemented(String position) {
		consoleLogger.severe("<" + position + "> : not implemented");
		fileLogger.severe("<" + position + "> : not implemented");
//		end();
	}

	private static void end() {
		consoleLogger.severe("System.exit(1)");
		fileLogger.severe("System.exit(1)");
		System.exit(1);
	}

//	public static void d(int delay) {
//		try {
//			Thread.sleep((long) delay);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
//	}
	
	
}
