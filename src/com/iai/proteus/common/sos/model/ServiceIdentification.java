/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the <ows:ServiceIdentification /> part of a Capabilities document 
 * 
 * @author Jakob Henriksson
 *
 */
public class ServiceIdentification {

	private String title;
	private String abstractText;
	
	private List<String> keywords;
	private String serviceType; 
	private List<String> serviceTypeVersions; 
	private List<String> accessConstraints; 
	private String fees; 
	
	/**
	 * Default constructor 
	 * 
	 */
	public ServiceIdentification() {
		keywords = new ArrayList<String>();
		serviceTypeVersions = new ArrayList<String>();
		accessConstraints = new ArrayList<String>(); 
	}

	/**
	 * Returns the title 
	 * 
	 * @return
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Sets the title 
	 * 
	 * @param title
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * Returns the abstract 
	 * 
	 * @return
	 */
	public String getAbstract() {
		return abstractText;
	}

	/**
	 * Sets the abstract 
	 * 
	 * @param abstractText
	 */
	public void setAbstract(String abstractText) {
		this.abstractText = abstractText;
	}

	/**
	 * Returns the list of keywords 
	 * 
	 * @return
	 */
	public List<String> getKeywords() {
		return keywords;
	}

	/**
	 * Adds a keyword 
	 * 
	 * @param keyword
	 */
	public void addKeywords(String keyword) {
		keywords.add(keyword);
	}
	
	/**
	 * Returns the service type
	 * 
	 * @return
	 */
	public String getServiceType() {
		return serviceType;
	}
	
	/**
	 * Sets the service type 
	 * 
	 * @param serviceType
	 */
	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}
	
	/**
	 * Returns the service type versions 
	 * 
	 * @return
	 */
	public List<String> getServiceTypeVersions() {
		return serviceTypeVersions;
	}
	
	/**
	 * Adds a service type version 
	 * 
	 * @param version
	 */
	public void addServiceTypeVersion(String version) {
		serviceTypeVersions.add(version);
	}
	
	/**
	 * Returns the access constraints 
	 * 
	 * @return
	 */
	public List<String> getAccessConstraints() {
		return accessConstraints;
	}

	/**
	 * Adds an access constraint  
	 * 
	 * @param accessConstraint
	 */
	public void addAccessConstraint(String accessConstraint) {
		accessConstraints.add(accessConstraint);
	}
	
	/**
	 * Returns the fees associated with accessing the service 
	 * 
	 * @return
	 */
	public String getFees() {
		return fees;
	}
	
	/**
	 * Sets the fees associated with this service 
	 * 
	 * @param fees
	 */
	public void setFees(String fees) {
		this.fees = fees;
	}
	
}
