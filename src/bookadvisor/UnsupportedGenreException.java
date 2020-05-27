package bookadvisor;

public class UnsupportedGenreException extends Exception {
	private String genreName;
	
	public UnsupportedGenreException(String genreName){
		super("The genre " + genreName + " is either invalid or is unsupported by Book Advisor");
		this.genreName = genreName;
	}
	
	public String getUnsupportedGenreName(){
		return this.genreName;
	}
	
	
}
