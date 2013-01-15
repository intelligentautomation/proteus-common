/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the <ows:Parameter /> part of a Capabilities document 
 * 
 * @author Jakob Henriksson
 *
 */
public class Parameter {
	
	private String name;
	private List<String> allowedValues; 
	
	/**
	 * Constructor 
	 * 
	 * @param name
	 */
	public Parameter(String name) {
		this.name = name;
		allowedValues = new ArrayList<String>(); 
	}
	
	public String getName() {
		return name;
	}
	
	public void addAllowedValue(String value) {
		allowedValues.add(value);
	}
	
	public List<String> getAllowedValues() {
		return allowedValues;
	}

}
