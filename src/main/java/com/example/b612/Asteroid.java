package com.example.b612;

/**
 * Asteroid class for the storage and retrieval of variables of an Asteroid
 * @author Daniel Hawes
 *
 */
public class Asteroid
{
	private String name;
	private double diameter;
	private double[] dimension;
	private double dimensionL;
	private double dimensionW;
	private double dimensionH;
	private double meanDFromSun;
	
	
	/**
	 * Constructor for Asteroid that includes different dimension parameters
	 * 
	 * @param name name of asteroid
	 * @param diameter diameter of asteroid
	 * @param dimension_L length of asteroid
	 * @param dimension_W width of asteroid
	 * @param dimension_H height of asteroid
	 * @param meanDFromSun the mean distance from the sun
	 */
	public Asteroid(String name, double diameter, double dimension_L, double dimension_W, double dimension_H, double meanDFromSun)
	{
		this.name = name;
		this.diameter = diameter;
		double[] tempDimension = {dimension_L, dimension_W, dimension_H};
		this.dimension = tempDimension;
		this.dimensionL = dimension_L;
		this.dimensionW = dimension_W;
		this.dimensionH = dimension_H;
		this.meanDFromSun = meanDFromSun;
	}
	
	/**
	 * Constructor for Asteroid that has array for dimension parameter
	 * 
	 * @param name name of asteroid
	 * @param diameter diameter of asteroid
	 * @param dimension array of length, width, and height of asteroid
	 * @param meanDFromSun the mean distance from the sun
	 */
	public Asteroid(String name, double diameter, double[] dimension, double meanDFromSun)
	{
		this.name = name;
		this.diameter = diameter;
		this.dimension = dimension;
		this.dimensionL = dimension[0];
		this.dimensionW = dimension[1];
		this.dimensionH = dimension[2];
		this.meanDFromSun = meanDFromSun;
	}
	
	public String getName()
	{
		return name;
	}
	
	public double getDiameter()
	{
		return diameter;
	}
	
	public double[] getDimension()
	{
		return dimension;
	}
	
	public double getDimensionL()
	{
		return dimensionL;
	}
	
	public double getDimensionW()
	{
		return dimensionW;
	}
	
	public double getDimensionH()
	{
		return dimensionH;
	}
	
	public double getMeanDFromSun()
	{
		return meanDFromSun;
	}
	
	public void setName(String name)
	{
		this.name = name;
	}
	
	public void setDiameter(double diameter)
	{
		this.diameter = diameter;
	}
	
	public void setDimension(double[] dimension)
	{
		this.dimension = dimension;
	}
	
	public void setMeanDFromSun(double meanDFromSun)
	{
		this.meanDFromSun = meanDFromSun;
	}
}
