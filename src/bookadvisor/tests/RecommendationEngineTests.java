package bookadvisor.tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

import bookadvisor.Book;
import bookadvisor.BookAdvisor;
import bookadvisor.MysteryBook;
import bookadvisor.RecommendationEngine;

public class RecommendationEngineTests {
	
	@Test
	public void testGetGenreWeight(){
		RecommendationEngine recommendationEngine = new RecommendationEngine();
		assertEquals((Double).1, recommendationEngine.getGenreWeight("fantasy", "non fiction"));
		assertEquals((Double).6, recommendationEngine.getGenreWeight("fantasy", "mystery"));
		assertEquals((Double).3, recommendationEngine.getGenreWeight("mystery", "non fiction"));
		assertEquals(new Double(1), recommendationEngine.getGenreWeight("mystery", "mystery"));
	}
}
