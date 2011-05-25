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
		
		long debut = System.currentTimeMillis();
		System.out.println("Start : "+debut);
		
		Parser p = new Parser();
		Index ind = new Index();
	    
	    Vector<Mot> listeMots = p.get_vectorMot();
	    System.out.println(listeMots.size());
	    
	    ind.createIndex(listeMots);
	    
	    long fin = System.currentTimeMillis();
	    System.out.println("End : "+fin);
	    System.out.println("Durée : "+(fin-debut)/1000);
	}
}
