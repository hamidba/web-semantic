package search;

import Model.Index;

/**
 * Classe permettant de chercher dans l'index à partir d'une requete
 * @author hamid
 *
 */
public class Rechercher 
{
	String req;
	String[] mots;
	Index index;
	
	public Rechercher(String requete)
	{
		index = new Index();
		req = requete;
		mots = req.split(" ");
		
		//Pour chaque mot on calcule son tf, son idf puis son tf-idf
		
	}
	
	/**
	 * TF = nb_occur / nb_total_mot_paragraphe
	 * @param mot
	 * @return
	 */
	public double tf(String mot)
	{
		//Recupération du nombre d'occurence d'un mot dans un paragraphe
		
		
		
		return 0.0;
	}

}
