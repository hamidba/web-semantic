package indexation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
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
	
	public void parseDoc()
	{
		// r�cup�ration de la stopListe
		Vector<String> stopListe = getStopListe();
		
		// r�cup�ration de la liste des noeuds int�ressants pour le parsing
		Element pres = racine.getChild("PRESENTATION");
		String titre = pres.getChildText("TITRE");
		String auteur = pres.getChildText("AUTEUR");	
		List pDescription = pres.getChild("DESCRIPTION").getChildren("P");
		
		//On cr�e un Iterator sur notre liste de paragraphes description
		Iterator iDesc = pDescription.iterator();
		Vector<Element> vectorParaDesc = new Vector<Element>();
		while(iDesc.hasNext())
		{
			//On recr�e l'Element courant � chaque tour de boucle afin de
			//pouvoir utiliser les m�thodes propres aux Element comme :
			//selectionner un noeud fils, modifier du texte, etc...
			Element courant = (Element)iDesc.next();
			
			// on ajoute l'�l�ment courant dans le vecteur de description
			vectorParaDesc.add(courant);
		}
		
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
				//System.out.println(positionPara+" = "+p.getText());
				
				//cr�ation du tableau de tous les mots du paragraphe
				String para = p.getText().toLowerCase();
				String[] tabMot = para.split("[ ,;:!?.*/'(){}]"); // split sur plusieurs caract�res de ponctuation
				
				//Vector<String> vectorPara = new Vector<String>();
				int positionMot = 1;
				for(int i = 0 ; i < tabMot.length ; i++){
					String mot = tabMot[i].trim();// on supprime tous les espaces pr�sents dans le mot
					if(mot.length() > 0 && !stopListe.contains(mot)){	
						Mot m = new Mot(mot, "", "", positionMot++);
						//vectorPara.add(mot);
						this._vectorMot.add(m);
					}
				}
				
//				System.out.println("=======================\n\nVector AVANT");
//				for (String ch : vectorPara) {
//					System.out.println("'"+ch+"'");
//				}				
				
				// on ajoute l'�l�ment courant dans le vecteur de description
				vectorRecitSecPara.add(p);
			}
		}		
	}
	
	public static Vector<String> getStopListe(){
		String fichier = "D:\\Etudes\\M2 ICE 2010-2011\\WEB_SEMANTIQUE\\stopListe.txt";
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
	   
	public static void main(String[] args)
	{
		//On cr�e une instance de SAXBuilder
		SAXBuilder sxb = new SAXBuilder();
		try
		{
			//On cr�e un nouveau document JDOM avec en argument le fichier XML que l'on souhaite parser
			//Le parsing est termin� ;)
			document = sxb.build(new File("D:\\Etudes\\M2 ICE 2010-2011\\WEB_SEMANTIQUE\\Collection\\d001.xml"));
		}
		catch(Exception e){
			e.printStackTrace();
		}
	
		//On initialise un nouvel �l�ment racine avec l'�l�ment racine du document.
		racine = document.getRootElement();
	
		Parser p = new Parser();
	    p.parseDoc();
	    
	    Vector<Mot> test = p.get_vectorMot();
	    for(Mot m : test){
	    	System.out.println(m.get_position()+" => "+m.get_chaine());
	    }
	}

	public Vector<Mot> get_vectorMot() {
		return _vectorMot;
	}
}
