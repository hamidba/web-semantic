package Model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import classes.Connector;
import classes.Mot;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;

/**
 * Classe permettant la creation d'index ˆ partir des données reçues
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
		_handlerBD = new Connector("127.0.0.1", "index", "root", "root").getConnection();

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
			System.out.println(mot.get_nomDoc());
			
			
			//Insertion d'un mot dans la table
			if(!motExiste(mot))
			{
				//Le mot n'existe pas on l'insere
				addMot(mot);
				//Insertion reussie -> ajout ligne dans para
				addParagraphe(mot);
				
			}
			else
			{
				//l'insertion a échoué (Contrainte de clŽ primaire) -> nbOccur++ et Insertion d'une ligne dans la table para
				addOccur(mot.get_chaine());
				addParagraphe(mot);
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
		
		int count = s.executeUpdate("INSERT INTO mot (mot) VALUES ('"+m.get_chaine()+"') ");
		System.out.println("Count :"+count);
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
		ResultSet rs = s.executeQuery("SELECT mot FROM paragraphe WHERE mot = '"+m.get_chaine()+"' AND path = '"+m.get_path()+"' AND nomDoc = '"+m.get_nomDoc()+"'");
		
		if(rs.first())
		{
			//Le mot existe dŽja dans le paragraphe on incremente sa frŽquence
			s.execute("UPDATE paragraphe SET freq_mot = freq_mot+1 WHERE mot = '"+m.get_chaine()+"' AND path = '"+m.get_path()+"' AND nomDoc = '"+m.get_nomDoc()+"'");
			return 0;
		}
		else
		{
			return s.executeUpdate("INSERT INTO paragraphe (nomDoc, path, position, mot) " +
					"VALUES ('"+m.get_nomDoc()+"','"+m.get_path()+"',"+m.get_position()+", '"+m.get_chaine()+"')");
		}
	}
	
	
	public Boolean motExiste(Mot mot) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT * FROM mot WHERE mot = '"+mot.get_chaine()+"'");
		return res.first();
			
	}
	
	/**
	 * Retourne freq_mot
	 * @param mot
	 * @throws SQLException 
	 */
	public Vector getNbOccur(String mot) throws SQLException
	{
		Vector<Mot> listeMots = null;
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT nomDoc, path, freq_mot FROM paragraphe WHERE mot = '"+mot+"'");
		
		while(res.next())
		{
			Mot m = new Mot(mot, res.getString("path"), res.getString("nomDoc"), 0);
			listeMots.add(res.getInt("freq_mot"), m);
		}
		
		return listeMots;
		
	}
	
	/**
	 * Retourne le nombre total d'apparition d'un mot ds un paragraphe
	 * @param mot
	 * @return
	 * @throws SQLException 
	 */
	public int getNbMotPara(String path) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT SUM(freq_mot) FROM paragraphe WHERE path = '"+path+"'");
		return res.getInt(1);
	}
	
	
}
