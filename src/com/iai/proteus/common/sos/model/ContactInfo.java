/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;


/**
 * Represents the <ows:ContactInfo /> part of a Capabilities document 
 * 
 * @author Jakob Henriksson
 *
 */
public class ContactInfo {

	private String phone; 
	private Address address; 
	
	/**
	 * Constructor 
	 * 
	 */
	public ContactInfo() {
		
	}

	/**
	 * @return the phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone the phone to set
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return the address
	 */
	public Address getAddress() {
		return address;
	}

	/**
	 * @param address the address to set
	 */
	public void setAddress(Address address) {
		this.address = address;
	}
	
	
}
