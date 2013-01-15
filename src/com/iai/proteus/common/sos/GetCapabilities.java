/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;


import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.iai.proteus.common.Util;
import com.iai.proteus.common.sos.model.SosCapabilities;
import com.iai.proteus.common.sos.parser.SosCapabilitiesParser;

public class GetCapabilities {

	private static final Logger log =
		Logger.getLogger(GetCapabilities.class);

	/**
	 * Executes a GetCapabilities method call on given service address and
	 * returns the resulting document, or null if there is an error
	 *
	 * @param serviceAddress
	 * @return
	 */
	public static String getDocument(String serviceAddress)
		throws SAXException, UnknownHostException, IOException, SocketTimeoutException
	{
		// use default values 
		return getDocument(serviceAddress, 10, 60);
	}
	
	/**
	 * Executes a GetCapabilities method call on given service address and
	 * returns the resulting document, or null if there is an error
	 *
	 * @param serviceAddress
	 * @param timeoutConnection
	 * @param timeoutRead 
	 * @return
	 */
	public static String getDocument(String serviceAddress, 
			int timeoutConnection, int timeoutRead)
		throws SAXException, UnknownHostException, IOException, SocketTimeoutException
	{

		final String getParams =
			"?service=SOS&request=GetCapabilities&version=1.0.0";

		try {

			/* adds the required parameters that specifies service (SOS)
			 * and the actual request (GetCapabilities)
			 */
			serviceAddress += getParams;

			return Util.get(serviceAddress, timeoutConnection, timeoutRead);

		} catch (MalformedURLException e) {
			log.error("Malformed URL Exception: " + e.getMessage());
		}

		// default
		return null;
	}	

	/**
	 * Returns a Capabilities object given a string containing the document
	 *
	 * @param document
	 * @return
	 */
	public static SosCapabilities
		parseCapabilitiesDocument(String document)
	{
		if (document != null) {

			return new SosCapabilitiesParser().parseCapabilities(document);
		}

		log.warn("The Capabilities document to parse was null");

		// default
		return null;
	}
}
