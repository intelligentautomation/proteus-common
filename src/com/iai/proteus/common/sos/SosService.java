/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos;

public enum SosService {
	
	GET_CAPABILITIES("GetCapabilities"), 
	GET_OBSERVATION("GetObservation");
	
	private String service;
	
	private SosService(String service) {
		this.service = service;
	}
	
	@Override
	public String toString() {
		return service;
	}

}
