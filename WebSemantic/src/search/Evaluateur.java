package search;

import java.io.*;
import java.util.Vector;

public class Evaluateur 
{
	public Evaluateur(String qrel, Vector result)
	{
		Vector base = loadFile("requetes/qrel/"+qrel);
		
		for (int i = 0; i < result.size(); i++)
		{
			System.out.println(result.get(i));
		}
		
		System.out.println("Rappel de la requete : "+rappel(base, result));
		System.out.println("PrŽcision de la requete : "+precision(base, result));
		
		
		
		
		
	}
	
	
	public Vector loadFile(String filename)
	{
		Vector qrel = new Vector();
		
		try
		{
			InputStream ips = new FileInputStream(filename); 
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine())!=null)
			{
				String[] test = ligne.split("\t");

				if(test[test.length -1].equals("1"))
				{
					//System.out.println(test[test.length -1]);
					qrel.add(test[1]+" "+test[0]);
				}
				
				
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
		
		return qrel;
		
	}
	
	
	public double rappel(Vector pDocPertinents, Vector pDocObtenus)
	{
		
		int nbPert = getNbPertinents(pDocPertinents, pDocObtenus);
		
		return (double) nbPert/pDocPertinents.size();
	}
	
	public double precision(Vector pDocPertinents, Vector pDocObtenus)
	{
		int nbPert = getNbPertinents(pDocPertinents, pDocObtenus);
		return (double) nbPert/pDocObtenus.size();
		
	}
	
	
	public int getNbPertinents(Vector pDocPertinents, Vector pDocObtenus)
	{
		int nbPertinents = 0;
		
		for (int i=0; i < pDocObtenus.size(); i++)
		{
			if(pDocPertinents.contains(pDocObtenus.get(i))) nbPertinents++;
		}
		
		return nbPertinents;
	}
}
