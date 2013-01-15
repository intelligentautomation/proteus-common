/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.util;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import com.iai.proteus.common.LatLon;
import com.iai.proteus.common.Util;
import com.iai.proteus.common.sos.GetCapabilities;
import com.iai.proteus.common.sos.GetObservation;
import com.iai.proteus.common.sos.SosCapabilitiesCache;
import com.iai.proteus.common.sos.SosService;
import com.iai.proteus.common.sos.SupportedResponseFormats;
import com.iai.proteus.common.sos.data.CSVData;
import com.iai.proteus.common.sos.data.SensorData;
import com.iai.proteus.common.sos.exception.ExceptionReportException;
import com.iai.proteus.common.sos.model.Address;
import com.iai.proteus.common.sos.model.ContactInfo;
import com.iai.proteus.common.sos.model.GetObservationRequest;
import com.iai.proteus.common.sos.model.Operation;
import com.iai.proteus.common.sos.model.OperationsMetadata;
import com.iai.proteus.common.sos.model.SensorOffering;
import com.iai.proteus.common.sos.model.ServiceContact;
import com.iai.proteus.common.sos.model.ServiceIdentification;
import com.iai.proteus.common.sos.model.ServiceProvider;
import com.iai.proteus.common.sos.model.SosCapabilities;

public class SosUtil {

	private static final Logger log = Logger.getLogger(SosUtil.class);

	/**
	 * Returns the GET service URL for the given service in the given
	 * Capabilities object, if it exists, null otherwise
	 *
	 * @param capabilities
	 * @param service
	 * @return
	 */
	public static String findGetServiceUrl(SosCapabilities capabilities,
			SosService service)
	{

		if (capabilities == null)
			return null;

		OperationsMetadata metadata =
			capabilities.getOperationsMetadata();

		String url = null;
		for (Operation operation : metadata.getOperations()) {
			if (operation.getName().equalsIgnoreCase(service.toString())) {
				url = operation.getGet();
				break;
			}
		}

		return url;
	}

	/**
	 * Returns the list of response formats we support of the ones provided
	 * by the given offering, empty list if none.
	 *
	 * @param offering
	 *
	 * @return
	 */
	public static List<String> commonResponseFormats(SensorOffering offering) {
		List<String> formats = new ArrayList<String>();
		List<String> availableFormats = offering.getResponseFormats();
		for (SupportedResponseFormats f : SupportedResponseFormats.values()) {
			String format = f.toString();
			if (availableFormats.contains(format))
				formats.add(format);
		}
		return formats;
	}

	/**
	 * Returns a Capabilities object for this source
	 *
	 * @param serviceUrl
	 *
	 * @return
	 */
	public static SosCapabilities getCapabilities(String serviceUrl) {
		return getCapabilities(serviceUrl, false);
	}

	/**
	 * Returns a Capabilities object for this source
	 *
	 * @param serviceUrl
	 * @param refresh True if we should not use the cache
	 *
	 * @return
	 */
	public static SosCapabilities getCapabilities(String serviceUrl, boolean refresh) {

		if (serviceUrl != null) {
			SosCapabilitiesCache cache = SosCapabilitiesCache.getInstance();
			if (cache.has(serviceUrl) && !refresh) {
				return cache.get(serviceUrl);
			} else {

				if (refresh)
					log.info("The Capabilities document is being refreshed " +
							"for service: " + serviceUrl);
				else
					log.warn("The Capabilities cache did not have a resource " +
							"for service: " + serviceUrl);

				String contents = getCapabilitiesDocument(serviceUrl);

				SosCapabilities capabilities =
					GetCapabilities.parseCapabilitiesDocument(contents);

				if (capabilities != null) {

					// commit to cache
					cache.commit(contents, capabilities);

					return capabilities;
				}

				log.warn("Capabilities object was null");
				return null;
			}
		}

		log.error("There was no service URL associated with the source");
		return null;
	}

	/**
	 * Retrieves observations
	 *
	 * @param service
	 * @param sensorOffering
	 * @param timeoutConnection
	 * @param timeoutRead
	 * @param request
	 * @throws {@link ExceptionReportException}
	 * @throws {@link SocketTimeoutException}
	 */
	public static SensorData getObservationData(String service,
			GetObservationRequest request, int timeoutConnection, int timeoutRead)
					throws ExceptionReportException, SocketTimeoutException
	{
		String result = getObservationResponse(service, request,
				timeoutConnection, timeoutRead);

		return parseObservationDataFromResponse(request, result);
	}

	/**
	 * Handles the parsing of sensor data from a given response format
	 *
	 * @param request
	 * @param doc
	 * @return
	 * @throws ExceptionReportException
	 */
	public static SensorData parseObservationDataFromResponse(GetObservationRequest request,
			String doc) throws ExceptionReportException
	{
		String responseFormat = request.getResponseFormat();

		if (doc != null) {

			if (doc.contains("ExceptionReport")) {

				log.error("Exception report: " + "===\n" + doc + "\n===\n");

				throw new ExceptionReportException(doc);

			} else {

				/*
				 * Handle response
				 */

				SupportedResponseFormats format =
					SupportedResponseFormats.parse(responseFormat);
				switch (format) {
				case CSV:
					CSVData csv = CSVData.parse(doc);
					if (csv != null)
						return csv;
					break;
					
				// TODO: add support for more data formats 
				}

			}
		}

		log.error("We did not properly handle the response");

		return null;
	}

	/**
	 *
	 * @param service
	 * @param request
	 * @param timeoutConnection
	 * @param timeoutRead
	 * @return
	 * @throws SocketTimeoutException
	 */
	public static String getObservationResponse(String service,
			GetObservationRequest request, int timeoutConnection, int timeoutRead)
					throws SocketTimeoutException
	{
	
		String result = null;

		try {

			if (request.getMethod().equals("POST")) {

				log.info("Issuing a POST GetObservation request");

				result =
						GetObservation.getObservation(service, request,
								timeoutConnection, timeoutRead);

			} else {

				String serviceAddress =	service +
						(service.endsWith("?") ? "" : "?") +
						request.getGetQueryString();

				log.info("Issuing a GET GetObservation request: " + serviceAddress);

				result = Util.get(serviceAddress, timeoutConnection, timeoutRead);
			}

		} catch (MalformedURLException e) {
			log.error("Malformed URL: " + e.getMessage());
		} catch (SocketTimeoutException e) {
			throw e; 
		} catch (IOException e) {
			log.error("IOException: " + e.getMessage());
		}	
		
		return result;
	}

	/**
	 * Returns the Capabilities document from the service
	 *
	 * @param serviceUrl
	 * @return
	 */
	public static String getCapabilitiesDocument(String serviceUrl) {

		try {

			log.trace("Fetching Capabilities document from: " + serviceUrl);

			return GetCapabilities.getDocument(serviceUrl);

		} catch (SocketTimeoutException e) {
			log.warn("GetCapabilities request timed out: " + e.getMessage());
		} catch (IOException e) {
			log.error("IOException: " + e.getMessage());
		} catch (SAXException e) {
			log.error("SAX Exception: " + e.getMessage());
		}

		return null;
	}

	/**
	 * Returns the service title from the Capabilities document if
	 * there is one, otherwise null
	 *
	 * @param capabilities
	 * @return
	 */
	public static String getServiceTitle(SosCapabilities capabilities) {
		if (capabilities != null) {
			ServiceIdentification identification =
				capabilities.getServiceIdentification();
			if (identification != null)
				return identification.getTitle();
		}
		// default
		return null;
	}

	/**
	 * Returns the service provider name from the Capabilities document if
	 * there is one, otherwise null
	 *
	 * @param capabilities
	 * @return
	 */
	public static String getServiceProviderName(SosCapabilities capabilities) {
		if (capabilities != null) {
			ServiceProvider provider = capabilities.getServiceProvider();
			if (provider != null)
				return provider.getName();
		}
		// default
		return null;
	}

	/**
	 * Returns the service provider name from the Capabilities document if
	 * there is one, otherwise null
	 *
	 * @param capabilities
	 * @return
	 */
	public static String getServiceProviderSite(SosCapabilities capabilities) {
		if (capabilities != null) {
			ServiceProvider provider = capabilities.getServiceProvider();
			if (provider != null)
				return provider.getSite();
		}
		// default
		return null;
	}

	/**
	 * Returns the service contact name from the Capabilities document if
	 * there is one, otherwise null
	 *
	 * @param capabilities
	 * @return
	 */
	public static String getServiceContactName(SosCapabilities capabilities) {
		if (capabilities != null) {
			ServiceProvider provider = capabilities.getServiceProvider();
			if (provider != null) {
				ServiceContact contact = provider.getContact();
				if (contact != null) {
					return contact.getName();
				}
			}
		}
		// default
		return null;
	}

	/**
	 * Returns the service contact phone number from the Capabilities document
	 * if there is one, otherwise null
	 *
	 * @param capabilities
	 * @return
	 */
	public static String getServiceContactPhone(SosCapabilities capabilities) {
		if (capabilities != null) {
			ServiceProvider provider = capabilities.getServiceProvider();
			if (provider != null) {
				ServiceContact contact = provider.getContact();
				if (contact != null) {
					ContactInfo contactInfo = contact.getContactInfo();
					if (contactInfo != null)
						return contactInfo.getPhone();
				}
			}
		}
		// default
		return null;
	}

	/**
	 * Returns the service contact email from the Capabilities document
	 * if there is one, otherwise null
	 *
	 * @param capabilities
	 * @return
	 */
	public static String getServiceContactEmail(SosCapabilities capabilities) {
		if (capabilities != null) {
			ServiceProvider provider = capabilities.getServiceProvider();
			if (provider != null) {
				ServiceContact contact = provider.getContact();
				if (contact != null) {
					ContactInfo contactInfo = contact.getContactInfo();
					if (contactInfo != null) {
						Address address = contactInfo.getAddress();
						if (address != null) {
							return address.getEmaill();
						}
					}
				}
			}
		}
		// default
		return null;
	}

	/**
	 * Returns the lower corner position of the bounding box if it
	 * is available, false otherwise
	 *
	 * @param offering
	 * @return
	 */
	public static LatLon getLowerCornerBoundingBox(SensorOffering offering) {
		if (offering.isLoaded()) {
			Double lowerLat = offering.getLowerCornerLat();
			Double lowerLong = offering.getLowerCornerLong();
			return new LatLon(lowerLat, lowerLong);
		}
		// default
		return null;
	}

	/**
	 * Returns the upper corner position of the bounding box if it
	 * is available, false otherwise
	 *
	 * @param offering
	 * @return
	 */
	public static LatLon getUpperCornerBoundingBox(SensorOffering offering) {
		if (offering.isLoaded()) {
			Double upperLat = offering.getUpperCornerLat();
			Double upperLong = offering.getUpperCornerLong();
			return new LatLon(upperLat, upperLong);
		}
		// default
		return null;
	}

	/**
	 * Returns the bounding box description of an offering as a string
	 *
	 * @param offering
	 * @return
	 */
	public static String getBoundingBox(SensorOffering offering) {
		LatLon upper = getUpperCornerBoundingBox(offering);
		LatLon lower = getLowerCornerBoundingBox(offering);
		if (upper != null && lower != null) {
			return "Upper: " + upper + "; Lower: " + lower;
		}
		// default
		return "";
	}
}
