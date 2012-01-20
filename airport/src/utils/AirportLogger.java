package utils;

import java.io.File;
import java.io.IOException;

import org.apache.log4j.FileAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;

import p2pmpi.mpi.MPI;

public class AirportLogger {
	private Logger log = Logger.getLogger("airport");
	
	public static Logger getLogger() {
		return AirportLogger.getInstance().log;
	}
	
	private static AirportLogger instance;
	
	public static AirportLogger getInstance() {
		if (instance == null) {
			instance = new AirportLogger();
		}
		
		return instance;
	}
	
	private AirportLogger() {
		File logFile = createNewLogFile("airport-" + MPI.COMM_WORLD.Rank() + ".txt");
		//ConsoleAppender consolLog = new ConsoleAppender(new SimpleLayout());
		FileAppender fileLog;
		try {
			fileLog = new FileAppender(new SimpleLayout(), logFile.getAbsolutePath(), false);
			log.addAppender(fileLog);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//log.addAppender(consolLog);
	}
	
	public File createNewLogFile(String filename){
		File f = new File("/tmp/" + filename);
		f.delete();
		return f;
	}
}
