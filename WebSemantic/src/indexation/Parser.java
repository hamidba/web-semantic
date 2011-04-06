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

public class Parser {
	static org.jdom.Document document;
	static Element racine;
	
	static void afficheALL()
	{
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
			System.out.println(pRecit.size());
			
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
				String[] tabMot = para.split(" ");
				
				Vector<String> vectorMot = new Vector<String>();
				for(int i = 0 ; i < tabMot.length ; i++){
					vectorMot.add(tabMot[i]);
				}
				
				System.out.println("===================\nVector AVANT ");
				for (String ch : vectorMot) {
					System.out.println(ch);
				}
				
				if(vectorMot.contains("la")){
					System.out.println("===============\n============\nLA\n=============\n============");
				}
				
				// suppression de tous les mots qui se trouvent dans la stop liste dans le P
				String fichier = "D:\\Etudes\\M2 ICE 2010-2011\\WEB_SEMANTIQUE\\stopListe.txt";
				
				//lecture du fichier	
				try{
					InputStream ips = new FileInputStream(fichier); 
					InputStreamReader ipsr = new InputStreamReader(ips);
					BufferedReader br = new BufferedReader(ipsr);
					String ligne;
					while((ligne = br.readLine()) != null){
						//System.out.println(ligne);
						if(vectorMot.contains(ligne)){
							//System.out.println(ligne);
							vectorMot.remove(ligne);
						}
					}
					
					System.out.println("=======================\n\nVector APRES");
					for (String ch : vectorMot) {
						System.out.println(ch);
					}
					br.close(); 
				}		
				catch (Exception e){
					System.out.println(e.toString());
				}
				
				// on ajoute l'�l�ment courant dans le vecteur de description
				vectorRecitSecPara.add(p);
			}
		}		
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
	
	    afficheALL();
	}
}
