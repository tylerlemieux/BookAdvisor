package bookadvisor.logging;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogObject implements Serializable {
	private String action;
	private LocalDateTime timestamp;  
	private String extraData;
	private LogLevel level;
	
	public LogObject(String action, LogLevel level){
		this.action = action;
		this.timestamp = LocalDateTime.now();
		this.extraData = null;
		this.level = level;
	}
	
	public LogObject(String action, LogLevel level, String extraData){
		this.action = action;
		this.timestamp = LocalDateTime.now();
		this.extraData = extraData;
		this.level = level;
	}
	
	public LogLevel getLogLevel(){
		return level;
	}
	
	@Override
	public String toString(){
		return this.level.toString() + " " +
				(DateTimeFormatter.BASIC_ISO_DATE).format(timestamp) + 
				"\tAction: " + action + 
				"\tOther Data: " + extraData;
	}
}
