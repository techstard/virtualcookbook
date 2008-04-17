import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * This class connects to the recipe database.
 * @author Anders & Tobbe
 * TIN171, 2008
 */
public class DB_connect {
	
	// Search variables that configures the query to the database
	private String wantedIngredients[];
	private String notWantedIngredients[];
	
	private String wantedCategory[];
	private String notWantedCategory[];
	
	private String warnings[];
	private String specificDish;
	
	// This main method should be changed to a 
	//  constructor class or something in the future.
	public static void main(String args[]) {
		Connection con = null;

		try {
			// Create the connection
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Notice the username and password, it's only configured for Anders... yet.
			con = DriverManager.getConnection("jdbc:mysql://localhost/recipe_db",
					"root", "vertrigo");
			
			// If this works, you are connected.
			if(!con.isClosed())
				System.out.println("Successfully connected to " +
					"MySQL server using TCP/IP...");
      
			// Now for the real deal.
			
			// Create statement
			Statement stmt = con.createStatement();
		
			// Enter your query.
			ResultSet rset = stmt.executeQuery ("SELECT * "+
														"FROM recipes ");
			// Iterate through the result.
			while (rset.next())
				System.out.println ("arg2: " + rset.getString(2));

		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.err.println(e);
		} finally {
			try {
				if(con != null)
					con.close();
			} catch(SQLException e) {
				System.out.println(e);
			}
		}
	}
}