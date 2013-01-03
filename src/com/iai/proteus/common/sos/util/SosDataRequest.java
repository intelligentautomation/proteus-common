package com.iai.proteus.common.sos.util;

import com.iai.proteus.common.sos.model.GetObservationRequest;

/**
 * An object holding information required to issue a data request to an 
 * SOS instance. 
 * 
 * We need:
 * 
 * - The SOS source (the service)
 * - The GetObservation request  
 * 
 * @author Jakob Henriksson
 *
 */
public class SosDataRequest {

	private String label; 
	
	private String serviceUrl;
	private GetObservationRequest request; 
	
	private boolean active; 
	
	/**
	 * Constructor 
	 * 
	 * @param label 
	 * @param serviceUrl
	 * @param request
	 */
	public SosDataRequest(String label, String serviceUrl, 
			GetObservationRequest request) 
	{
		this.label = label;
		this.serviceUrl = serviceUrl;
		this.request = request;
		// default 
		active = true;
	}

	/**
	 * Returns the label for this request
	 * 
	 * @return
	 */
	public String getLabel() {
		return label;
	}
	
	/**
	 * Sets the label for the request 
	 * 
	 * @param label
	 */
	public void setLabel(String label) {
		this.label = label;
	}
	
	/**
	 * Returns the source (service) for this data request 
	 * 
	 * @return the source
	 */
	public String getServiceUrl() {
		return serviceUrl;
	}

	/**
	 * Returns the GetObservation request 
	 * 
	 * @return the request
	 */
	public GetObservationRequest getRequest() {
		return request;
	}
	
	/**
	 * Marks this data request as active 
	 * 
	 */
	public void activate() {
		active = true;
	}
	
	/**
	 * Marks this data request as inactive 
	 */
	public void deactivate() {
		active = false; 
	}
	
	/**
	 * Returns true if this data request is considered active
	 * 
	 * @return
	 */
	public boolean isActive() {
		return active; 
	}
}
