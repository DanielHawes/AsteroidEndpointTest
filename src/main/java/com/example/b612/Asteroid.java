package com.example.b612;

public class Asteroid
{
	private String name;
	private double diameter;
	private double[] dimension;
	private double dimensionL;
	private double dimensionW;
	private double dimensionH;
	private double meanDFromSun;
	
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
