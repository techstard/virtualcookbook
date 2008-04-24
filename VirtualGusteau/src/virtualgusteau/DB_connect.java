package virtualgusteau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

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
        private KnowledgeBase kb = new KnowledgeBase();
	        
	// This main method should be changed to a 
	//  constructor class or something in the future.
	public String connect(LinkedList<Noun> ingredientsWanted) {
		Connection con = null;

		try {
			// Create the connection
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			// Notice the username and password, it's only configured for Anders... yet.
			//con = DriverManager.getConnection("jdbc:mysql://localhost/recipe_db",
			//		"root", "vertrigo");
                        //Testing another MySQL server
			con = DriverManager.getConnection("jdbc:mysql://193.11.241.134:3306/recipe_db",
					"Gusteau", "gusteau");
                        
			// If this works, you are connected.
			/*if(!con.isClosed()) {
				System.out.println("Successfully connected to " +
					"MySQL server using TCP/IP...");
                            return "connection succsessfull";
                        } else {
                            return "connection failed";
                        }*/
      
			// Now for the real deal.
			
			// Create statement
			Statement stmt = con.createStatement();
		
			// Enter your query.
                        String query = "SELECT r.name FROM recipes r, contains c WHERE r.rID = c.rID AND "
                                + "c.name = \"" + ingredientsWanted.getFirst().toString() + "\"";
                        System.out.println(query);
			ResultSet rset = stmt.executeQuery(query);
                        
			// Iterate through the result.
                        String output = "Recipies : ";
			while (rset.next()){
                            output = output + rset.getString(1) + "\n";
                        }
                        return output;
                        //rset.next();
                        //return rset.getString(2);
			//System.out.println ("arg2: " + rset.getString(1));

		} catch(Exception e) {
			System.err.println("Exception: " + e.getMessage());
			System.err.println(e);
                        return "Exception : " + e;
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