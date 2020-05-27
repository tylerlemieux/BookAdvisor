package bookadvisor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RecommendationEngine {
    private static HashMap<String, Double> weightLookup;
    private final String genreWeightsFile = "genre-weights.txt";
   
	
	public Double getGenreWeight(String genreOne, String genreTwo){
		// intent: returns the weight of two genres
		// postcondition: decimal value is returned showing the weight of the genres
		if(weightLookup == null){
			loadWeightLookupMap();
		}
		
		return weightLookup.get(genreOne + "+" + genreTwo);
	}
	
	public Double getKeywordMatchCount() throws Exception {
		throw new Exception("Not implemented");
	}
	
	public Book getRecommendation(ArrayList<Book> bookList) {
		// intent: get a recommendation of the next book a user should read
		// precondition: there should be a list of books the user has
		// 				with at least one book completed
		// postcondition: returns the book that the user is most likely to like
		
		// TODO this will need to factor in all of the books read... for now it only takes into account the 
		// first in the list that gets found
		Book bookRead = null;
		for(int i=0; i<bookList.size();i++){
			if(bookList.get(i).isCompleted()){
				bookRead = bookList.get(i);
				break;
			}
		}
		
		// TODO for now this will just return the most similar genre book, need to fix this later
		// it also is not yet accounting for the actual rating		
		Double mostSimilarGenreWeight = Double.parseDouble("-1");
		Book mostSimilarBook = null;
		for(int i=0;i<bookList.size();i++){
			if(!bookList.get(i).isCompleted()){
				Double currentWeight = getGenreWeight(bookRead.getGenre(), bookList.get(i).getGenre());
				if(currentWeight > mostSimilarGenreWeight){
					mostSimilarBook = bookList.get(i);
					mostSimilarGenreWeight = currentWeight;
				}	
			}
		}
		
		return mostSimilarBook;
	}
	
	private void loadWeightLookupMap(){
		// intent: load the weightLookup map with weight values from a file
		// precondition: file exists formatted with genre names and weights 
		// postcondition weightLookup map is populated with data
		this.weightLookup = new HashMap<String, Double>();
		Scanner genreLookupDataFile = null;
		try{
			genreLookupDataFile = new Scanner(new File(this.genreWeightsFile));
			
			String firstLine = genreLookupDataFile.nextLine();
			String[] genreOrder = firstLine.trim().split("\\|");
			
			while(genreLookupDataFile.hasNext()){
	            String inputLine = genreLookupDataFile.nextLine();
	            String[] inputs = inputLine.trim().split("\\|");
	            for(int i=1; i<inputs.length;i++){
	            	String firstGenreName = inputs[0];
	            	String secondGenreName = genreOrder[i];
	            	Double weight = Double.parseDouble(inputs[i]);
	            	
	            	weightLookup.put(firstGenreName + "+" + secondGenreName, weight);
	            }
			}
			
		} catch (IOException ex) {
			System.out.println(this.genreWeightsFile + " does not exist");
		
		} finally {
			genreLookupDataFile.close();
		}
		
	}
}
