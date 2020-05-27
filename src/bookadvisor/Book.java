package bookadvisor;

import java.util.Dictionary;

public abstract class Book {
    private String title;
    private String[] keywords;
    private Integer rating;
    		
    public Book(String title, String keywords){
        this.title = title;
        this.keywords = keywords.split(" ");
        this.rating = -1;
    }

    public String getTitle(){
        return this.title;
    }

    public abstract String getGenre();

    public String[] getKeywords(){
        return this.keywords;
    }

    public int getRating(){
        return this.rating;
    }

    public void setRating(int rating){
        this.rating = rating;
    }
    
    public boolean isCompleted(){
    	return this.rating != -1;
    }
    
    public abstract double getGenreSimilarityWeight(Book book);
}
