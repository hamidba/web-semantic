package indexation;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class Parser {
	static org.jdom.Document document;
	static Element racine;
	
	static void afficheALL()
	{
		// récupération de la liste des noueds intéressants pour le parsing
		Element pres = racine.getChild("PRESENTATION");
		String titre = pres.getChildText("TITRE");
		String auteur = pres.getChildText("AUTEUR");
		String date = pres.getChildText("DATE");
		List pDescription = pres.getChild("DESCRIPTION").getChildren("P");
		
		//On crée un Iterator sur notre liste de paragraphes description
		Iterator iDesc = pDescription.iterator();
		Vector<Element> vDesc = new Vector<Element>();
		while(iDesc.hasNext())
		{
			//On recrée l'Element courant à chaque tour de boucle afin de
			//pouvoir utiliser les méthodes propres aux Element comme :
			//selectionner un noeud fils, modifier du texte, etc...
			Element courant = (Element)iDesc.next();
			
			// on ajoute l'élément courant dans le vecteur de description
			vDesc.add(courant);
		}
		
		//On crée une List contenant tous les noeuds "P" de l'Element racine
		Element recit = racine.getChild("RECIT");
	}
	   
	public static void main(String[] args)
	{
		//On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//On crée un nouveau document JDOM avec en argument le fichier XML que l'on souhaite parser
			//Le parsing est terminé ;)
			document = sxb.build(new File("D:\\Etudes\\M2 ICE 2010-2011\\WEB_SEMANTIQUE\\Collection\\d001.xml"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		//On initialise un nouvel élément racine avec l'élément racine du document.
		racine = document.getRootElement();
	
	    afficheALL();
	}
}
