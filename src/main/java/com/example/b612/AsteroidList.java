package com.example.b612;

import com.google.api.server.spi.config.Api;
import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.lang.StringBuffer;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.users.User;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.compute.Compute;
import com.google.api.services.compute.model.Instance;
import com.google.api.services.compute.model.InstanceList;
import com.google.api.services.compute.model.Operation;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Named;
import javax.servlet.ServletException;

/*Initiate the API for this class*/
@Api(
	    name = "helloworld",
	    version = "v1",
	    // You can add additional SCOPES as a comma separated list of values
	    scopes = {Constants.EMAIL_SCOPE},
	    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
	    audiences = {Constants.ANDROID_AUDIENCE}
	)

/**
 * Class for the management of the list of asteroids
 * 
 * @author Daniel Hawes
 *
 */
public class AsteroidList
{
	DatabaseControl db = new DatabaseControl();
	
	/**
	 * Insert a new Asteroid using the API
	 * 
	 * @param asteroidName asteroid name
	 * @param diameter diameter of asteroid
	 * @param dimensionL length of asteroid
	 * @param dimensionW width of asteroid
	 * @param dimensionH height of asteroid
	 * @param meanDFromSun mean distance from the sun
	 * @return
	 */
	@ApiMethod(name = "asteroidList.addAsteroid", httpMethod = "post")
	public Asteroid insertAsteroid(@Named("asteroidName") String asteroidName, @Named("diameter") double diameter, 
							@Named("dimensionL") double dimensionL, @Named("dimensionW") double dimensionW,
							@Named("dimensionH") double dimensionH, @Named("meanDFromSun") double meanDFromSun)
	{
		//Create a response of the new Asteroid to return
		Asteroid response = new Asteroid(asteroidName, diameter, dimensionL, dimensionW, dimensionH, meanDFromSun);

		//Try to add this asteroid to the database
		try {
			db.addAsteroidToDatabase(response);
		} catch (ServletException e) {
			e.printStackTrace();
		}
		
		//Information for connecting to the Compute Engine
		String projectID = "better-world-175517";
		String zone = "us-east1-c";
		String instance = "computeasteroid";
	
		//Attempt to connect to the Compute Engine
		Compute computeService;
		try {
			computeService = createComputeService();
			Compute.Instances.Start request = computeService.instances().start(projectID, zone, instance);
			Operation response2 = request.execute();
			
			System.out.println(response2);
						
		} catch (IOException | GeneralSecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return response;
	}
	
	/**
	 * Method to multiply together the dimensions of an asteroid
	 * 
	 * @param id autoincremented ID of the reqeust
	 * @param request The request of the compute engine
	 * @param status status of the request
	 */
	@ApiMethod(name = "asteroidList.multiplyAsteroid", httpMethod = "post")
	public void multiplyAsteroid(@Named("id") String id, @Named("request") String request, @Named("status") String status)
	{
		//Asteroid asteroid = 

	}
	
	/**
	 * Set up the Compute Engine service
	 * 
	 * @return Compute.Builder
	 * 
	 * @throws IOException
	 * @throws GeneralSecurityException
	 */
	public static Compute createComputeService() throws IOException, GeneralSecurityException {
	    HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
	    JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();

	    GoogleCredential credential = GoogleCredential.getApplicationDefault();
	    if (credential.createScopedRequired()) {
	      credential =
	          credential.createScoped(Arrays.asList("https://www.googleapis.com/auth/cloud-platform"));
	    }

	    return new Compute.Builder(httpTransport, jsonFactory, credential)
	        .setApplicationName("b612_BetterWorld/1.0")
	        .build();
	  }
	
	/**
	 * Returns the list of asteroids to iterate over
	 * 
	 * @return list of asteroids
	 * @throws ServletException
	 */
	public ArrayList<Asteroid> listAsteroids() throws ServletException
	{
		try
		{
			return db.getAsteroidsFromDatabase();
		} catch (ServletException e)
		{
			e.printStackTrace();
		}
		return null;
	}

}
