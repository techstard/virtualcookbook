package virtualgusteau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

/**
 * This class connects to the recipe database.
 * @author Anders & Tobbe
 * TIN171, 2008
 */
public class DB_connect {
        private Connection con = null;

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
	//  constructor class or something in the future.
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
            //System.out.println(query+wanted);
            try{
                ResultSet rset = connect(query+wanted);

                String output = "Recipies : ";
                while (rset.next()){
                    output = output + rset.getString(1) + "\n";
                }
                return output;
            } catch(Exception e) {
			System.err.println("Exception in searchRecipe: " + e.getMessage());
			System.err.println(e);
                        return null;
            } 
        }//End searchRecipe
        
        public void closeConnection(){
            try {
                    if(con != null)
                            con.close();
            } catch(SQLException e) {
                    System.out.println(e);
            }
        }
        
}//End DB_connect