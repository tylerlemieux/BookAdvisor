package bookadvisor.logging;

import java.io.Closeable;
import java.io.EOFException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Stream;

// Singleton object Logger
public class Logger implements AutoCloseable {
	private ObjectOutputStream outfile;
	private ArrayList<LogObject> logObjects;
	private final String logFileName = "log.dat";
	
	protected Logger(){
		logObjects = new ArrayList<LogObject>();
		loadObjects();
		try {
			outfile = new ObjectOutputStream(new     
			        FileOutputStream(logFileName));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			outfile = null;
		} 
	}
	
	private static Logger loggerInstance;
	public static Logger GetInstance(){
		if(loggerInstance == null){
			loggerInstance = new Logger();
		}
		
		return loggerInstance;
	}
	
	public void LogError(String action){
		Log(action, LogLevel.Error);
	}
	
	public void LogError(String action, String extraInformation){
		Log(action, extraInformation, LogLevel.Error);
	}
	
	public void Log(String action, LogLevel level){
		this.Log(action, null, level);
	}
	
	public void Log(String action, String extraInformation, LogLevel level){
		// intent: add an item to the log queue to be written to the output file
		// postcondition: LogObject is in the queue
		
		LogObject logEntry = new LogObject(action, level, extraInformation);
		logObjects.add(logEntry);
		
		try {
			this.outfile.writeObject(logEntry);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}    
	}
	
	private void loadObjects(){
		try (ObjectInputStream infile = new ObjectInputStream(new FileInputStream(logFileName));)       
		{
			try{
				while (true)
				{
					logObjects.add((LogObject) infile.readObject());
				} 
			} catch (EOFException e){
				//end of file reached swallow the error
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} finally {
				try {
					infile.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void PrintErrorLogs(){
		logObjects.stream()
			.filter(s -> s.getLogLevel() == LogLevel.Error)
			.forEach(s -> System.out.println(s.toString()));
	}
	
	public void PrintErrorLogs(long numLines){
		logObjects.stream()
			.filter(s -> s.getLogLevel() == LogLevel.Error)
			.limit(numLines)
			.forEach(s -> System.out.println(s.toString()));
	}
	
	public void PrintInfoLogs(){
		logObjects.stream()
			.filter(s -> s.getLogLevel() == LogLevel.Information)
			.forEach(s -> System.out.println(s.toString()));	
	}
	
	public void PrintInfoLogs(long numLines){
		logObjects.stream()
			.filter(s -> s.getLogLevel() == LogLevel.Information)
			.limit(numLines)
			.forEach(s -> System.out.println(s.toString()));
	}
	
	public void PrintLogs(){
		logObjects.stream()
			.forEach(s -> System.out.println(s.toString()));
	}
	
	public void PrintLogs(long numLines){
		logObjects.stream()
			.limit(numLines)
			.forEach(s -> System.out.println(s.toString()));
	}
	
	@Override
	public void close(){
		try {
			outfile.flush();
			outfile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
