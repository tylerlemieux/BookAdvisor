package bookadvisor.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import bookadvisor.MysteryBook;

public class MysteryBookTests {
	@Test
	public void testGetTitle(){
		MysteryBook mysteryBook = new MysteryBook("mystery title", "some keywords");
		assertEquals("mystery title", mysteryBook.getTitle());
	}
	
	@Test
	public void testGetKeywords(){
		MysteryBook mysteryBook = new MysteryBook("mystery title", "some keywords");
		assertEquals(new String[]{"some", "keywords"}, mysteryBook.getKeywords());
	}
	
	@Test
	public void testGetRating(){
		MysteryBook mysteryBook = new MysteryBook("mystery title", "some keywords");
		assertEquals(-1, mysteryBook.getRating());
	}
	
	@Test 
	public void testSetRating(){
		MysteryBook mysteryBook = new MysteryBook("mystery title", "some keywords");
		mysteryBook.setRating(3);
		assertEquals(3, mysteryBook.getRating());

	}
	
	@Test
	public void testGetGenre(){
		MysteryBook mysteryBook = new MysteryBook("mystery title", "some keywords");
		assertEquals("mystery", mysteryBook.getGenre());
	}
}
