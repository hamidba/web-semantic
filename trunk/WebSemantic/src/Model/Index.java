package Model;

import java.sql.SQLException;
import java.util.Vector;

import classes.Connector;
import classes.Mot;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * Classe permettant la creation d'index à partir des données reçues
 * @author m2ice-2
 *
 */
public class Index 
{
	private Vector<Mot> _data; //Tableau contenant tous les mots de tous les documents 
	private Connection _handlerBD;
	
	
	public Index()
	{
		//Mise en place de la connexion
		_handlerBD = new Connector("127.0.0.1", "index", "root", "").getConnection();

	}
	
	
	/**
	 * Creation de l'index à partir d'un  vector de tous les mots necessaires
	 * @throws SQLException 
	 */
	public void createIndex(Vector<Mot> data) throws SQLException
	{
		//Initialisation des mots
		_data = data;
		
		//Boucle sur la totalité du vector
		for (Mot mot : _data)
		{
			System.out.println("Traitement du mot :" +mot.get_chaine() +" \n");
			//Insertion d'un mot dans la table
			if(0 == addMot(mot))  
			{
				//l'insertion a échoué (Contrainte de clé primaire) -> nbOccur++ et Insertion d'une ligne dans la table para
				addOccur(mot.get_chaine());
				if(0 == addParagraphe(mot))	{System.out.println("Insertion paragraphe échoué"); }
				
			}
			else
			{
				//Insertion reussie -> ajout ligne dans para
				if(0 == addParagraphe(mot))	{System.out.println("Insertion paragraphe échoué"); }
			}
		}		
	}
	
	
	
	/**
	 * Methode permettant d'inserer un mot dans la table mot
	 * @param m
	 * @return
	 */
	public int addMot(Mot m) throws SQLException
	{		
		Statement s = (Statement) _handlerBD.createStatement();
		int count = s.executeUpdate("INSERT INTO mot (mot) VALUES ("+m.get_chaine()+") ");
		s.close();
		return count;		
	}
	
	
	/**
	 * Incremente le nombre d'occurence d'un mot dans la table des mots 
	 * @throws SQLException 
	 */
	public void addOccur(String mot) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
	    s.executeUpdate("UPDATE mot SET nb_occur = nb_occur+1 WHERE mot ='"+mot+"'");
		s.close();
	}
	
	
	/**
	 * 
	 * @param m
	 * @return
	 * @throws SQLException
	 */
	public int addParagraphe(Mot m) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
		int resultSt1 = s.executeUpdate("INSERT INTO paragraphe (nomDoc, path, position, idMot) " +
				"VALUES ('"+m.get_nomDoc()+"','"+m.get_path()+"',"+m.get_position()+", '"+m.get_chaine()+"')");
		return resultSt1;
	}
	
	
	
	

}
