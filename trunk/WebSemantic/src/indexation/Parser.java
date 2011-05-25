package indexation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.Character.Subset;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import classes.Mot;

public class Parser {
	static org.jdom.Document document;
	static Element racine;

	// création du tableau de mots à retourner
	private Vector<Mot> _vectorMot = new Vector<Mot>();
	private Vector<String> _stopListe = getStopListe();
	
	public Parser()
	{
		//On crée une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			// on se place dans le répertoire contenant tous les fichiers xml
			FilenameFilter filter = new FilenameFilter() {
				
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.endsWith(".xml");
				}
			};
			
			File repertoire = new File("resources/collection");
			File[] files = repertoire.listFiles(filter);
			
			//System.out.println("Nombre de fichiers xml = "+files.length);
			
			// boucle sur le répertoire contenant tous les documents xml
			for (File file : files) {
				//System.out.println(file.getName());
				
				//On crée un nouveau document JDOM avec en argument le fichier XML que l'on souhaite parser
				document = sxb.build(file);
				
				//On initialise un nouvel élément racine avec l'élément racine du document.
				racine = document.getRootElement();
				
				parseDoc(file.getName());
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void parseDoc(String nomDoc)
	{		
		String caracSplit = "[ ,;:!?.*/'(){}-[0-9]*\"«»]";
		
		// récupération de la liste des noeuds intéressants pour le parsing		
		Element pres = racine.getChild("PRESENTATION");
		String titre = pres.getChildText("TITRE").toLowerCase();
		String[] tabMotTitre = titre.split(caracSplit); // split sur plusieurs caractères de ponctuation
		
		String path = "/BALADE[1]/PRESENTATION[1]/TITRE[1]";  
		
		int positionMotTitre = 1;
		for(int i = 0 ; i < tabMotTitre.length ; i++){
			String mot = tabMotTitre[i].trim();// on supprime tous les espaces présents dans le mot
			if(mot.length() > 6){
				mot = mot.substring(0, 6);
			}
			
			if(mot.length() > 0 && !_stopListe.contains(mot)){				
				// on alimente le vecteur de paragraphes qui contient des Mot
				Mot m = new Mot(mot, path, nomDoc, positionMotTitre++);
				_vectorMot.add(m);
			}
		}	
		
		//String auteur = pres.getChildText("AUTEUR");	
		
		// on regarde si une description est présente
		if(!pres.getChildren("DESCRIPTION").isEmpty()){
			List pDescription = pres.getChild("DESCRIPTION").getChildren("P");
			
			//On crée un Iterator sur notre liste de paragraphes description
			Iterator iDesc = pDescription.iterator();
			//Vector<Element> vectorParaDesc = new Vector<Element>();
			int positionPara = 1;
			while(iDesc.hasNext())
			{
				//On recrée l'Element courant à chaque tour de boucle
				Element pDesc = (Element)iDesc.next();
				//int positionPara = vectorParaDesc.size()+1;
				path = "/BALADE[1]/PRESENTATION[1]/DESCRIPTION[1]/P["+positionPara+"]";

				//création du tableau de tous les mots du paragraphe
				String paraDesc = pDesc.getText().toLowerCase();
				String[] tabMotDesc = paraDesc.split(caracSplit); // split sur plusieurs caractères de ponctuation
				
				int positionMot = 1;
				for(int i = 0 ; i < tabMotDesc.length ; i++){
					String mot = tabMotDesc[i].trim();// on supprime tous les espaces présents dans le mot
					if(mot.length() > 6){
						mot = mot.substring(0, 6);
					}
					
					if(mot.length() > 0 && !_stopListe.contains(mot)){						
						// on alimente le vecteur de paragraphes qui contient des Mot
						Mot m = new Mot(mot, path, nomDoc, positionMot++);
						_vectorMot.add(m);
					}
				}				
				
				// on ajoute l'élément courant dans le vecteur de description
				//vectorParaDesc.add(pDesc);
				positionPara++;
			}		
		}
		
		Element recit = racine.getChild("RECIT");
		
		if(!recit.getChildren("P").isEmpty()){
			List pRecit = recit.getChildren("P");
			// on va itérer sur les P sous la balise SEC
			Iterator iRecitPara = pRecit.iterator();
			//Vector<Element> vectorRecitPara = new Vector<Element>();
			int positionRecitPara = 1;
			while(iRecitPara.hasNext())
			{
				// on récupère l'élément P courant
				Element p = (Element)iRecitPara.next();
				//int positionRecitPara = vectorRecitPara.size()+1;
				
				path = "/BALADE[1]/RECIT[1]/P["+positionRecitPara+"]";
				
				//création du tableau de tous les mots du paragraphe
				String para = p.getText().toLowerCase();
				String[] tabMotRecitP = para.split(caracSplit); // split sur plusieurs caractères de ponctuation
				
				int positionMot = 1;
				for(int i = 0 ; i < tabMotRecitP.length ; i++){
					String mot = tabMotRecitP[i].trim();// on supprime tous les espaces présents dans le mot
					if(mot.length() > 6){
						mot = mot.substring(0, 6);
					}
					
					if(mot.length() > 0 && !_stopListe.contains(mot)){						
						// on alimente le vecteur de paragraphes qui contient des Mot
						Mot m = new Mot(mot, path, nomDoc, positionMot++);
						_vectorMot.add(m);
					}
				}				
				
				// on ajoute l'élément courant dans le vecteur de description
				//vectorRecitPara.add(p);
				positionRecitPara++;
			}
		}
		
		List secRecit = recit.getChildren("SEC");
		
		// on va itérer sur les SEC qui se trouvent sous la balise RECIT
		Iterator iSecRecit = secRecit.iterator();
		//Vector<Element> vectorRecitSec = new Vector<Element>();
		int positionRecitSec = 1;
		while(iSecRecit.hasNext())
		{
			// on récupère l'élément SEC courant
			Element sec = (Element)iSecRecit.next();
			//int positionRecitSec = vectorRecitSec.size()+1;
			
			List pRecitSec = sec.getChildren("P");
			
			// on va itérer sur les P sous la balise SEC
			Iterator iRecitSecPara = pRecitSec.iterator();
			//Vector<Element> vectorRecitSecPara = new Vector<Element>();
			int positionRecitSecPara = 1;
			while(iRecitSecPara.hasNext())
			{
				// on récupère l'élément P courant
				Element p = (Element)iRecitSecPara.next();
				//int positionRecitSecPara = vectorRecitSecPara.size()+1;
				
				path = "/BALADE[1]/RECIT[1]/SEC["+positionRecitSec+"]/P["+positionRecitSecPara+"]";
				
				//création du tableau de tous les mots du paragraphe
				String para = p.getText().toLowerCase();
				String[] tabMot = para.split(caracSplit); // split sur plusieurs caractères de ponctuation
				
				int positionMot = 1;
				for(int i = 0 ; i < tabMot.length ; i++){
					String mot = tabMot[i].trim();// on supprime tous les espaces présents dans le mot
					if(mot.length() > 6){
						mot = mot.substring(0, 6);
					}
					
					if(mot.length() > 0 && !_stopListe.contains(mot)){	
						// on alimente le vecteur de paragraphes qui contient des Mot
						Mot m = new Mot(mot, path, nomDoc, positionMot++);
						_vectorMot.add(m);
					}
				}				
				
				// on ajoute l'élément courant dans le vecteur de description
				//vectorRecitSecPara.add(p);
				positionRecitSecPara++;
			}
			//vectorRecitSec.add(sec);
			positionRecitSec++;
		}		
	}
	
	public static Vector<String> getStopListe(){
		String fichier = "resources/stopListe.txt";
		Vector<String> vectorStopListe = new Vector<String>();
		
		//lecture du fichier	
		try{
			InputStream ips = new FileInputStream(fichier); 
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			// pour tous les mots de la stopListe
			while((ligne = br.readLine()) != null){
				// ajout du stopMot dans le vecteur de stopListe
				vectorStopListe.add(ligne);
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		return vectorStopListe;
	}
	
	public Vector<Mot> get_vectorMot() {
		return _vectorMot;
	}
}
