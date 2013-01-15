/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

/**
 * Represents the <ows:ServiceContact /> part of a Capabilities document 
 * 
 * 
 * @author Jakob Henriksson
 *
 */
public class ServiceContact {
	
	private String name;
	private ContactInfo contactInfo;
	
	/**
	 * Constructor 
	 * 
	 * @param name
	 * @param info
	 */
	public ServiceContact(String name, ContactInfo info) {
		this.name = name;
		this.contactInfo = info;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the contactInfo
	 */
	public ContactInfo getContactInfo() {
		return contactInfo;
	}
	
}
