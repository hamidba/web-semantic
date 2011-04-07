package Model;

import indexation.Parser;

import java.sql.SQLException;
import java.util.Vector;

import classes.Mot;


public class test {

	/**
	 * @param args
	 * @throws SQLException 
	 */
	public static void main(String[] args) throws SQLException 
	{
		// TODO Auto-generated method stub	
		
		Parser p = new Parser();
		Index ind = new Index();
	    p.parseDoc();
	    
	    Vector<Mot> test = p.get_vectorMot();
	    
	    ind.createIndex(test);

	}

}
