/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a SOS Capabilities document 
 * 
 * @author Jakob Henriksson
 */
public class SosCapabilities {

	/*
	 * The list of offerings contained in the Capabilities document 
	 */
	private List<SensorOffering> offerings; 
	
	/*
	 * Service identification 
	 */
	private ServiceIdentification serviceIdentification; 
	
	/*
	 * Service Provider
	 */
	private ServiceProvider serviceProvider; 
	
	/*
	 * Operations Metadata
	 */
	private OperationsMetadata operationsMetadata; 
	
	/**
	 * Constructor 
	 * 
	 */
	public SosCapabilities() {
		offerings = new ArrayList<SensorOffering>();
	}

	public void addSensorOffering(SensorOffering offering) {
		offerings.add(offering);
	}
	
	/**
	 * Returns the Offering with the given ID
	 * 
	 * @param id
	 * @return
	 */
	public SensorOffering getOfferingById(String id) {
		for (SensorOffering offering : offerings) {
			if (offering.getGmlId().equals(id))
				return offering;
		}
		// default
		return null;
	}
	
	/**
	 * Returns the Offering with the given Name
	 * 
	 * @param name
	 * @return
	 */
	public SensorOffering getOfferingByName(String name) {
		for (SensorOffering offering : offerings) {
			if (offering.getName().equals(name))
				return offering;
		}
		// default
		return null;
	}	
	
	/**
	 * Returns all offerings 
	 * 
	 * @return
	 */
	public List<SensorOffering> getOfferings() {
		return offerings; 
	}
	
	/**
	 * Set the Service Identification object
	 * 
	 * @param service
	 */
	public void setServiceIdentification(ServiceIdentification service) {
		this.serviceIdentification = service; 
	}
	
	/**
	 * Returns the Service Identification object 
	 * 
	 * @return
	 */
	public ServiceIdentification getServiceIdentification() {
		return serviceIdentification;
	}
	
	/**
	 * Set the Service Provider object 
	 * 
	 * @param provider
	 */
	public void setServiceProvider(ServiceProvider provider) {
		this.serviceProvider = provider;
	}
	
	/**
	 * Returns the Service Provider object
	 *  
	 * @return
	 */
	public ServiceProvider getServiceProvider() {
		return serviceProvider;
	}
	
	/**
	 * Sets the Operations Metadata object 
	 * 
	 * @param metadata
	 */
	public void setOperationsMetadata(OperationsMetadata metadata) {
		this.operationsMetadata = metadata;
	}
	
	/**
	 * Returns the Operations Metadata object 
	 * 
	 * @return
	 */
	public OperationsMetadata getOperationsMetadata() {
		return operationsMetadata;
	}	
}
