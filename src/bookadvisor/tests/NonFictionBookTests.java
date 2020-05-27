package bookadvisor.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import bookadvisor.NonFictionBook;

public class NonFictionBookTests {
	@Test
	public void testGetTitle(){
		NonFictionBook nonFictionBook = new NonFictionBook("non fiction title", "some keywords");
		assertEquals("non fiction title", nonFictionBook.getTitle());
	}
	
	@Test
	public void testGetKeywords(){
		NonFictionBook nonFictionBook = new NonFictionBook("non fiction title", "some keywords");
		assertEquals(new String[]{"some", "keywords"}, nonFictionBook.getKeywords());
	}
	
	@Test
	public void testGetRating(){
		NonFictionBook nonFictionBook = new NonFictionBook("non fiction title", "some keywords");
		assertEquals(-1, nonFictionBook.getRating());
	}
	
	@Test 
	public void testSetRating(){
		NonFictionBook nonFictionBook = new NonFictionBook("non fiction title", "some keywords");
		nonFictionBook.setRating(3);
		assertEquals(3, nonFictionBook.getRating());

	}
	
	@Test
	public void testGetGenre(){
		NonFictionBook nonFictionBook = new NonFictionBook("non fiction title", "some keywords");
		assertEquals("non fiction", nonFictionBook.getGenre());
	}
}
