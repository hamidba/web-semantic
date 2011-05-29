package search;

import classes.Connector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.Vector;

import com.mysql.jdbc.Statement;

import classes.Mot;

import Model.Index;

/**
 * Classe permettant de chercher dans l'index ˆ partir d'une requete
 * @author hamid
 *
 */
public class Rechercher 
{
	String req;
	String[] mots;
	Index index;
	
	Vector<String> vocabulaire;
	Vector<Vector<Double>> vecteursParagraphe;
	Vector<String> distinctPara;
	
	public Rechercher() throws SQLException, IOException
	{
		index = new Index();
		
		BufferedReader br = new BufferedReader(
                new InputStreamReader(System.in));
		String req;
		
		System.out.println("Lancement du moteur, Veuillez patientez svp ...\n");
		espaceVectoriel();
		
		Vector<Double> q ;
		Double[] innerProducts;
		Vector<Double> para;
		
		System.out.print("Entrer votre requete ou 'q' pour quitter : ");
		req = br.readLine();
		
		while(!req.equals("q"))
		{
			innerProducts = new Double[vecteursParagraphe.size()];
			para = new Vector<Double>();
			
			//Construction vecteur de la requete
			q = requeteToVector(req);
			
			for(int i = 0; i < vecteursParagraphe.size(); i++)
			{
				para = (Vector<Double>) vecteursParagraphe.get(i);
				
				innerProducts[i] = innerProduct(q, para );
			}
			
			
			Vector result = bestResults(innerProducts); 
			printResult(result);
			
			System.out.print("Entrer votre requete ou 'q' pour quitter : ");
			req = br.readLine();
			
		}
	
	}
	
	
	
	public void espaceVectoriel() throws SQLException
	{
		vecteursParagraphe = new Vector();
		
		//Construction du vecteur de toute la dimension (ie tout le vocabulaire)
		vocabulaire = index.getVocabulaire();
		Vector<Double> para = new Vector<Double>();
		
		
		distinctPara = index.getDistinctParagraphe();
		
		
		for(int i=0; i < distinctPara.size(); i++)
		{
			para = index.getVectorParagraphe((String) distinctPara.get(i), vocabulaire);
			vecteursParagraphe.add(i,para);
			
		}
		
	}
	
	
	public Vector<Double> requeteToVector(String req)
	{
		String[] requete = req.split(" ");
		Vector<Double> qVector = new Vector<Double>();
		qVector.setSize(vocabulaire.size());
		
		for (int i = 0; i < requete.length; i++)
		{
			if(vocabulaire.contains(requete[i]))
			{
				qVector.insertElementAt((double) 1, vocabulaire.indexOf(requete[i]));
			}
				
		}
		
		
		return qVector;
		
	}
	
	/**
	 * 
	 * @param q
	 * @param p
	 * @return
	 */
	public double innerProduct(Vector<Double> q, Vector<Double> p)
	{
		int size = vocabulaire.size();
		Double innerProduct = 0.0;
		
		
		for(int i = 0; i < size; i++)
		{
			if((q.get(i) != null) && (p.get(i) != null))
			{
				innerProduct+=p.get(i)*q.get(i);
			}
		}
		
		return innerProduct;
		
	}
	
	public Vector bestResults(Double[] innerProducts)
	{
		Vector result = new Vector();
		
		for(int i = 0; i < innerProducts.length; i++)
		{
			if(innerProducts[i] != 0.0)
			{
				
				result.add(distinctPara.get(i));
			
			}
			
		}
		
		return result;
	}
		
		
	
	public void printResult(Vector result)
	{
		
		for(int i = 0; i < result.size(); i++)
		{
			
				
					System.out.println(result.get(i)) ;
				
			
		}
		
		
	}
	
	
	
	

}
