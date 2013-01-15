/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.parser;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Date;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import com.iai.proteus.common.TimeUtils;
import com.iai.proteus.common.Util;
import com.iai.proteus.common.sos.model.Address;
import com.iai.proteus.common.sos.model.ContactInfo;
import com.iai.proteus.common.sos.model.Operation;
import com.iai.proteus.common.sos.model.OperationsMetadata;
import com.iai.proteus.common.sos.model.Parameter;
import com.iai.proteus.common.sos.model.SensorOffering;
import com.iai.proteus.common.sos.model.ServiceContact;
import com.iai.proteus.common.sos.model.ServiceIdentification;
import com.iai.proteus.common.sos.model.ServiceProvider;
import com.iai.proteus.common.sos.model.SosCapabilities;

public class SosCapabilitiesParser extends DefaultHandler {
	
	private static final Logger log = 
		Logger.getLogger(SosCapabilitiesParser.class);	
	
	private String nsGml = "http://www.opengis.net/gml";
	private String nsOws = "http://www.opengis.net/ows/1.1";
	private String nsSos = "http://www.opengis.net/sos/1.0";
	private String nsXlink = "http://www.w3.org/1999/xlink";
	
	/*
	 * Capabilities object 
	 */
	private SosCapabilities capabilities; 
	
	private ServiceIdentification serviceIdentification;
	
	private ServiceProvider serviceProvider;
	private ServiceContact serviceContact;
	private String individualName; 
	private ContactInfo contactInfo;
	private String phone; 
	private Address address;
	
	private OperationsMetadata operationsMetadata;
	private Operation operation;
	private Parameter parameter; 
	
	private SensorOffering offering;
	
	private StringWriter sw;
	private String str;	

	/*
	 * Boolean helpers
	 */
	private boolean inOffering = false;
	private boolean inTime = false;
	private boolean endTimeIndeterminate = false; 
	private boolean inServiceIdentification = false;
	private boolean inServiceProvider = false;
	private boolean inServiceContact = false;
	private boolean inContactInfo = false;
	private boolean inAddress = false;
	private boolean inOperationsMetadata = false;
	private boolean inOperation = false;
	private boolean inParameter = false;
	private boolean noLatLong = false;
	private String timeUnits = "";
	
	
	/**
	 * Returns a Capabilities object created from parsing the XML document   
	 * 
	 * @param document 
	 */
	public SosCapabilities parseCapabilities(String document) {
		
		// check if there is a valid XML declaration 
		// TODO: needs to be case insensitive 
		if (!document.trim().startsWith("<?xml")) {
			log.warn("The Capabilities document is not a valid XML document");
			return null;
		}

		// get a factory
		SAXParserFactory spf = SAXParserFactory.newInstance();
				
		try {
			
			spf.setFeature("http://xml.org/sax/features/namespaces", true);
			
			// get a new instance of parser
			SAXParser sp = spf.newSAXParser();
			
			ByteArrayInputStream bs = 
				new ByteArrayInputStream(document.getBytes());
			
			log.info("Starting to parse Capabilities document...");
			
			// parse the document and also register this class for call backs
			sp.parse(bs, this);
			
			log.info("Done parsing Capabilities document.");			
			
			// return the capabilities object 
			return capabilities; 

		} catch(SAXException e) {
			log.error("Error parsing Capabilities document: " + e.getMessage());
		} catch(ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		log.error("Something went wrong when parsing Capabilities document");
		
		// default in case of errors 
		return null;
	}
	
	/**
	 * Start element
	 * 
	 */
	@Override
	public void startElement(String uri, String localName,String qName, 
            Attributes attributes) throws SAXException {
		
		sw = new StringWriter();
		
		String fullUri = uri != "" ? uri + "#" + localName : localName;		
	
		if (fullUri.equalsIgnoreCase(nsSos + "#Capabilities")) {
			capabilities = new SosCapabilities(); 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ServiceIdentification")) {
			serviceIdentification = new ServiceIdentification();
			inServiceIdentification = true; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ServiceProvider")) {
			serviceProvider = new ServiceProvider();
			inServiceProvider = true; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ServiceContact")) {
			inServiceContact = true; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ContactInfo")) {
			contactInfo = new ContactInfo();
			inContactInfo = true; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#OperationsMetadata")) {
			operationsMetadata = new OperationsMetadata();
			inOperationsMetadata = true; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Operation")) {
			String name = attributes.getValue("name");
			operation = new Operation(name);
			inOperation = true; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Get")) {
			if (inOperation) {
				String address = attributes.getValue(nsXlink, "href");
				operation.addServiceAddress("get", address);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Post")) {
			if (inOperation) {
				String address = attributes.getValue(nsXlink, "href");
				operation.addServiceAddress("post", address);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Parameter")) {
			String name = attributes.getValue("name");
			parameter = new Parameter(name);
			inParameter = true; 
		} else if (fullUri.equalsIgnoreCase(nsSos + "#ObservationOffering")) {
			String gmlId = attributes.getValue(nsGml, "id");
			offering = new SensorOffering(gmlId);
			inOffering = true; 
			noLatLong = false;
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ProviderSite")) {
			if (inServiceProvider) {
				String site = attributes.getValue(nsXlink, "href");
				serviceProvider.setSite(site);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Address")) {
			if (inContactInfo) {
				address = new Address(); 
				inAddress = true; 
			}
		} else if (fullUri.equalsIgnoreCase(nsSos + "#procedure")) {
			String procedure = attributes.getValue(nsXlink, "href");
			offering.addProcedure(procedure);
		} else if (fullUri.equalsIgnoreCase(nsSos + "#observedProperty")) {
			String property = attributes.getValue(nsXlink, "href");
			offering.addObservedProperty(property);
		} else if (fullUri.equalsIgnoreCase(nsSos + "#featureOfInterest")) {
			String feature = attributes.getValue(nsXlink, "href");
			offering.addFeatureOfInterest(feature);
		} else if (fullUri.equalsIgnoreCase(nsSos + "#time")) {
			if (inOffering)
				inTime = true; 
		} else if (fullUri.equalsIgnoreCase(nsGml + "#endPosition")) {
			if (inTime) {
				String value = attributes.getValue("indeterminatePosition");
				if (value != null)
					endTimeIndeterminate = true; 
			}
		} else if (fullUri.equalsIgnoreCase(nsGml + "#timeInterval")) {
			if (inTime) {
				timeUnits = attributes.getValue("unit");
			}
		} 
	}

	/**
	 * End element
	 */
	@Override
	public void endElement(String uri, String localName,
		String qName) throws SAXException 
	{
		
		str = sw.toString();
		
		String fullUri = uri != "" ? uri + "#" + localName : localName;
	
		if (fullUri.equalsIgnoreCase(nsOws + "#ServiceIdentification")) {
			capabilities.setServiceIdentification(serviceIdentification);
			inServiceIdentification = false;
		} else if (fullUri.equalsIgnoreCase(nsOws + "#OperationsMetadata")) {
			capabilities.setOperationsMetadata(operationsMetadata);
			inOperationsMetadata = false; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Operation")) {
			if (inOperationsMetadata) {
				operationsMetadata.addOperation(operation);
			}
			inOperation = false; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Operation")) {
			if (inOperationsMetadata) {
				operationsMetadata.addOperation(operation);
			}
			inOperation = false; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Parameter")) {
			if (inOperation) {
				operation.addParameter(parameter);
			}
			inParameter = false;
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Value")) {
			if (inParameter) {
				parameter.addAllowedValue(new String(str));
			}
		} else if (fullUri.equalsIgnoreCase(nsSos + "#ObservationOffering")) {
			// we loaded the offering object from the Capabilities document
			offering.loaded(); 
			// add offering to capabilities document 
			// NOTE: right now we require offerings to have LAT LONG
			if (!noLatLong)
				capabilities.addSensorOffering(offering);
			inOffering = false; 
		} else if (fullUri.equalsIgnoreCase(nsGml + "#description")) {
			offering.setDescription(str);
		} else if (fullUri.equalsIgnoreCase(nsGml + "#name")) {
			if (inOffering)
				offering.setName(str);
		} else if (fullUri.equalsIgnoreCase(nsGml + "#srsName")) {
			if (inOffering)
				offering.setSrsName(str);
		} else if (fullUri.equalsIgnoreCase(nsSos + "#responseFormat")) {
			if (inOffering)
				offering.addResponseFormat(str);
		} else if (fullUri.equalsIgnoreCase(nsSos + "#resultModel")) {
			if (inOffering)
				offering.setResultModel(str);
		} else if (fullUri.equalsIgnoreCase(nsSos + "#responseMode")) {
			if (inOffering)
				offering.setResponseMode(str);
		} else if (fullUri.equalsIgnoreCase(nsGml + "#lowerCorner")) {
			if (inOffering) {
				double latlong[] = getLatLong(str.trim()); 
				if (latlong != null) {
					offering.setLowerCornerLat(latlong[0]);
					offering.setLowerCornerLong(latlong[1]);
				} else {
					noLatLong = true;
				}
			}
		} else if (fullUri.equalsIgnoreCase(nsGml + "#upperCorner")) {
			if (inOffering) {
				double latlong[] = getLatLong(str.trim());
				if (latlong != null) {
					offering.setUpperCornerLat(latlong[0]);
					offering.setUpperCornerLong(latlong[1]);
				} else {
					noLatLong = true;					
				}
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Title")) {
			if (inServiceIdentification) {
				serviceIdentification.setTitle(Util.cleanUpString(str)); 
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Abstract")) {
			if (inServiceIdentification) {
				serviceIdentification.setAbstract(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Keyword")) {
			if (inServiceIdentification) {
				serviceIdentification.addKeywords(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ServiceType")) {
			if (inServiceIdentification) {
				serviceIdentification.setServiceType(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ServiceTypeVersion")) {
			if (inServiceIdentification) {
				serviceIdentification.addServiceTypeVersion(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#AccessConstraints")) {
			if (inServiceIdentification) {
				serviceIdentification.addAccessConstraint(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Fees")) {
			if (inServiceIdentification) {
				serviceIdentification.setFees(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ServiceProvider")) {
			serviceProvider.setContact(serviceContact);
			capabilities.setServiceProvider(serviceProvider);
			inServiceProvider = false; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ProviderName")) {
			if (inServiceProvider) {
				serviceProvider.setName(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ServiceContact")) {
			if (inServiceContact) {
				String name = "";
				if (individualName != null)
					name = individualName;
				serviceContact = new ServiceContact(name, contactInfo); 
			}
			inServiceContact = false; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#IndividualName")) {
			if (inServiceContact)
				individualName = new String(str);
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ContactInfo")) {
			if (phone != null)
				contactInfo.setPhone(phone);
			contactInfo.setAddress(address);
			inContactInfo = false; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Voice")) {
			if (inContactInfo)
				phone = new String(str);
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Address")) {
			inAddress = false; 
		} else if (fullUri.equalsIgnoreCase(nsOws + "#DeliveryPoint")) {
			if (inAddress) {
				address.setDeliveryPoint(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#City")) {
			if (inAddress) {
				address.setCity(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#AdministrativeArea")) {
			if (inAddress) {
				address.setAdministrativeArea(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#PostalCode")) {
			if (inAddress) {
				address.setPostalCode(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#Country")) {
			if (inAddress) {
				address.setCountry(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsOws + "#ElectronicMailAddress")) {
			if (inAddress) {
				address.setEmaill(str);
			}
		} else if (fullUri.equalsIgnoreCase(nsSos + "#time")) {
			inTime = false; 
		} else if (fullUri.equalsIgnoreCase(nsGml + "#beginPosition")) {
			if (inTime) {
				Date begin = TimeUtils.parseDefault(str);
				offering.setStartTime(begin);
			}
		} else if (fullUri.equalsIgnoreCase(nsGml + "#endPosition")) {
			if (inTime) {
				/*
				 * Only parse the end time if it is given 
				 */
				if (endTimeIndeterminate) {
					endTimeIndeterminate = false; 
				} else {
					Date end = TimeUtils.parseDefault(str);
					offering.setEndTime(end);
				}
			}
		} else if (fullUri.equalsIgnoreCase(nsGml + "#timeInterval")) {
			if (inTime) {
				// TODO: find out which units are valid
				double interval = 60.0 * Double.parseDouble(str);
				offering.setInterval(interval);
			}
		} 
		
	}

	/**
	 * Reading characters 
	 */
	@Override
	public void characters(char ch[], int start, int length) 
		throws SAXException 
	{
		str = new String(ch, start, length);
		sw.write(str);		
	}
	
	/**
	 * Returns an array of the lat and long given a string representation, 
	 * e.g. "16.03 -107"
	 * 
	 * @param str
	 * @return
	 */
	private double[] getLatLong(String str) {
		if (!str.equals("")) {
			double[] res = new double[2];
			String[] latlongData = str.split(" ");
			res[0] = Double.parseDouble(latlongData[0]);
			res[1] = Double.parseDouble(latlongData[1]);
			return res;
		}
		return null;
	}
}
