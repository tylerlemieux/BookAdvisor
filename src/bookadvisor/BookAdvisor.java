package bookadvisor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class BookAdvisor {
	private final String pathToSaveFile = "data.txt";
	
	private final int titleIndex = 0;
	private final int genreIndex = 1;
	private final int keywordsIndex = 2;
	private final int ratingIndex = 3;
	
	private ArrayList<Book> books;
	
	public BookAdvisor(){
		loadBooks();
	}
	
	public void addBook(String title, String genre, String keywords){
		// intent: add a book to the user's collection
		// postcondition: the book added will be in the book array and saved to the data file
		// postcondition: if the genre is unsupported show a message to the user about it
		Book bookToAdd;
		try {
			bookToAdd = instantiateBook(title, keywords, genre);
			books.add(bookToAdd);
			saveBookData();	
		} catch (UnsupportedGenreException ex) {
			System.out.println("The genre " + ex.getUnsupportedGenreName() + " is not supported");
		}
	}
	
	public void completeBook(String title, int rating){
		// intent: rate a book so it shows as complete
		// postcondition: the book specified will have a rating set
		for(Book book : this.books){
			if(book.getTitle().equals(title)){
				book.setRating(rating);
			}
		}
	}
	
	public void recommendNext(){
		// TODO
	}
	
	public ArrayList<Book> getBooks(){
		return this.books;
	}
	
	public <T extends Book> ArrayList<T> getBookOfGenre(String genreName, Class<T> classparam){
		ArrayList<T> booksInGenre = new ArrayList<T>();
		// intent: downcast books to books of a specific genre
		// postcondition: all of the books in the genre that have been added get returned here		
		for(Book book : this.books){
			if(genreName == book.getGenre()){
				booksInGenre.add((T) book);
			}
		}
		
		return booksInGenre;
	}
	
	private void saveBookData() {
		// intent: save book data to a file
		// postcondition: data will be saved with a book on each line with each element separated by |
		FileWriter fileWriter = null;
		try{
			fileWriter = new FileWriter(this.pathToSaveFile);
			
			for(Book book : books){
				fileWriter.write(book.getTitle() + "|" 
								+ book.getGenre() + "|" 
								+ String.join(" ", book.getKeywords()) + "|"  
								+ book.getRating() + "\n");
			}
			
		} catch (Exception ex){
			System.out.println(this.pathToSaveFile + " does not exist");
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private Book instantiateBook(String title, String keywords, String genre) throws UnsupportedGenreException{
		// intent: use polymorphism to create the correct instance of a book object
		// postcondition: book is created using the correct type
		// postcondition: if the genre is not defined, throw an unsupportedgenreexception
		Book bookToAdd;
        switch(genre){
        	case FantasyBook.genreName: 
        		bookToAdd = new FantasyBook(title, keywords);
        		break;
        	case MysteryBook.genreName:
        		bookToAdd = new MysteryBook(title, keywords);
        		break;
        	case NonFictionBook.genreName:
        		bookToAdd = new NonFictionBook(title, keywords);
        		break;
        	default: 
        		throw new UnsupportedGenreException(genre);
        }
        return bookToAdd;
	}
	
	private void loadBooks(){
		// intent: load the books from a file and create the books using polymorphism
		// postcondition: the books ArrayList should be populated with data from the file
		// postcondition: if there is a row with an unsupported genre tell the user the file is corrupted
		this.books = new ArrayList<Book>();
		
		try{
			Scanner bookDataFile = new Scanner(new File(this.pathToSaveFile));
			
			try{
				while(bookDataFile.hasNext()){
		            String inputLine = bookDataFile.nextLine();
		            String[] inputs = inputLine.trim().split("\\|");
		            
		            
		            Book bookToAdd;
		            bookToAdd = instantiateBook(inputs[titleIndex], inputs[keywordsIndex], inputs[genreIndex]);
		            bookToAdd.setRating(Integer.parseInt(inputs[ratingIndex]));
		            this.books.add(bookToAdd);	
				}
			} catch (UnsupportedGenreException ex){
				System.out.println(ex.getUnsupportedGenreName() + " is not a supported genre.  Your save file must be corrupted.");
			} finally {
	            bookDataFile.close();
			}
		} catch (IOException ex) {
			System.out.println(this.pathToSaveFile + " does not exist");
		
		}
	}
	
}
