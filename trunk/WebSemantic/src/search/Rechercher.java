package search;

import classes.Connector;
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
	
	public Rechercher(String requete) throws SQLException
	{
		index = new Index();
		req = requete;
		//mots = req.split(" ");
		
		//Pour chaque mot on calcule son tf, son idf puis son tf-idf
		//HashMap tf = getTF(req);
		//HashMap tfIdf = getTfIdf(tf, getIDF(req));
		
		//Construction de l'espace vectoriel
		
		espaceVectoriel();
		
		//Construction vecteur de la requete
		Vector<Double> q = requeteToVector(req);
		
		
		Double[] innerProducts = new Double[vecteursParagraphe.size()];
		Vector<Double> para = new Vector<Double>();
		
		for(int i = 0; i < vecteursParagraphe.size(); i++)
		{
			para = (Vector<Double>) vecteursParagraphe.get(i);
			
			innerProducts[i] = cosinus(q, para );
		}
		
		
		//Double[] result = bestResult(innerProducts); 
		printResult(innerProducts);
	
		
		
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
/*
		for(int i=0; i < para.size(); i++)
		{
			if(para.get(i) != null)
			{
				System.out.println(para.get(i));
			}
		}
*/
		
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
	 * Calcul de l'angle entre un paragraphe et une requete
	 * @param q
	 * @param p
	 * @return
	 */
	public double cosinus(Vector<Double> q, Vector<Double> p)
	{
		int size = vocabulaire.size();
		Double innerProduct = 0.0;
		
		
		for(int i = 0; i < size; i++)
		{
			if((q.get(i) != null) && (p.get(i) != null))
			{
				System.out.println(p.get(i));
				innerProduct+=p.get(i)*q.get(i);
			}
		}
		
		return innerProduct;
		
	}
	
	public Double[] bestResult(Double[] innerProducts)
	{
		Double[] result = new Double[innerProducts.length];
		
		for(int i = 0; i < innerProducts.length; i++)
		{
			if(innerProducts[i] != 0)
			{
				
				result[i] = innerProducts[i];
			}
			
		}
		
		//Arrays.sort(result);
		
		return result;
	}
		
		
	
	public void printResult(Double[] result)
	{
		
		for(int i = 0; i < result.length; i++)
		{
			
				if(result[i] != 0.0)
				{
					System.out.println(distinctPara.get(i)+" "+result[i]) ;
				}
			
		}
		
		
	}
	
	
	
	

}
