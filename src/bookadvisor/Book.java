package bookadvisor;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.concurrent.Callable;

public abstract class Book implements IDataRetrievable  {
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
    
    public String concatKeywords(){
    	String keywords = "";
		for(String keyword : this.getKeywords()){
			keywords += keyword + " ";
		}
		
		return keywords;
    }
    
    public abstract double getGenreSimilarityWeight(Book book);

    @Override
	public HashMap<String, String> GetStringValues() {
    	HashMap<String, String> properties = new HashMap<String, String>();
    	properties.put("title", this.getTitle());
    	properties.put("genre", this.getGenre());
    	properties.put("keywords", this.concatKeywords().trim());
    	properties.put("rating", Integer.toString(this.getRating()));
    	return properties;
    }
}
