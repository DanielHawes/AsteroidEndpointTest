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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import javax.inject.Named;

@Api(
	    name = "helloworld",
	    version = "v1",
	    // You can add additional SCOPES as a comma separated list of values
	    scopes = {Constants.EMAIL_SCOPE},
	    clientIds = {Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID, Constants.IOS_CLIENT_ID},
	    audiences = {Constants.ANDROID_AUDIENCE}
	)

public class AsteroidList
{
	public static ArrayList<Asteroid> asteroidList = new ArrayList<Asteroid>();
	
	
	static
	{
		double[] ceresDim = {965, 962, 891};
		asteroidList.add(new Asteroid("Ceres", 946, ceresDim, 2.766));
		SpannerTools tools = new SpannerTools();
		tools.addAsteroid(new Asteroid("Ceres", 946, ceresDim, 2.766));
		
		double[] vestaDim = {572.6, 557.2, 446.4};
		asteroidList.add(new Asteroid("Vesta", 525.4, vestaDim, 2.362));
		
		double[] pallasDim = {550, 516, 476};
		asteroidList.add(new Asteroid("Pallas", 512, pallasDim, 2.773));
		
		double[] hygieaDim = {530, 407, 370};
		asteroidList.add(new Asteroid("Hygiea", 431, hygieaDim, 3.139));
	}
	
	public Asteroid getAsteroid(@Named("name") String name) throws NotFoundException
	{
		for(int i=0; i < asteroidList.size(); i++)
		{
			if(asteroidList.get(i).getName().equals(name))
			{
				return asteroidList.get(i);
			}
		}
		throw new NotFoundException("Asteroid '" + name + "' is not in the system yet");
	}
	
	@ApiMethod(name = "asteroidList.addAsteroid", httpMethod = "post")
	public Asteroid insertAsteroid(@Named("asteroidName") String asteroidName, @Named("diameter") double diameter, 
							@Named("dimensionL") double dimensionL, @Named("dimensionW") double dimensionW,
							@Named("dimensionH") double dimensionH, @Named("meanDFromSun") double meanDFromSun)
	{
		double[] newDim = {dimensionL, dimensionW, dimensionH};
		Asteroid response = new Asteroid(asteroidName, diameter, newDim, meanDFromSun);
		asteroidList.add(response);
		
		String projectID = "better-world-175517";
		String zone = "us-east1-c";
		String instance = "computeasteroid";
			
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
	
	public ArrayList<Asteroid> listAsteroids()
	{
		return asteroidList;
	}

}
