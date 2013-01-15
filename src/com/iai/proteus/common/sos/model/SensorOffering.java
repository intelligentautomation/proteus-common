/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

import com.iai.proteus.common.Util;

/**
 * Represents the Sensor Offering section of the SOS Capabilities document  
 *
 */
public class SensorOffering implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String gmlId;
	private String name;
	private String description;
	
	private String srsName;
	private String resultModel;
	private String responseMode;
	
	private ArrayList<String> featuresOfInterest; 
	private ArrayList<String> responseFormats;
	private ArrayList<String> procedures;
	private ArrayList<String> observedProperties;

	private Date startTime;
	private Date endTime;
	private Date latestDataPoint; 
	private double interval; // minutes 

	private double lowerCornerLat;
	private double lowerCornerLong;

	private double upperCornerLat;
	private double upperCornerLong;
	
	/*
	 * True if this object has been 'filled out' from a Capabilities document
	 */
	private boolean loaded = false;

	/**
	 * Default constructor 
	 */
	public SensorOffering() {
		featuresOfInterest = new ArrayList<String>();
		responseFormats = new ArrayList<String>();
		procedures = new ArrayList<String>(); 
		observedProperties = new ArrayList<String>(); 
	}
	
	/**
	 * Constructor
	 * 
	 * @param gmlId
	 */
	public SensorOffering(String gmlId) {
		this();
		this.gmlId = gmlId; 
	}

	/*
	 * Getters and setters
	 */
	
	public void setGmlId(String id) {
		this.gmlId = id;
	}
	
	public String getGmlId() {
		return gmlId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public void setSrsName(String srsName) {
		this.srsName = srsName;
	}
	
	public String getSrsName() {
		return srsName;
	}
	
	public void setResultModel(String resultModel) {
		this.resultModel = resultModel;
	}
	
	public String getResultModel() {
		return resultModel;
	}
	
	public void setResponseMode(String responseMode) {
		this.responseMode = responseMode;
	}
	
	public String getResponseMode() {
		return responseMode;
	}
	
	/**
	 * @return the featuresOfInterest
	 */
	public ArrayList<String> getFeaturesOfInterest() {
		return featuresOfInterest;
	}

	/**
	 * @param featuresOfInterest the featuresOfInterest to set
	 */
	public void setFeaturesOfInterest(ArrayList<String> featuresOfInterest) {
		this.featuresOfInterest = featuresOfInterest;
	}

	public void addFeatureOfInterest(String feature) {
		if (!featuresOfInterest.contains(feature))
			featuresOfInterest.add(feature); 
	}
	
	public void addResponseFormat(String format) {
		// replace all white spaces to reduce conflicts when comparing
		// available response formats with provided response formats
		String str = Util.removeWhitespaces(format);
		if (!responseFormats.contains(str))
			responseFormats.add(str);
	}

	public ArrayList<String> getResponseFormats() {
		return responseFormats;
	}
	
	/**
	 * @param responseFormats the responseFormats to set
	 */
	public void setResponseFormats(ArrayList<String> responseFormats) {
		this.responseFormats = responseFormats;
	}

	public void addProcedure(String procedure) {
		if (!procedures.contains(procedure))
			procedures.add(procedure);
	}
	
	public ArrayList<String> getProcedures() {
		return procedures;
	}
	
	/**
	 * @param procedures the procedures to set
	 */
	public void setProcedures(ArrayList<String> procedures) {
		this.procedures = procedures;
	}

	public void addObservedProperty(String property) {
		if (!observedProperties.contains(property))
			observedProperties.add(property);
	}
	
	public ArrayList<String> getObservedProperties() {
		return observedProperties;
	}
	
	/**
	 * @param observedProperties the observedProperties to set
	 */
	public void setObservedProperties(ArrayList<String> observedProperties) {
		this.observedProperties = observedProperties;
	}

	public void setStartTime(Date start) {
		this.startTime = start;
	}

	public Date getStartTime() {
		return startTime;
	}
	
	public void setEndTime(Date end) {
		this.endTime = end; 
	}

	public Date getEndTime() {
		return endTime;
	}
	
	public boolean noEndTime() {
		return endTime == null; 
	}
	
	public void setLatestDataPoint(Date latest) {
		this.latestDataPoint = latest;
	}
	
	public Date getLatestDataPoint() {
		return latestDataPoint;
	}
	
	public boolean hasLatestDataPoint() {
		return latestDataPoint != null;
	}

	public double getInterval()
	{
		return interval;
	}

	public void setInterval(double interval)
	{
		this.interval = interval;
	}

	public void setLowerCornerLat(double lowerLat) {
		lowerCornerLat = lowerLat;
	}

	public double getLowerCornerLat() {
		return lowerCornerLat;
	}

	public void setLowerCornerLong(double lowerLon) {
		lowerCornerLong = lowerLon;
	}

	public double getLowerCornerLong() {
		return lowerCornerLong;
	}

	public void setUpperCornerLat(double upperLat) {
		upperCornerLat = upperLat;
	}

	public double getUpperCornerLat() {
		return upperCornerLat;
	}

	public void setUpperCornerLong(double upperLon) {
		upperCornerLong = upperLon;
	}

	public double getUpperCornerLong() {
		return upperCornerLong;
	}

	/**
	 * Notifies that this object was loaded/instantiated from a 
	 * Capabilities document 
	 * 
	 */
	public void loaded() {
		loaded = true; 
	}
	
	/**
	 * Returns True if this object has been loaded/filled out from a 
	 * Capabilities object, False otherwise 
	 * 
	 * @return
	 */
	public boolean isLoaded() {
		return loaded; 
	}
	
	/**
	 * Load this sensor offering from a Capabilities object  
	 * 
	 */
	public void loadSensorOffering(SosCapabilities capabilities) {
		
		if (capabilities != null) {
			SensorOffering offering = capabilities.getOfferingById(getGmlId());
			if (offering != null) {
				// set all the attributes 
				setName(offering.getName());
				setDescription(offering.getDescription());
				setSrsName(offering.getSrsName());
				setResultModel(offering.getResultModel());
				setResponseMode(offering.getResponseMode());
				for (String feature : offering.getFeaturesOfInterest()) 
					addFeatureOfInterest(feature);
				for (String format : offering.getResponseFormats())
					addResponseFormat(format);
				for (String procedure : offering.getProcedures())
					addProcedure(procedure);
				for (String property : offering.getObservedProperties())
					addObservedProperty(property);
				setStartTime(offering.getStartTime());
				if (!noEndTime())
					setEndTime(offering.getEndTime()); 
				setLowerCornerLat(offering.getLowerCornerLat());
				setLowerCornerLong(offering.getLowerCornerLong());
				setUpperCornerLat(offering.getUpperCornerLat());
				setUpperCornerLong(offering.getUpperCornerLong());
				// mark that it is loaded
				loaded();
			}
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((gmlId == null) ? 0 : gmlId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SensorOffering other = (SensorOffering) obj;
		if (gmlId == null) {
			if (other.gmlId != null)
				return false;
		} else if (!gmlId.equals(other.gmlId))
			return false;
		return true;
	}
	
}
