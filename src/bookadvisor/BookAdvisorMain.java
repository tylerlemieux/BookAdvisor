package bookadvisor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import bookadvisor.UserInputHandler;
import bookadvisor.logging.LogLevel;
import bookadvisor.logging.Logger;

public class BookAdvisorMain {
	public static void main(String[] args) throws IOException{		
		UserInputHandler inputHandler = new UserInputHandler();
		BookAdvisor bookAdvisor = new BookAdvisor();
		Logger logger = Logger.GetInstance();
		
		String lastInput = "";
		while(!lastInput.equals("exit")) {
			lastInput = inputHandler.GetUserInput("Select a menu option \n"
					+ "1. View the list of books\n"
					+ "2. Add a book\n"
					+ "3. Mark a book as completed\n"
					+ "4. View Logs\n"
					+ "5. View past recommendations\n"
					+ "\n"
					+ "Type 'exit' to close");
			if(lastInput.equals("exit")) continue;
			else if(lastInput.equals("1")){
				promptViewingList(bookAdvisor);
			}
			else if(lastInput.equals("2")){
				promptAddBook(bookAdvisor);
			}
			else if(lastInput.equals("3")){
				promptMarkCompleted(bookAdvisor);
			}
			else if(lastInput.equals("4")){
				promptViewLogs();
			}
			else if(lastInput.equals("5")){
				viewPastRecommendations();
			}
			else {
				System.out.println("Invalid input");
				logger.LogError("MainMenuAction", "Invalid Input");
			}
		}	
	}
	
	private static void promptViewingList(BookAdvisor bookAdvisor){
		UserInputHandler inputHandler = new UserInputHandler();

		String genreInput = inputHandler.GetUserInput("Type the genre of books you want to see, type 'all' to see all books");
		
		if(genreInput.toLowerCase().equals(FantasyBook.genreName))
			printBooks(bookAdvisor.getBookOfGenre(genreInput, FantasyBook.class));
		else if(genreInput.toLowerCase().equals(NonFictionBook.genreName))
			printBooks(bookAdvisor.getBookOfGenre(genreInput, NonFictionBook.class));
		else if(genreInput.toLowerCase().equals(MysteryBook.genreName))
			printBooks(bookAdvisor.getBookOfGenre(genreInput, MysteryBook.class));
		else if(genreInput.toLowerCase().equals("all"))
			printBooks(bookAdvisor.getBooks());
		else{
			System.out.println("Invalid genre");
			Logger logger = Logger.GetInstance();
			logger.LogError("PrintBooks", "Invalid genre " + genreInput);
		}
	}
	
	private static <T extends Book> void printBooks(ArrayList<T> books){
		for(Book book : books){
			String keywords = "";
			for(String keyword : book.getKeywords()){
				keywords += keyword + " ";
			}
			System.out.println("Title: " + book.getTitle() + " | "
					+ "Genre: " + book.getGenre() + " | "
					+ "Keywords: " + keywords);
		}
		
		Logger logger = Logger.GetInstance();
		logger.Log("PrintBooks", LogLevel.Information);
	}
	
	private static void promptAddBook(BookAdvisor bookAdvisor){
		UserInputHandler inputHandler = new UserInputHandler();
		
		String titleInput = inputHandler.GetUserInput("Enter the title of the book: ");
		String genreInput = inputHandler.GetUserInput("Enter the genre of the book: ");
		String keywordsInput = inputHandler.GetUserInput("Enter the keywords of the book: ");
		
		bookAdvisor.addBook(titleInput, genreInput, keywordsInput);
		
		Logger logger = Logger.GetInstance();
		logger.Log("AddBook", LogLevel.Information);
	}
	
	private static void promptMarkCompleted(BookAdvisor bookAdvisor){
		UserInputHandler inputHandler = new UserInputHandler();
		Logger logger = Logger.GetInstance();

		String titleInput = inputHandler.GetUserInput("Enter the title of the book you completed: ");
		String ratingInput = inputHandler.GetUserInput("Enter your rating out of 5: ");
		
		int rating = Integer.parseInt(ratingInput);
		if(rating < 1 || rating > 5){
			System.out.println("Your rating cannot be less than 1 or higher than 5");
			logger.LogError("MarkCompleted", "Invalid Rating Entered");
		}
		else {
			bookAdvisor.completeBook(titleInput, rating);
			
			// Recommend the next book
			RecommendationEngine recommendationEngine = new RecommendationEngine();
			Book nextBook = recommendationEngine.getRecommendation(bookAdvisor.getBooks());
			if(nextBook != null){
				// Save the recommendation to the database
				RecommendationDb db = new RecommendationDb();
				db.InsertRecommendation(nextBook);
				
				System.out.println("Your next recommended book is " + nextBook.getTitle());
			}
			
			logger.Log("MarkCompleted", "Recommended next book - " + nextBook, LogLevel.Information);
		}	
	}
	
	private static void promptViewLogs(){
		UserInputHandler inputHandler = new UserInputHandler();
		String selection = inputHandler.GetUserInput("Select what logs you want to see \n"
				+ "1. View All\n"
				+ "2. View Errors\n"
				+ "3. View Information\n");
		
		Logger logger = Logger.GetInstance();
		
		String numResults = inputHandler.GetUserInput("Enter how many results you would like to see.  Just press enter for all \n");
		if(numResults.equals("")){
			if(selection.equals("1")){
				logger.PrintLogs();
			} else if(selection.equals("2")) {
				logger.PrintErrorLogs();
			} else if(selection.equals("3")) {
				logger.PrintInfoLogs();
			}
		} else {
			if(selection.equals("1")){
				logger.PrintLogs(Long.parseLong(numResults));
			} else if(selection.equals("2")) {
				logger.PrintErrorLogs(Long.parseLong(numResults));
			} else if(selection.equals("3")) {
				logger.PrintInfoLogs(Long.parseLong(numResults));
			}
		}
		
	}

	private static void viewPastRecommendations(){
		RecommendationDb db = new RecommendationDb();
		db.SelectRecommendations();
	}
}
