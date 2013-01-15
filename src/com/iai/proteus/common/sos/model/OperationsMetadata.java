/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents the <ows:OperationsMetadata /> part of a Capabilities document 
 * 
 * @author Jakob Henriksson
 *
 */
public class OperationsMetadata {
	
	private List<Operation> operations;
	
	/**
	 * Constructor 
	 * 
	 */
	public OperationsMetadata() {
		operations = new ArrayList<Operation>(); 
	}
	
	public void addOperation(Operation operation) {
		operations.add(operation);
	}
	
	public List<Operation> getOperations() {
		return operations; 
	}
	

}
