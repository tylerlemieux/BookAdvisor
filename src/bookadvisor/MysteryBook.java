package bookadvisor;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class MysteryBook extends Book {
	public static final String genreName = "mystery";
	
	public MysteryBook(String title, String keywords) {
		super(title, keywords);
		// TODO Auto-generated constructor stub
	}

	@Override
	public String getGenre() {
		return genreName;
	}

	@Override
	public double getGenreSimilarityWeight(Book book) {
		// TODO implement this later, this will give a weight for the similarity to other genres based on the book
		return 0;
	}	
}
