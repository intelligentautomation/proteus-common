/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


import org.apache.log4j.Logger;

import com.iai.proteus.common.sos.model.SosCapabilities;
import com.iai.proteus.common.sos.util.SosUtil;


/**
 * Singleton for holding Capabilities documents cache 
 * 
 * @author Jakob Henriksson 
 *
 */
public class SosCapabilitiesCache implements Iterable<String> {
	
	private static final Logger log = 
		Logger.getLogger(SosCapabilitiesCache.class);		

	// maps URL to Capabilities document 
	private Map<String, String> documents; 

	// maps URL to Capabilities object 
	private Map<String, SosCapabilities> cache; 
	
	/**
	 * Constructor 
	 * 
	 */
	public SosCapabilitiesCache() {
		cache = new HashMap<String, SosCapabilities>();
		documents = new HashMap<String, String>();
	}
	
	/**
	 * Adds a Capabilities object to this cache 
	 * 
	 * @param document 
	 * @param capabilities
	 */
	public synchronized void commit(String document, SosCapabilities capabilities) {
		
		if (capabilities != null) {
			String url =  
					SosUtil.findGetServiceUrl(capabilities, SosService.GET_CAPABILITIES);
			if (url != null) {
				cache.put(url, capabilities);
				documents.put(url, document);
				log.info("Committing Capabilities documents to cache");
			} else {
				log.error("No GET service URL was found, capabilities " + 
						"not added to cache");
			}
		}
	}
	
	/**
	 * Returns a Capabilities object if it exists, null otherwise 
	 * 
	 * @param getServiceURL
	 * @return
	 */
	public synchronized SosCapabilities get(String getServiceURL) {
		return cache.get(getServiceURL);
	}
	
	/**
	 * Returns true if the cache has the Capabilities object, false otherwise
	 * 
	 * @param getServiceURL
	 * @return
	 */
	public synchronized boolean has(String getServiceURL) {
		return cache.containsKey(getServiceURL);
	}
	
	/**
	 * Returns true if the cache has the original Capabilities document, 
	 * false otherwise 
	 * 
	 * @param getServiceURL
	 * @return
	 */
	public synchronized boolean hasDocument(String getServiceURL) {
		return documents.containsKey(getServiceURL);
	}
	
	/**
	 * Returns the Capabilities document if it exists, null otherwise  
	 * 
	 * @param getServiceURL
	 * @return
	 */
	public synchronized String getDocument(String getServiceURL) {
		return documents.get(getServiceURL);
	}
	
	/**
	 * Returns the size of the cache 
	 * 
	 * @return
	 */
	public synchronized int size() {
		return cache.size();
	}
	
	/**
	 * Singleton holder 
	 * 
	 */
	private static class SingletonHolder {
		public static final SosCapabilitiesCache INSTANCE = 
			new SosCapabilitiesCache();
	}

	public static SosCapabilitiesCache getInstance() {
		return SingletonHolder.INSTANCE;
	}
	
	@Override
	public Iterator<String> iterator() {
		return cache.keySet().iterator();
	}

}
