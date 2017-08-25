package com.example.b612;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.ServletException;

import com.google.apphosting.api.ApiProxy;

/**
 * Controls all the database querys and connections
 * 
 * @author Daniel Hawes
 *
 */
public class DatabaseControl
{
	Connection aConn; //connection to the Asteroids database
	Connection requestConn; //connection to the requests database
	Connection resultConn; //connection to the result database
	final String addQuery = "INSERT INTO Asteroids (Name, Diameter, Dim_L, Dim_W, Dim_H, MeanDistanceFromSun) VALUES (?, ?, ?, ?, ?, ?)";
	final String selectAllQuery = "SELECT * FROM Asteroids";
	final String insertMultiplyQuery = "INSERT INTO requests (request, status) VALUES (?, ?)";
	
	public DatabaseControl()
	{
		getConnections();
		System.out.println("CONNECTION: " + aConn);
		System.out.println("CONNECTION: " + requestConn);
		System.out.println("CONNECTION: " + resultConn);
	}
	
	public void addAsteroidToDatabase(Asteroid asteroid) throws ServletException
	{		
		try(PreparedStatement statementAddAsteroid = aConn.prepareStatement(addQuery))
		{
			statementAddAsteroid.setString(1, asteroid.getName());
			statementAddAsteroid.setDouble(2, asteroid.getDiameter());
			statementAddAsteroid.setDouble(3, asteroid.getDimension()[0]);
			statementAddAsteroid.setDouble(4, asteroid.getDimension()[1]);
			statementAddAsteroid.setDouble(5, asteroid.getDimension()[2]);
			statementAddAsteroid.setDouble(6, asteroid.getMeanDFromSun());
			statementAddAsteroid.executeUpdate();
		} catch(SQLException e)
		{
			throw new ServletException("SQL error", e);
			
		}
	}
	
	/**
	 * Retrieve all Asteroids from the database
	 * 
	 * @return ArrayList of Asteroids
	 * @throws ServletException
	 */
	public ArrayList<Asteroid> getAsteroidsFromDatabase() throws ServletException
	{
		ArrayList<Asteroid> temp = new ArrayList<Asteroid>();
		try (ResultSet rs = aConn.prepareStatement(selectAllQuery).executeQuery())
		{
			while(rs.next())
			{
				Asteroid tempAsteroid = new Asteroid(rs.getString("Name"), rs.getDouble("Diameter"), rs.getDouble("Dim_L"),
						rs.getDouble("Dim_W"), rs.getDouble("Dim_H"), rs.getDouble("MeanDistanceFromSun"));
				temp.add(tempAsteroid);
				System.out.println("Name: " + rs.getString("Name"));
				System.out.println("\tDiameter: " + rs.getDouble("Diameter"));
				System.out.println("\tLength: " + rs.getDouble("Dim_L"));
				System.out.println("\tWidth: " + rs.getDouble("Dim_W"));
				System.out.println("\tHeight: " + rs.getDouble("Dim_H"));
				System.out.println("\tDistance: " + rs.getDouble("MeanDistanceFromSun"));
			}
			
		} catch (SQLException e)
		{
			throw new ServletException("SQL error", e);
		}
		return temp;
	}
	
	/**
	 * return the Asteroid from the Asteroid table with the given name
	 * 
	 * @param name name of the new asteroid
	 * @return Asteroid object
	 * @throws ServletException
	 */
	public Asteroid getAsteroidFromDatabase(String name) throws ServletException
	{
		String query = "SELECT * FROM Asteroids WHERE Name = '" + name + "'";
		try (ResultSet rs = aConn.prepareStatement(query).executeQuery())
		{
			while(rs.next())
			{
				System.out.println("Name: " + rs.getString("Name"));
				System.out.println("\tDiameter: " + rs.getDouble("Diameter"));
				System.out.println("\tLength: " + rs.getDouble("Dim_L"));
				System.out.println("\tWidth: " + rs.getDouble("Dim_W"));
				System.out.println("\tHeight: " + rs.getDouble("Dim_H"));
				System.out.println("\tDistance: " + rs.getDouble("MeanDistanceFromSun"));				
			}
			return new Asteroid(rs.getString("Name"), rs.getDouble("Diameter"), rs.getDouble("Dim_L"), 
					rs.getDouble("Dim_W"), rs.getDouble("Dim_H"), rs.getDouble("MeanDistanceFromSun"));
			
		} catch (SQLException e)
		{
			throw new ServletException("SQL error", e);
		}
	
	}

	/**
	 * Update the Request table with the new request to multiply
	 * 
	 * @param name name of the asteroid
	 * @throws ServletException
	 */
	public void getMultiplicationFromDatabase(String name) throws ServletException
	{
		Asteroid asteroid = getAsteroidFromDatabase(name);
		try(PreparedStatement statementMultiplyAsteroid = requestConn.prepareStatement(insertMultiplyQuery))
		{
			statementMultiplyAsteroid.setString(1, "MULTIPLY: " + asteroid.getDimension()[0] + ","
					+ asteroid.getDimension()[1] + "," + asteroid.getDimension()[2]);
			statementMultiplyAsteroid.setString(2, "PENDING");
			statementMultiplyAsteroid.executeUpdate();
			String selectIDQuery = "";
		} catch(SQLException e)
		{
			throw new ServletException("SQL error", e);
		}
	}
	
	/**
	 * Set up the connection objections
	 */
	public void getConnections()
	{
		ApiProxy.Environment env = ApiProxy.getCurrentEnvironment();
	    Map<String,Object> attr = env.getAttributes();
		String hostname = (String) attr.get("com.google.appengine.runtime.default_version_hostname");
		/**
		 * the cloudsql-local and cloudsql files are stated in the appengine-web.xml
		 */
		String aUrl = hostname.contains("localhost:")
		          ? System.getProperty("asteroid-cloudsql-local") : System.getProperty("asteroid-cloudsql");
		String requestUrl = hostname.contains("localhost:")
				  ? System.getProperty("request-cloudsql-local") : System.getProperty("request-cloudsql");
		String resultUrl = hostname.contains("localhost:")
				  ? System.getProperty("result-cloudsql-local") : System.getProperty("result-cloudsql");
	    try
	    {
	        aConn = DriverManager.getConnection(aUrl);
	        requestConn = DriverManager.getConnection(requestUrl);
	        resultConn = DriverManager.getConnection(resultUrl);
	    }
	    catch(SQLException e)
	    {
	    	aConn = null;
	    	requestConn = null;
	    	resultConn = null;
	    	System.out.println("HERE'S THE ERROR:");
	    	System.out.println(e.getMessage());
	    }
	}
}
