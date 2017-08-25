package com.example.b612;

import java.util.ArrayList;
import java.util.List;

import com.google.api.server.spi.config.Api;
import com.google.cloud.spanner.DatabaseAdminClient;
import com.google.cloud.spanner.DatabaseClient;
import com.google.cloud.spanner.DatabaseId;
import com.google.cloud.spanner.Spanner;
import com.google.cloud.spanner.SpannerOptions;
import com.google.cloud.spanner.Statement;
import com.google.cloud.spanner.Mutation;
import com.google.cloud.spanner.ResultSet;


public class SpannerTools
{
	final String INSTANCE_ID = "asteroids";
	final String DATABASE_ID = "asteroids_db";
	
	SpannerOptions options;
	Spanner spanner;
	DatabaseId db;
	DatabaseClient dbClient;
	DatabaseAdminClient dbAdminClient;
	
	public SpannerTools()
	{
		options = SpannerOptions.newBuilder().build();
	}
	
	public void addAsteroid(Asteroid newAsteroid)
	{
		spanner = options.getService();
		try
		{
			db = DatabaseId.of(options.getProjectId(), INSTANCE_ID, DATABASE_ID);
			//System.out.println(db);
			dbClient = spanner.getDatabaseClient(db);
			dbAdminClient = spanner.getDatabaseAdminClient();
						
			/*List<Mutation> mutations = new ArrayList<>();
			mutations.add(
					Mutation.newInsertBuilder("Asteroids")
						.set("Name")
						.to(newAsteroid.getName())
						.set("Diameter")
						.to(newAsteroid.getDiameter())
						.set("Dim_L")
						.to(newAsteroid.getDimension()[0])
						.set("Dim_W")
						.to(newAsteroid.getDimension()[1])
						.set("Dim_H")
						.to(newAsteroid.getDimension()[2])
						.set("MeanDistanceFromSun")
						.to(newAsteroid.getMeanDFromSun())
						.build());
			
			dbClient.write(mutations);*/
			
			ResultSet resultSet = dbClient.singleUse().executeQuery(Statement.of("SELECT 1"));
			System.out.println("\n\nResults:");
		      // Prints the results
		      while (resultSet.next()) {
		        System.out.printf("%d\n\n", resultSet.getLong(0));
		      }
		}
		catch (Exception e)
		{
			System.out.println("SPANNER UNABLE TO WRITE");
		} finally
		{
			spanner.close();
		}
	}
}
