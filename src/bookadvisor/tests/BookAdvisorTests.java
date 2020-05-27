package bookadvisor.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import bookadvisor.Book;
import bookadvisor.BookAdvisor;
import bookadvisor.MysteryBook;
import bookadvisor.UnsupportedGenreException;

public class BookAdvisorTests {
	
	@Test
	public void testAddBook(){
		BookAdvisor bookAdvisor = new BookAdvisor();
		bookAdvisor.addBook("sample title", "fantasy", "violent dark");
		
		ArrayList<Book> books = bookAdvisor.getBooks();
		assertEquals("sample title" ,books.get(books.size() - 1).getTitle());
		
	}
	
	@Test 
	public void testCompleteBook(){
		BookAdvisor bookAdvisor = new BookAdvisor();
		bookAdvisor.addBook("completion", "mystery", "thrilling");
		bookAdvisor.completeBook("completion", 2);
		ArrayList<Book> books = bookAdvisor.getBooks();
		assertEquals(2, books.get(books.size() - 1).getRating());

	}
	
	@Test 
	public void testGetBooks(){
		BookAdvisor bookAdvisor = new BookAdvisor();
		ArrayList<Book> books = bookAdvisor.getBooks();
		
		assertTrue(books.size() > 0);
	}
	
	@Test
	public void testGetBooksOfGenre(){
		BookAdvisor bookAdvisor = new BookAdvisor();
		ArrayList<MysteryBook> fantasyBooks = bookAdvisor.getBookOfGenre("mystery", MysteryBook.class);
		
		assertTrue(fantasyBooks.size() > 0);
	}
}
