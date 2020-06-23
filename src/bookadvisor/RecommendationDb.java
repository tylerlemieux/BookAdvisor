package bookadvisor;

import java.sql.*;

public class RecommendationDb {
	private final String connectionString = "jdbc:sqlite:/Users/tylerlemieux/Documents/workspace/BookAdvisor/log.db";		

	public RecommendationDb(){
		
	}

	public void InsertRecommendation(Book book){		
		int bookId = this.InsertBook(book);
		if(bookId != -1){
			this.insertKeywords(book.getKeywords(), bookId);
			this.insertRecommendation(bookId);		
		}
	}
	
	private void insertKeywords(String[] keywords, int bookId){
		String insertKeywordsQuery = "INSERT INTO keywords (keyword, bookId) VALUES (?, ?)";
		try (Connection connection = DriverManager.getConnection(connectionString)) {

			PreparedStatement statement = connection.prepareStatement(insertKeywordsQuery);
			
			for(String keyword : keywords){
				statement.setString(1, keyword);
				statement.setInt(2, bookId);
				statement.executeUpdate();
			}
		} catch (Exception ex){
			System.out.println("Error connecting to database: " + ex.getMessage());
		}
	}
	
	private void insertRecommendation(int bookId){
		String insertRecommendationQuery = "INSERT INTO recommendation (bookId, recommendationDate) VALUES (?, ?)";
		try (Connection connection = DriverManager.getConnection(connectionString)) {

			PreparedStatement statement = connection.prepareStatement(insertRecommendationQuery);
			statement.setInt(1, bookId);
			statement.setString(2, java.time.LocalDate.now().toString());
			
			statement.executeUpdate();
			
		} catch (Exception ex){
			System.out.println("Error connecting to database: " + ex.getMessage());
		}
	}
	
	private int InsertBook(Book book){
		// intent: insert a book into a database
		// postcondition: return the id of the book
		String insertBookQuery = "INSERT INTO book (title, rating, genre) VALUES (?, ?, ?)";
		
		try (Connection connection = DriverManager.getConnection(connectionString)) {
			
			PreparedStatement statement = connection.prepareStatement(insertBookQuery);
			statement.setString(1, book.getTitle());
			statement.setInt(2, book.getRating());
			statement.setString(3, book.getGenre());			
			statement.executeUpdate();
						
			try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                return generatedKeys.getInt(1);
	            }
	            else {
	                throw new SQLException("Creating book failed, no ID obtained.");
	            }
	        }
		} catch (Exception ex){
			System.out.println("Error connecting to database: " + ex.getMessage());
			return -1;
		}
	}
	
	public void SelectRecommendations(){
		String query = "SELECT b.title, b.genre, r.recommendationDate "
				+ "FROM recommendation r "
				+ "INNER JOIN book b ON r.bookId = b.bookId "
				+ "ORDER BY r.recommendationDate DESC";
		try (Connection connection = DriverManager.getConnection(connectionString)) {
			Statement statement = connection.createStatement();
			ResultSet results = statement.executeQuery(query);
			
			System.out.println("Title\tGenre\tRecomended Date");
			while(results.next()){
				System.out.println(results.getString(1) + "\t" + results.getString(2) + "\t" + results.getString(3));
			}
		} catch (Exception ex){
			System.out.println("Error connecting to database: " + ex.getMessage());
		}
		
	}
	
}
