package virtualgusteau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.LinkedList;

/**
 * This class connects to the recipe database.
 * @author Anders & Tobbe
 * TIN171, 2008
 */
public class DB_connect {
	private Connection con = null;
        private LinkedList recept = new LinkedList();

	public DB_connect() {
		try{
			// Create the connection
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//connecting toMySQL server
			con = DriverManager.getConnection("jdbc:mysql://193.11.241.134:3306/recipe_db",
					"Gusteau", "gusteau");
		} catch (Exception e){}
	}
	// This main method should be changed to a 
	// constructor class or something in the future.
	public ResultSet connect(String query) {
		try {
			// Create statement
			Statement stmt = con.createStatement();
		
			// Enter your query.
                        //String query = searchRecipe(kb);
			
			ResultSet rset = stmt.executeQuery(query);
                        
			// Iterate through the result.
                        /*String output = "Recipies : ";
			while (rset.next()){
                            output = output + rset.getString(1) + "\n";
                        }*/
                        return rset;

		} catch(Exception e) {
			System.err.println("Exception in connect : " + e.getMessage());
			System.err.println(e);
                        return null;
		} 
	}//End connect
        
        public String searchRecipe(KnowledgeBase kb){
            /*
             * För flera ingredienser fyll ut med
             * INNER JOIN (SELECT * FROM contains WHERE name = 'XXXXXXX') AS dtY
             * och
             * dt1.rID = dtY.rID
             * 
            SELECT *
            FROM (SELECT * FROM contains WHERE name = 'minced meat') AS dt1
            INNER JOIN (SELECT * FROM contains WHERE name = 'onion') AS dt2
            INNER JOIN (SELECT * FROM contains WHERE name = 'potato') AS dt3
            INNER JOIN (SELECT * FROM contains WHERE name = 'banana') AS dt4
            ON dt1.rID = dt2.rID AND dt1.rID = dt3.rID AND dt1.rID = dt4.rID
             */
            Iterator iW = kb.iWIterator();
            String query = "SELECT * FROM ";
            String wanted = "";
            int i = 0;
            while(iW.hasNext()){
                if(i>0){
                    wanted = wanted+" INNER JOIN ";
                }
                i++;
                System.out.println("test");
                wanted = wanted + "(SELECT * FROM contains WHERE name = \'" + iW.next() + "\') AS dt"+i;
            }
            for(int k=0;k<i-1;k++){
                if(k==0){
                    wanted = wanted + " ON";
                }
                wanted = wanted + " dt"+1+".rID = "+"dt"+(k+2)+".rID";
                if(k<(i-2)){
                    wanted = wanted + " AND";
                }
            }
            System.out.println(query+wanted);
            try{
                ResultSet rset = connect(query+wanted);

                String output = "Recipies : ";
                while (rset.next()){
                    recept.add(rset.getInt(1));
                    output = output + rset.getString(1) + "\n";
                }
                return output;
            } catch(Exception e) {
			System.err.println("Exception in searchRecipe: " + e.getMessage());
			System.err.println(e);
                        return null;
            } 
        }//End searchRecipe
        
        /**
         * isCategory
         * checks if word is a category
         * @param word to check
         * @return Boolean true if it is a category, false otherwise
         */
        public boolean isCategory(String word){
            String q = "SELECT count(*) FROM category WHERE name = '" + word + "';";
            try{
                ResultSet rset = connect(q);
                rset.next();
                if(rset.getInt(1) >= 1){
                    return true;
                }
                return false;
                }
            catch(Exception e) {
                System.err.println("Exception in searchRecipe: " + e.getMessage());
		System.err.println(e);
                return false;
            }
        }
        
        /**
         * printRecipe
         * prints a string with a whole recipe based on the rID.
         * @param rID The chosen recipe to print
         * @return String with all information about the recipe 'rID'
         */
        public String printRecipe(int rID){
        	// Make query in DB for 'rID' in fields: recipe, contains
        	// to obtain all visual information about the chosen recipe.
        	try{
        		// variables
            	String query = "SELECT * FROM recipes WHERE rID = '" + rID + "'";
            	System.out.println(query);
            	String part1of3 = "";
        		String part2of3 = "";
        		String part3of3 = "";
        		String output;
        		int portions = 5;
            	// first query, get recipe information.
        		ResultSet rset = connect(query);
        		rset.next();
        		// #1 -> Title
        		part1of3 += "- " + rset.getString(2) + " - \n\n";
        		// #2 -> Description
        		part1of3 += rset.getString(3) + "\n\n";
        		// #3 -> Time and difficulty
        		part1of3 += "Time: " + rset.getString(5) + " minutes\nDifficulty from 1-5: " + rset.getString(6) + "\nFor " + portions + " people.\n\n";
        		// #5 -> Plan
        		part3of3 += rset.getString(4) + "\n\n";
        		// #4 -> List of ingredients
        		// last line needs another query from the field 'contains'
            	query = "SELECT * FROM contains WHERE rID = '" + rID + "'";
            	System.out.println(query);
            	rset = connect(query);
            	while(rset.next()){
            		// Check if its several of the ingredients, then go plural form.
            		String pluralEnd = "";
            		String amountOf = "";
            		if (rset.getDouble(4) != 1)
            			pluralEnd = "s";
            		// Take away .0 for example: 500.0 g -> 500 g
            		if (portions*rset.getDouble(4) % 1 == 0)
            			amountOf = ""+(int)(portions*rset.getDouble(4));
            		else
            			amountOf = ""+portions*rset.getDouble(4);
            		// Watch out for rows with no measures
            		if (rset.getString(3).compareTo("") != 0)
            			part2of3 += amountOf + " " + rset.getString(3) + " " + rset.getString(2) + pluralEnd + "\n";
            		else
            			part2of3 += amountOf + " " + rset.getString(2) + pluralEnd +"\n";
            	}
            	// Unite all the parts
            	output = "\n" + part1of3 + part2of3 + "\n" + part3of3;
            	// test the output
        		System.out.println(output);
        		return output;
        	} 
        	catch(Exception e) {
        		System.err.println("Exception in printRecipe(): " + e.getMessage());
        		System.err.println(e);
        		return null;
            } 
        }
        /**
         * isAnIngredient
         * Checks if a given string is an existing ingredient in the recipe_db
         * @param ing
         * @return boolean
         */
        public boolean isAnIngredient(String ing){
        	// Make query in DB and search for the string
        	try{
        		// variables
            	String query = "SELECT * FROM ingredients WHERE name = '" + ing + "'";
        		String output;
        		// connect with the query
        		ResultSet rset = connect(query);
        		if (rset.first() == false){
        			//System.out.println("It's NOT an ingredient!");
        			return false;
        		} else {
        			//System.out.println("It IS an ingredient!");
        			return true;
        		}
        	} 
        	catch(Exception e) {
        		System.err.println("Exception in isAnIngredient(): " + e.getMessage());
        		System.err.println(e);
        		return false;
            } 
        }
        
        public void closeConnection(){
            try {
                    if(con != null)
                            con.close();
            } catch(SQLException e) {
                    System.out.println(e);
            }
        }
        
}//End DB_connect