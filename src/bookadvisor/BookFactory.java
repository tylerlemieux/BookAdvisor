package bookadvisor;

import java.util.HashMap;

public class BookFactory implements IFactory<Book> {
	public Book Create(HashMap<String, String> properties){
		// intent: use polymorphism to create the correct instance of a book object
		// postcondition: book is created using the correct type
		// postcondition: if the genre is not defined, throw an unsupportedgenreexception
		Book bookToAdd = null;
		String title = properties.get("title");
		String keywords = properties.get("keywords");
		String genre = properties.get("genre");
		
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
        		System.out.println(genre + " is not a supported genre");
        }
        return bookToAdd;
	}
}
