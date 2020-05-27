package bookadvisor.tests;

import static org.junit.Assert.*;

import org.junit.Test;

import bookadvisor.FantasyBook;

public class FantasyBookTests {
	
	@Test
	public void testGetTitle(){
		FantasyBook fantasyBook = new FantasyBook("fantasy title", "some keywords");
		assertEquals("fantasy title", fantasyBook.getTitle());
	}
	
	@Test
	public void testGetKeywords(){
		FantasyBook fantasyBook = new FantasyBook("fantasy title", "some keywords");
		assertEquals(new String[]{"some", "keywords"}, fantasyBook.getKeywords());
	}
	
	@Test
	public void testGetRating(){
		FantasyBook fantasyBook = new FantasyBook("fantasy title", "some keywords");
		assertEquals(-1, fantasyBook.getRating());
	}
	
	@Test 
	public void testSetRating(){
		FantasyBook fantasyBook = new FantasyBook("fantasy title", "some keywords");
		fantasyBook.setRating(3);
		assertEquals(3, fantasyBook.getRating());

	}
	
	@Test
	public void testGetGenre(){
		FantasyBook fantasyBook = new FantasyBook("fantasy title", "some keywords");
		assertEquals("fantasy", fantasyBook.getGenre());
	}
	
}
