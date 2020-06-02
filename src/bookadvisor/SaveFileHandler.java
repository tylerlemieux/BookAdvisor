package bookadvisor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class SaveFileHandler<T extends IDataRetrievable>  {
	public final String fileName;
	
	public SaveFileHandler(String fileName){
		this.fileName = fileName;
	}
	
	public void SaveData(ArrayList<T> data){
		// intent: save an array list of generic typed data to a file
		// precondition: must extend IDataRetrievable which defines a HashMap of element names and 
		//   				values
		// postcondition: file exists with formatted data for each item provided
		FileWriter fileWriter = null;
		try{
			fileWriter = new FileWriter(this.fileName);
			for(int i=0; i<data.size();i++){
				// add "IDataRetrievable" interface and give it a method that returns each property and values
				// call interface method and save each entry to the file
				HashMap<String, String> propertiesToSave = data.get(i).GetStringValues();
				
				// Weird hack to fix access error - but wrap line in an array
				String[] line = new String[]{""};
			
				propertiesToSave.forEach((String key, String value) -> {
					if(line[0] != ""){
						line[0] += "|";
					}
					line[0] += key + ":" + value;
				});
				
				fileWriter.write(line[0] + "\n");
			}
			
			fileWriter.flush();
			fileWriter.close();
			
		} catch (IOException ex) {
			System.out.println(this.fileName + " does not exist");
		} 
	}
	
	public ArrayList<T> GetData(IFactory<T> objectFactory){
		// intent: return an array list of generic typed data read from a formatted file
		// precondition: factory exists that can take a hashmap of properties and create the object
		// postcondition: data from the file has been instantiated into Java objects using a factory
		Scanner dataFile = null;
		try{
			dataFile = new Scanner(new File(this.fileName));
			
			ArrayList<T> data = new ArrayList<T>();
			// instantiate the object using a factory pattern
			while(dataFile.hasNext()){
				String[] properties = dataFile.nextLine().split("\\|");
				HashMap<String, String> propertyMap = new HashMap<String, String>();
				for(int i=0; i<properties.length; i++){
					String[] keyValues = properties[i].split("\\:");
					propertyMap.put(keyValues[0], keyValues[1]);	
				}
				
				T object = objectFactory.Create(propertyMap);
				data.add(object);
			}
			
			return data;
			
		} catch (IOException ex) {
			System.out.println(this.fileName + " does not exist");
			return new ArrayList<T>();
		} finally {
			dataFile.close();
		}
	}
}
