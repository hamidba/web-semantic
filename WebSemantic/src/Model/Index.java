package Model;

import java.sql.ResultSet;
import java.sql.*;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Vector;

import classes.Connector;
import classes.Mot;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
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
			//System.out.println(mot.get_nomDoc());
			
			
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
	
	
	public void indexer(Vector<Mot> data ) throws SQLException
	{
		
		_data = data;
		
		//Insertion des mot
		String reqInsertionMot = "INSERT INTO mot(mot) VALUES (?) ON DUPLICATE KEY UPDATE nb_occur=nb_occur+1";
		String reqInsertionPara = "INSERT INTO paragraphe (nomDoc, path, position, mot) VALUES (?,?,?,?)";

		
		//Pr�paration de la requete
		PreparedStatement ps = (PreparedStatement) _handlerBD.prepareStatement(reqInsertionMot);
		PreparedStatement ps2 = (PreparedStatement) _handlerBD.prepareStatement(reqInsertionPara);

		
		//Construction de la chaine � inserer
		String chaine = "";
		String temp;
		
		int i = 0; 
		
		for (Mot m : _data)
		{
			if(i == 0)
			{
				chaine+="("+m.get_chaine()+")";
			}

		    chaine+=",("+m.get_chaine()+")";
				
			i++;
		}
		
		System.out.println(chaine);
		
		/*
		
		for(int i=0; i < _data.size(); i++)
		{
			Mot m = _data.get(i);
			if(i == 0)
			{
			    temp = "("+m.get_chaine()+")";
				chaine+=temp;
			}
			else
			{
				 temp = ",("+m.get_chaine()+")";
				 chaine+=temp;
				
			}
		}
		*/
		
		//execution de la requete
		ps.setString(1,chaine);
		System.out.println("Debut de l'indexation ! Loading ...");
		ps.executeUpdate();
		
	
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
	public HashMap getAll(String mot) throws SQLException
	{
		HashMap listeMots = null;
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT nomDoc, path, freq_mot FROM paragraphe WHERE mot = '"+mot+"'");
		
		while(res.next())
		{
			Mot m = new Mot(mot, res.getString("path"), res.getString("nomDoc"), 0);
			listeMots.put(res.getInt("freq_mot"), m);
			
		}
		
		return listeMots;
		
	}
	
	
	public double getFrequenceMot(String mot, String path) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement(ResultSet.TYPE_FORWARD_ONLY,
				ResultSet.CONCUR_READ_ONLY);
		
		ResultSet rs = s.executeQuery("SELECT freq_mot FROM paragraphe WHERE mot = '"+mot+"' and path= '"+path+"'");
		if (rs.first())
		{
			return (double)rs.getDouble(1);
		}
		return 0;
		
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
		ResultSet res = s.executeQuery("SELECT SUM(freq_mot) as sumFreq FROM paragraphe WHERE path = '"+path+"'");
		if(res.first()) 
		{
			return res.getInt("sumFreq");
		
		}
		return 0;
	}
	
	
	
	public int getNbMotParagraphes(String mot) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT SUM(freq_mot) as sumFreq FROM paragraphe WHERE mot = '"+mot+"'");
		if(res.first()) 
		{
			return res.getInt("sumFreq");
		
		}
		return 0;
	}
	
	public int getNbParagraphe() throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT COUNT(idPar) as nbPar FROM paragraphe");
		res.first();
		
		return res.getInt("nbPar");
	}
	
	public int countMotParagraphe(String mot) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT COUNT(idPar) as nb FROM paragraphe where mot = '"+mot+"'");
		
		if(res.first()) 
		{
			return res.getInt("nb");
		}
		
		return 0;
	}
	
	public String getParagraphe(String path) throws SQLException
	{
		Statement s = (Statement) _handlerBD.createStatement();
		ResultSet res = s.executeQuery("SELECT path FROM paragraphe where path = '"+path+"'");
		return res.getArray(1).toString();
		
	}
	
	public void ponderer () throws SQLException
	{
		String mot;
		String path;
		int nbApparition, nbParagraphe;
		double idf;
		double freq;
		int nbMotPara;
		double tf;
		
		Statement s = (Statement) _handlerBD.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT mot, path FROM paragraphe");
		
		while(rs.next())
		{
			//On recup�re un mot
			mot = rs.getString("mot");
			path = rs.getString("path");
			
			//Calcul IDF
			nbApparition = getNbMotParagraphes(mot);
			nbParagraphe = countMotParagraphe(mot);
			idf = (double) nbApparition/nbParagraphe;
			
			//Calcul TF
		    freq = getFrequenceMot(mot, path);
		    nbMotPara = getNbMotPara(path);
		    tf = (double)freq/nbMotPara;
		    
		    //Calcul TF*IDF
		    double tfxIdf = tf*idf;
		    
		    //Insertion du poid dans l'index (TF*IDF)
		    Statement s2 = (Statement) _handlerBD.createStatement();
			
			int result = s2.executeUpdate("UPDATE paragraphe SET poid = "+tfxIdf+" WHERE mot = '"+mot+"' AND path='"+path+"'");
			
			System.out.println(mot+" "+result);
		}
		s.close();
		rs.close();
	}
	
	
	
	public Vector<String> getVocabulaire() throws SQLException
	{
		
		
		Statement s = (Statement) _handlerBD.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT mot FROM mot");
		
		if( rs.first() )
		{
			Vector<String> vocabulaire = new Vector<String>();
			
			while(rs.next())
			{
				vocabulaire.add(rs.getString(1));
			}
			
			return vocabulaire;
		}
		
		return null;
	}
	
	
	
	public Vector getDistinctParagraphe() throws SQLException
	{
		
	
		Statement s = (Statement) _handlerBD.createStatement();
		
		ResultSet rs = s.executeQuery("SELECT distinct path, nomDoc FROM paragraphe");
		
		if( rs.first() )
		{
			Vector paragraphes = new Vector();
			int cpt = 0;
			
			while(rs.next())
			{
				
				paragraphes.add(rs.getString(1)+" "+rs.getString(2));
				cpt++;
			}
			
			return paragraphes;
			
		}
		return null;
		
	}
	
	public Vector<Double> getVectorParagraphe(String path, Vector<String> vocabulaire) throws SQLException
	{
		
		Statement s = (Statement) _handlerBD.createStatement();
		String[] test = path.split(" ");
		ResultSet rs = s.executeQuery("SELECT mot, poid FROM paragraphe where path ='"+test[0]+"' and nomDoc = '"+test[1]+"'");
		
		if( rs.first() )
		{
			int n = vocabulaire.size();
			Vector<Double> paragraphe = new Vector<Double>();
			paragraphe.setSize(n);
			int indexMot;
			
			while(rs.next())
			{
				if(vocabulaire.contains(rs.getString(1)))
				{
					indexMot = vocabulaire.indexOf(rs.getString(1));
					paragraphe.insertElementAt(rs.getDouble(2), indexMot);
				}
				
				
			}
			
			return paragraphe;
		}
		
		return null;
		
	}
	
	
	
}
