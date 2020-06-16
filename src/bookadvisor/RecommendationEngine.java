package bookadvisor;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class RecommendationEngine {
    private static HashMap<String, Double> weightLookup;
    private final String genreWeightsFile = "genre-weights.txt";
    private static final String[] genres = new String[] { FantasyBook.genreName, MysteryBook.genreName, NonFictionBook.genreName };
   
	
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
		
		GetGenreWeightThread getGenreWeightThread = new GetGenreWeightThread(bookList);
		GetKeywordMatchCountThread getKeywordMatchCountThread = new GetKeywordMatchCountThread(bookList);
		
		// Start the threads
		getGenreWeightThread.start();
		getKeywordMatchCountThread.start();
		
		// Wait for the calculations to finish
		try{
			getGenreWeightThread.join();
			getKeywordMatchCountThread.join();
		} catch (InterruptedException ex){
			System.out.println("Thread was interrupted.");
		}
		
		HashMap<String, Integer> genreWeights = getGenreWeightThread.getResults();
		HashMap<String, Integer> keywordCounts = getKeywordMatchCountThread.getResults();
		
		// Pick the book with the best recommendation score
		Book bestBook = null;
		int highestScore = 0;
		for(int i=0; i<bookList.size();  i++){
			Book currentBook = bookList.get(i);
			if(!currentBook.isCompleted()){
				// Multiply out - one must be added so the genre is still accounted for when there are no matches
				int recommendationScore = genreWeights.get(currentBook.getTitle()) * (keywordCounts.get(currentBook.getTitle()) + 1);
				if(recommendationScore > highestScore){
					bestBook = currentBook;
					highestScore = recommendationScore;
				}
			}
		}
		
		return bestBook;
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

	private class GetGenreWeightThread extends Thread {
		private ArrayList<Book> bookList;
		private HashMap<String, Integer> genreWeightResults; 
		
		public GetGenreWeightThread(ArrayList<Book> bookList){
			this.bookList = bookList;
			genreWeightResults = new HashMap<>();
		}
		
		public void run(){
			// Count get an average score given for each genre
			int[] averageGenreScores = new int[RecommendationEngine.genres.length];
			
			// Outer loop, loop through each genre
			for(int i=0; i<RecommendationEngine.genres.length; i++){
				int totalGenreScore = 0;
				int totalBooks = 0;
				
				// Inner loop, find all the books in the genre
				for (int j=0; j<bookList.size(); j++){
					if(bookList.get(j).isCompleted() && bookList.get(j).getGenre() == RecommendationEngine.genres[i]){
						// Found a completed book in the genre, add it
						totalBooks++;
						totalGenreScore += bookList.get(j).getRating();
					}
				}
				if(totalBooks > 0)
					averageGenreScores[i] = totalGenreScore / totalBooks;
			}
			
			// Multiply the average genre score of each uncompleted
			for(int i=0; i<bookList.size(); i++){
				// Multiply the genre similarity weight by the each of the genres
				int genreWeight = 0;
				for(int j=0;j<RecommendationEngine.genres.length;j++){
					genreWeight += getGenreWeight(bookList.get(i).getGenre(), RecommendationEngine.genres[j]) * averageGenreScores[j];
				}
				genreWeightResults.put(bookList.get(i).getTitle(), genreWeight);
			}
	    }
		
		public HashMap<String, Integer> getResults(){
			return this.genreWeightResults;
		}
	}
	
	private class GetKeywordMatchCountThread extends Thread {
		private ArrayList<Book> bookList;
		private HashMap<String, Integer> keywordScoreResults; 
		
		public GetKeywordMatchCountThread(ArrayList<Book> bookList){
			this.bookList = bookList;
			keywordScoreResults = new HashMap<>();
		}
		public void run(){
			// Get a list of all of the keywords in the completed books
			HashMap<String, Integer> completedKeywordsCountWeightedByRating = new HashMap<>();
			for(int i=0; i<bookList.size(); i++){
				if(bookList.get(i).isCompleted()){
					String[] keywords = bookList.get(i).getKeywords();
					for(int j=0; j<keywords.length; j++){
						if(completedKeywordsCountWeightedByRating.containsKey(keywords[j])){
							// Key exists, add to it
							Integer oldValue = completedKeywordsCountWeightedByRating.get(keywords[j]);
							completedKeywordsCountWeightedByRating.replace(keywords[j], bookList.get(i).getRating() + oldValue);
						} else {
							// Key does not exist, create it
							completedKeywordsCountWeightedByRating.put(keywords[j], bookList.get(i).getRating());
						}
					}
				}
			}
			
			// Loop through each uncompleted book and find how many keyword matches are in already completed books
			for(int i=0; i<bookList.size();i++){
				if(!bookList.get(i).isCompleted()){
					Integer keywordMatchScore = 0;
					// Check each keywords score
					String[] keywords = bookList.get(i).getKeywords();
					for(int j=0;j<keywords.length;j++){
						if(completedKeywordsCountWeightedByRating.containsKey(keywords[j])){
							// Keyword matches, add to the total score
							keywordMatchScore += completedKeywordsCountWeightedByRating.get(keywords[j]);
						}
					}
					
					// Add to the results
					this.keywordScoreResults.put(bookList.get(i).getTitle(), keywordMatchScore);
				}
			}
	    }
		
		public HashMap<String, Integer> getResults(){
			return this.keywordScoreResults;
		}
		
		
	}
}
