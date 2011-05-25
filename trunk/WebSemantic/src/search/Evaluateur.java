package search;

import java.io.*;
import java.util.Vector;

public class Evaluateur 
{
	public Evaluateur(String qrel, Vector distinctPara, Vector reult)
	{
		loadFile("requetes/qrel/"+qrel);
	}
	
	
	public void loadFile(String filename)
	{
		
		try
		{
			InputStream ips = new FileInputStream(filename); 
			InputStreamReader ipsr = new InputStreamReader(ips);
			BufferedReader br = new BufferedReader(ipsr);
			String ligne;
			while ((ligne = br.readLine())!=null)
			{
				String[] test = new String[20000];
				
				System.out.println(ligne.split(" ").toString());
			}
			br.close(); 
		}		
		catch (Exception e){
			System.out.println(e.toString());
		}
	}

}
