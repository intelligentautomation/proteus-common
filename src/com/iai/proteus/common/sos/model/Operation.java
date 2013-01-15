/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Represents the <ows:Operation /> part of a Capabilities document 
 * 
 * @author Jakob Henriksson
 *
 */
public class Operation {
	
	private String name;
	private Map<String, String> addresses; 
	private List<Parameter> parameters; 
	
	/**
	 * Constructor 
	 * 
	 * @param name
	 */
	public Operation(String name) {
		this.name = name;
		this.addresses = new HashMap<String, String>(); 
		this.parameters = new ArrayList<Parameter>(); 
	}
	
	public String getName() {
		return name;
	}
	
	/**
	 * Adds an address
	 * 
	 * @param method
	 * @param address
	 */
	public void addServiceAddress(String method, String address) {
		addresses.put(method.toLowerCase(), address); 
	}
	
	public boolean hasGet() {
		return addresses.containsKey("get");
	}

	public boolean hasPost() {
		return addresses.containsKey("post");
	}
	
	public String getGet() {
		return addresses.get("get");
	}
	
	public String getPost() {
		return addresses.get("post"); 
	}
	
	public void addParameter(Parameter parameter) {
		parameters.add(parameter); 
	}
	
	public List<Parameter> getParameters() {
		return parameters; 
	}

}
