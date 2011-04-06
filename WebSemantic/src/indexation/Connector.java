package indexation;

import java.sql.*;

import com.mysql.jdbc.*;
import com.mysql.jdbc.Connection;

/**
 * Classe permettant de se connecter à la base de données
 * 
 * @author m2ice-2
 *
 */
public class Connector 
{
	Connection connection;
	
	public Connector(String host, String db, String user, String mdp)
	{
		
		 	
		    try
		     {
		         Class.forName("com.mysql.jdbc.Driver");		         
		         connection = (Connection) DriverManager.getConnection("jdbc:mysql://"+host+":3306/"+db+","+user+","+mdp);
		         System.out.println("Connexion Ok");
		     }
		     catch(Exception cnfe)
		     {
		         System.out.println("Error:"+cnfe.getMessage());
		     }
		
	}
	
	
	public Connection getConnection()
	{
		return this.connection;
	}
	
}
