package bookadvisor.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import bookadvisor.Book;
import bookadvisor.BookFactory;
import bookadvisor.FantasyBook;
import bookadvisor.MysteryBook;
import bookadvisor.NonFictionBook;
import bookadvisor.SaveFileHandler;

public class SaveFileHandlerTests {
	
	@Test
	public void testSaveAndGetData(){		
		// Create some fake books to save
		MysteryBook mysteryBook1 = new MysteryBook("Who did it", "thrilling crime");
		FantasyBook fantasyBook1 = new FantasyBook("Harry Potter", "magic hogwarts wizard");
		NonFictionBook nonFictionBook1 = new NonFictionBook("Non fiction test", "self-help test2");

		ArrayList<Book> books = new ArrayList<Book>();
		books.add(mysteryBook1);
		books.add(fantasyBook1);
		books.add(nonFictionBook1);
		
		SaveFileHandler<Book> saveFileHandler = new SaveFileHandler<Book>("unittest.txt");
		saveFileHandler.SaveData(books);
		
		//Run get and make sure it created the objects correctly
		BookFactory bookFactory = new BookFactory();
		ArrayList<Book> loadedBooks = saveFileHandler.GetData(bookFactory);
		
		MysteryBook loadedMysteryBook = null;
		FantasyBook loadedFantasyBook = null;
		NonFictionBook loadedNonFictionBook = null;
		
		for(int i=0; i<loadedBooks.size();i++){
			if(loadedBooks.get(i).getTitle().equals("Who did it"))
				loadedMysteryBook = (MysteryBook)loadedBooks.get(i);
			else if(loadedBooks.get(i).getTitle().equals("Harry Potter"))
				loadedFantasyBook = (FantasyBook)loadedBooks.get(i);
			else if(loadedBooks.get(i).getTitle().equals("Non fiction test"))
				loadedNonFictionBook = (NonFictionBook)loadedBooks.get(i);
		}
		
		// Assert the properties of the books
		
		// Mystery Tests
		assertNotNull(loadedMysteryBook);
		assertEquals("thrilling crime ", loadedMysteryBook.concatKeywords());
		
		// Fantasy Tests
		assertNotNull(loadedFantasyBook);
		assertEquals("magic hogwarts wizard ", loadedFantasyBook.concatKeywords());
		
		// NonFiction Tests
		assertNotNull(loadedNonFictionBook);
		assertEquals("self-help test2 ", loadedNonFictionBook.concatKeywords());
		
	}
	
	
	
}
