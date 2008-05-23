package virtualgusteau;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.LinkedList;
import grammar.*;

/**
 * This class connects to the recipe database.
 * @author Anders & Tobbe
 * TIN171, 2008
 */
public class DB_connect {
	private Connection con = null;
        private LinkedList<Integer> recept = new LinkedList<Integer>();
        private LinkedList<Integer> categoryRecept = new LinkedList<Integer>();
        private LinkedList<Integer> possibleRecept = new LinkedList<Integer>();

        /**
        * Creates a connection to the database
        */
	public DB_connect() {
		try{
			// Create the connection
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			//connecting toMySQL server
			con = DriverManager.getConnection("jdbc:mysql://193.11.241.134:3306/recipe_db",
					"Gusteau", "gusteau");
		} catch (Exception e){}
	}
        
        
        /**
         * Querys the database
         * 
         * @param query The query for the database
         * @return The results of the query as a ResultSet
         * @since 2008-05-13
         */
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
        
        /**
         * Searches trough the wanted ingredients to find what possible recipes there is
         * 
         * @param iW An interator over strings
         * @return A string with the possible recipes
         * @since 2008-05-13
         */
        //TODO : ändra return
        public LinkedList searchRecipe(Iterator iW) {
        //public String searchRecipe(Iterator iW){
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
            recept.clear(); //töm recept för att söka om på nytt efter recept.
            if(!iW.hasNext()){
                return recept;
            }
            String query = "SELECT * FROM ";
            String wanted = "";
            int i = 0;
            while(iW.hasNext()){
                if(i>0){
                    wanted = wanted+" INNER JOIN ";
                }
                i++;
                //System.out.println("test");
                wanted = wanted + "(SELECT * FROM contains WHERE name = \'" + (String)iW.next() + "\') AS dt"+i;
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
                    //output = output + rset.getString(1) + "\n";
                }
                //return output;
                return recept;
            } catch(Exception e) {
			System.err.println("Exception in searchRecipe: " + e.getMessage());
			System.err.println(e);
                        return null;
            } 
        }//End searchRecipe
        
        /**
         * removeNotWantedRecipes
         * removes recipes that includes somthing that the user dont want
         * @param nwi the ingredient that the user dont want in the dish.
         * @since 2008-05-13
         */
        public void removeNotWantedRecipes(String nwi){
        /*SELECT rID FROM 
        (SELECT rID FROM contains WHERE name = 'cream') as rpt      this is the not wanted ingredient
        WHERE rpt.rID = 2 OR rpt.rID = 7                            this is the wanted recipes*/
            String query = "SELECT rID FROM (SELECT rID FROM contains WHERE name = '" + nwi + "') AS rpt ";
            query = query + "WHERE rpt.rID = ";
            for(int i = 0; i < possibleRecept.size();i++){
                query = query + possibleRecept.get(i);
                if(i != possibleRecept.size() -1){
                    query = query + " OR rpt.rID = ";
                }
            }
            System.out.println(query);
            System.out.println("recept innan while "+possibleRecept.size());
            try{
                ResultSet rset = connect(query);
                while(rset.next()){
                    if(possibleRecept.indexOf(rset.getInt(1)) >= 0){
                        possibleRecept.remove(possibleRecept.indexOf(rset.getInt(1)));
                    }
                }
                System.out.println("recept efter while "+possibleRecept.size());
            }
            catch(Exception e) {
                System.err.println("Exception in removeNotWantedRecipes: " + e.getMessage());
		System.err.println(e);
            }
        }
        
        /**
         * addCategoryRecipes
         * adds all recipes containing ingredients from a category
         * @param category the category
         * @since 2008-05-13
         */
        public void addCategoryRecipes(String category){        
            String query = "SELECT DISTINCT(rID) FROM contains WHERE name IN" +
                    " ( SELECT ingredient FROM is_categorized_by WHERE category = '"+category + "')";
            //System.out.println("recept innan lägger till category "+categoryRecept.size());
            categoryRecept.clear();
            try{
                ResultSet rset = connect(query);
                while(rset.next()){
                        categoryRecept.add(rset.getInt(1));
                }
            //System.out.println("recept efter att ha lagt till category "+categoryRecept.size());    
            }
            catch(Exception e) {
                System.err.println("Exception in addCategoryRecipes: " + e.getMessage());
		System.err.println(e);
            }
        }
        
        /**
         * removeCategoryRecipes
         * removes all recipes that has something from that category
         * @param category the category
         * @since 2008-05-13
         */
        public void removeCategoryRecipes(String category){        
            String query = "SELECT DISTINCT(rID) FROM contains WHERE name IN" +
                    " ( SELECT ingredient FROM is_categorized_by WHERE category = '"+category + "')";
            //System.out.println("removeCategoryRecipes innan borttagning "+categoryRecept.size());
            try{
                ResultSet rset = connect(query);
                while(rset.next()){
                    if(possibleRecept.indexOf(rset.getInt(1)) >= 0){
                        possibleRecept.remove(possibleRecept.indexOf(rset.getInt(1)));
                    }
                }
            //System.out.println("removeCategoryRecipes efter borttagning "+possibleRecept.size());    
            }
            catch(Exception e) {
                System.err.println("Exception in removeCategoryRecipes: " + e.getMessage());
		System.err.println(e);
            }
        }
        
        /**
         * possibleRecipes
         * returns the possible recipes the user wants.
         * @param 
         * @return LinkedList<Noun> The list of all recipes
         * @since 2008-05-21
         */
        public LinkedList<Integer> possibleRecipes(KnowledgeBase kb){//borde ta in knowledgebasen
            possibleRecept.clear();
            Iterator wantIT = kb.iWCIterator(); 
            while(wantIT.hasNext()){
                Object tmp = wantIT.next();
                addCategoryRecipes((String)tmp);
            }
            
            Iterator iW = kb.iWIterator();
            searchRecipe(iW);
            
            if(categoryRecept.size() != 0 && recept.size() == 0){
                possibleRecept = categoryRecept;
            } else if(categoryRecept.size() == 0 && recept.size() != 0){
                possibleRecept = recept;
            } else if(categoryRecept.size() == 0 && recept.size() == 0){
                //gör inget
            }else {
                if(categoryRecept.size() >= recept.size()){
                    for(int i = 0; i < recept.size();i++){
                        for(int k = 0; k < categoryRecept.size();k++){
                            if(recept.get(i) == categoryRecept.get(k)){
                                possibleRecept.add(recept.get(i));
                            }
                        }
                    }
                } else {
                    for(int i = 0; i < categoryRecept.size();i++){
                        for(int k = 0; k < recept.size();k++){
                            if(categoryRecept.get(i) == recept.get(k)){
                                possibleRecept.add(categoryRecept.get(i));
                            }
                        }
                    }
                }
            }
            
            Iterator notWantC = kb.iNWCIterator(); 
            while(notWantC.hasNext()){
                Object tmp = notWantC.next();
                removeCategoryRecipes((String)tmp);
            }
            
            Iterator notWantI = kb.iNWIterator(); 
            while(notWantI.hasNext()){
                Object tmp = notWantI.next();
                removeNotWantedRecipes((String)tmp);
            }
            
            for(int i = 0; i < possibleRecept.size(); i++){
                System.out.println(possibleRecept.get(i));
            }
            
            return possibleRecept;
            
        }
        
        /**
         * isCategory
         * checks if word is a category
         * @param word to check
         * @return Boolean true if it is a category, false otherwise
         * @since 2008-05-13
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
         * getIngredients
         * gets all the ingredients for a recipe
         * @param rID The recipe to check
         * @return String with all ingredients in a recipe
         * @since 2008-05-13
         */
        public String[] getIngredients(int rID) {
            try {
                String q = "SELECT name FROM contains WHERE rID = '" + rID + "'";
                LinkedList<String> ing = new LinkedList<String>();
                ResultSet rSet = connect(q);
                while(rSet.next()) {
                    ing.add(rSet.getString(1));
                }
                Object[] a = ing.toArray();
                String[] bla = new String[ing.size()];
                for(int i = 0; i < ing.size(); i++) {
                    bla[i] = (String)a[i];
                }
                return bla;
            } catch(Exception e) {
                System.err.println("Exception in printRecipe(): " + e.getMessage());
                System.err.println(e);
                return null;
            }
        }
        /**
         * printRecipe
         * prints a string with a whole recipe based on the rID.
         * @param rID The chosen recipe to print
         * @return String with all information about the recipe 'rID'
         * @since 2008-05-13
         */
        public String printRecipe(int rID, int nr){
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
        		int portions = nr;
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
        		//System.out.println(output);
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
         * @param ing ingredient to check
         * @return boolean true if it is an ingredient, false otherwise
         * @since 2008-05-13
         */
        public boolean isDish(String dish){
                // Make query in DB and search for the string
                try{
                        // variables
                    //SELECT COUNT(name) FROM dish WHERE name = "pie"
                String query = "SELECT COUNT(name) FROM dish WHERE name = '" + dish + "'";
                        String output;
                        // connect with the query
                        ResultSet rset = connect(query);
                        if (rset.getInt(1) == 0){
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
        
        /**
         * findRecipeName
         * Gives the name of the chosen rID.
         * @param rID The chosen recipe to print
         * @return String with titlename of recipe 'rID'
         * @since 2008-05-13
         */
        public String findRecipeName(int rID){
        	// Make query in DB for 'rID' for the name
        	try{
            	String query = "SELECT * FROM recipes WHERE rID = '" + rID + "'";
            	ResultSet rset = connect(query);
        		rset.next();
        		return rset.getString(2);
        	} 
        	catch(Exception e) {
        		System.err.println("Exception in getRecipeName(): " + e.getMessage());
        		System.err.println(e);
        		return null;
            } 
        }
        /**
         * isAnIngredient
         * Checks if a given string is an existing ingredient in the recipe_db
         * @param ing ingredient to check
         * @return boolean true if it is an ingredient, false otherwise
         * @since 2008-05-13
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
        /**
         * findUniqueIngredients
         * Find and returns the ingredients that only occurs once
         * @param recipes the possible recipes
         * @return LinkedList<String> with possible ingredients
         * @since 2008-05-22
         */
        public LinkedList<String> findUniqueIngredients(KnowledgeBase kb){
        	// Make query in DB and search for the string
//                SELECT name FROM 
//                (SELECT name, COUNT(name) AS cnt FROM contains WHERE (rID = 7 OR rID = 3) 
//                GROUP BY name) AS t
//                WHERE t.cnt = 1;
        	LinkedList<String> ing = new LinkedList<String>();
                LinkedList<Integer> recipes = possibleRecipes(kb);
                String q = "SELECT name FROM (SELECT name, COUNT(name) AS cnt FROM contains WHERE (";
                for(int i = 0; i < recipes.size();i++){
                    q += "rID = " + recipes.get(i);
                    if(i < recipes.size() -1 ){
                        q += " OR ";
                    }
                }
                q += ") GROUP BY name) AS t WHERE t.cnt = 1 AND name NOT IN (SELECT ingredient FROM is_categorized_by WHERE category = \"spice\");";
                System.out.println(q);
                try{
                    // variables

                    // connect with the query
                    ResultSet rset = connect(q);
                    while(rset.next()){
                        System.out.println(rset.getString(1));
                        ing.add(rset.getString(1));
                    }
                    return ing;	
        	} 
        	catch(Exception e) {
        		System.err.println("Exception in isAnIngredient(): " + e.getMessage());
        		System.err.println(e);
        		return ing;
                }
        }
        
         /**
         * closeConnection
         * closes the databaseconnection
         * @since 2008-05-13
         */
        public void closeConnection(){
            try {
                    if(con != null)
                            con.close();
            } catch(SQLException e) {
                    System.out.println(e);
            }
        }
        
}//End DB_connect