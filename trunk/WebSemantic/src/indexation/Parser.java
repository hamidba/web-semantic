package indexation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.jdom.Element;
import org.jdom.input.SAXBuilder;

import classes.Mot;

public class Parser {
	static org.jdom.Document document;
	static Element racine;

	// cr�ation du tableau de mots � retourner
	private Vector<Mot> _vectorMot = new Vector<Mot>();
	private Vector<String> _stopListe = getStopListe();
	
	public Parser()
	{
		//On cr�e une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			// on se place dans le r�pertoire contenant tous les fichiers xml
			FilenameFilter filter = new FilenameFilter() {
				
				
				@Override
				public boolean accept(File dir, String name) {
					// TODO Auto-generated method stub
					return name.endsWith(".xml");
				}
			};
			
			File repertoire = new File("Collection");
			File[] files = repertoire.listFiles(filter);
			
			System.out.println("Nombre de fichiers xml = "+files.length);
			
			// boucle sur le r�pertoire contenant tous les documents xml
			for (File file : files) {
				System.out.println(file.getName());
				
				//On cr�e un nouveau document JDOM avec en argument le fichier XML que l'on souhaite parser
				document = sxb.build(file);
				
				//On initialise un nouvel �l�ment racine avec l'�l�ment racine du document.
				racine = document.getRootElement();
				
				parseDoc();
			}
			System.out.println(_vectorMot.size());
		}
		catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void parseDoc()
	{		
		// r�cup�ration de la liste des noeuds int�ressants pour le parsing		
		Element pres = racine.getChild("PRESENTATION");
		String titre = pres.getChildText("TITRE");
		String auteur = pres.getChildText("AUTEUR");	
		/*
		List pDescription = pres.getChild("DESCRIPTION").getChildren("P");
		
		//On cr�e un Iterator sur notre liste de paragraphes description
		Iterator iDesc = pDescription.iterator();
		Vector<Element> vectorParaDesc = new Vector<Element>();
		while(iDesc.hasNext())
		{
			//On recr�e l'Element courant � chaque tour de boucle
			Element courant = (Element)iDesc.next();
			
			// on ajoute l'�l�ment courant dans le vecteur de description
			vectorParaDesc.add(courant);
		}
		
		*/
		
		Element recit = racine.getChild("RECIT");
		List secRecit = recit.getChildren("SEC");
		
		// on va it�rer sur les SEC qui se trouvent sous la balise RECIT
		Iterator iSecRecit = secRecit.iterator();
		while(iSecRecit.hasNext())
		{
			// on r�cup�re l'�l�ment SEC courant
			Element sec = (Element)iSecRecit.next();
			
			List pRecit = sec.getChildren("P");
			
			// on va it�rer sur les P sous la balise SEC
			Iterator iRecitSecPara = pRecit.iterator();
			Vector<Element> vectorRecitSecPara = new Vector<Element>();
			while(iRecitSecPara.hasNext())
			{
				// on r�cup�re l'�l�ment P courant
				Element p = (Element)iRecitSecPara.next();
				int positionPara = vectorRecitSecPara.size()+1;
				
				//cr�ation du tableau de tous les mots du paragraphe
				String para = p.getText().toLowerCase();
				String[] tabMot = para.split("[ ,;:!?.*/'(){}-\"]"); // split sur plusieurs caract�res de ponctuation
				
				int positionMot = 1;
				for(int i = 0 ; i < tabMot.length ; i++){
					String mot = tabMot[i].trim();// on supprime tous les espaces pr�sents dans le mot
					if(mot.length() > 0 && !_stopListe.contains(mot)){	
						Mot m = new Mot(mot, "", "", positionMot++);
						this._vectorMot.add(m);
					}
				}				
				
				// on ajoute l'�l�ment courant dans le vecteur de description
				vectorRecitSecPara.add(p);
			}
		}		
	}
	
	public static Vector<String> getStopListe(){
		String fichier = "Collection/stopListe.txt";
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
