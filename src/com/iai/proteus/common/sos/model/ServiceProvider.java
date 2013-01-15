/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;


/**
 * Represents the <ows:ServiceProvider /> part of a Capabilities document 
 * 
 * @author Jakob Henriksson
 *
 */
public class ServiceProvider {
	
	private String name; 
	private String site; 
	
	private ServiceContact contact; 
	
	/**
	 * Constructor 
	 * 
	 */
	public ServiceProvider() {
		
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the site
	 */
	public String getSite() {
		return site;
	}

	/**
	 * @param site the site to set
	 */
	public void setSite(String site) {
		this.site = site;
	}

	/**
	 * @return the contact
	 */
	public ServiceContact getContact() {
		return contact;
	}

	/**
	 * @param contact the contact to set
	 */
	public void setContact(ServiceContact contact) {
		this.contact = contact;
	}
	
	

}
