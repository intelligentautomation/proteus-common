/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.io.Serializable;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.iai.proteus.common.TimeUtils;

/**
 * Represents a GetObservation request
 * 
 * @author Jakob Henriksson
 * 
 */
public class GetObservationRequest implements Serializable, Cloneable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger log = Logger.getLogger(GetObservationRequest.class);
	
	private String method; 

	private SensorOffering sensorOffering;
	private String observedProperty;
	private String responseFormat;
	
	/*
	 * These intervals are mainly used, but are not serialized 
	 */
	private ArrayList<TimeInterval> timeIntervals;
	
	/**
	 * Default constructor 
	 * 
	 */
	public GetObservationRequest() {
		// defaults 
		method = "GET";
		timeIntervals = new ArrayList<TimeInterval>();
	}
	
	/**
	 * Constructor 
	 * 
	 * @param offering
	 * @param observedProperty
	 * @param responseFormat
	 */
	public GetObservationRequest(SensorOffering offering, 
			String observedProperty, String responseFormat) 
	{
		this(); 
		this.sensorOffering = offering;
		this.observedProperty = observedProperty; 
		this.responseFormat = responseFormat;
	}
	
	/**
	 * Returns the sensor offering that this request is intended for 
	 * 
	 * @return
	 */
	public SensorOffering getSensorOffering() {
		return sensorOffering;
	}
	
	/**
	 * Sets the sensor offering 
	 * 
	 * @param sensorOffering
	 */
	public void setSensorOffering(SensorOffering sensorOffering) {
		this.sensorOffering = sensorOffering;
	}
	
	/**
	 * Returns the observed property
	 * 
	 * @return
	 */
	public String getObservedProperty() {
		return observedProperty;
	}
	
	/**
	 * Changes the observed property 
	 * 
	 * @param observedProperty 
	 * @return
	 */
	public void setObservedProperty(String observedProperty) {
		this.observedProperty = observedProperty;
	}	
		
	/**
	 * Sets the response format 
	 * 
	 * @param format
	 */
	public void setResponseFormat(String format) {
		this.responseFormat = format;
	}
	
	/**
	 * Returns the response format 
	 * 
	 * @return
	 */
	public String getResponseFormat() {
		return responseFormat;
	}
	
	/**
	 * Adds a time interval for this request 
	 * 
	 * @param interval
	 */
	public void addTimeInterval(TimeInterval interval) {
		timeIntervals.add(interval);
	}
	
	/**
	 * Removes all time intervals specified for this request  
	 * 
	 */
	public void clearIntervals() {
		timeIntervals.clear();
	}
	
	/**
	 * Returns the collection of time intervals specified for this request
	 * 
	 * @return
	 */
	public ArrayList<TimeInterval> getTimeIntervals() {
		return timeIntervals;
	}
	
	
	/**
	 * Set the time intervals 
	 * 
	 * @param timeIntervals the timeIntervals to set
	 */
	public void setTimeIntervals(ArrayList<TimeInterval> timeIntervals) {
		this.timeIntervals = timeIntervals;
	}
	
	/**
	 * @return the method
	 */
	public String getMethod() {
		return method;
	}

	/**
	 * @param method the method to set
	 */
	public void setMethod(String method) {
		this.method = method;
	}

	/**
	 * Returns a GET query string for this request  
	 * 
	 * @return
	 */
	public String getGetQueryString() {
		
		try {
		
			String base = "service=SOS&version=1.0.0&request=GetObservation";

			String query = 
				"responseFormat=" + 
				URLEncoder.encode(responseFormat, "UTF-8") + 
				"&offering=" + 
				URLEncoder.encode(sensorOffering.getName(), "UTF-8") + 
				"&observedProperty=" + 
				URLEncoder.encode(observedProperty, "UTF-8"); 
			
			// time intervals 
			for (TimeInterval interval : timeIntervals) {
				String eventTime = "";
				eventTime += TimeUtils.format(interval.getStart());
				Date end = interval.getEnd();
				if (end != null) { 
					eventTime += "/";
					eventTime += TimeUtils.format(end);
				}
				query += "&eventtime=" + URLEncoder.encode(eventTime, "UTF-8");
				// NOTE: we currently only handle one time interval
				break;
			}			
				
			return base + "&" + query;
		
		} catch (UnsupportedEncodingException e) {
			log.warn("Unsupported encoding: " + e.getMessage());
		}
		
		return null;
	}
	
	/**
	 * Returns this GetObservation as an XML request 
	 * 
	 * @return
	 */
	public String getXmlRequest() {
		
		try {

	        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	        DocumentBuilder builder = factory.newDocumentBuilder();
	        Document document = builder.newDocument();  
	        
	        // create the actual request 
	        Element root = createXmlRequest(document, true); 
	        document.appendChild(root);
	        
			// set up a transformer
	        TransformerFactory transfac = TransformerFactory.newInstance();
	        Transformer trans = transfac.newTransformer();
	        trans.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "no");
	        trans.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
	        trans.setOutputProperty(OutputKeys.VERSION, "1.0");
	        trans.setOutputProperty(OutputKeys.INDENT, "yes");
	        trans.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", 
	        		"2");            
	
			// write XML tree to String
	        StringWriter sw = new StringWriter(); 
			StreamResult result = new StreamResult(sw);
			DOMSource source = new DOMSource(document);
			trans.transform(source, result);
	
			return sw.toString();
		
		} catch (ParserConfigurationException e) {
			log.error("Parser configuration error while serializing layers: " +
					e.getMessage()); 
		} catch (TransformerException e) {
			log.error("Error while serializing layers: " + e.getMessage());
		}
		
		// default
		return null; 
	}
	
	/**
	 * Creates the GetObservation XML request and returns the root element 
	 * 
	 * @param document
	 * @param ns True if attributes should be included  
	 * @return
	 */
	public Element createXmlRequest(Document document, boolean ns) {
		
		Element root = createXmlRequestRootElement(document, ns); 
		
		// offering 
		Element elmtOffering = serializeOffering(document);
		root.appendChild(elmtOffering);
		
		// time periods
		// TODO: verify how this should be done for multiple time periods 
		for (TimeInterval interval : timeIntervals) {
			Element elmtEventTime = serializeEventTime(document, interval); 
			root.appendChild(elmtEventTime);
		}
		
		// observed property
		Element elmtObservedProperty = serializeObservedProperty(document);
		root.appendChild(elmtObservedProperty);
		
		// response format 
		Element elmtResponseFormat = serializeResponseFormat(document);
		root.appendChild(elmtResponseFormat);

		// result model 		
		Element elmtResultModel = serializeResultModel(document);
		root.appendChild(elmtResultModel);
		
		// response mode 
		Element elmtResponseMode = serializeResponseMode(document);
		root.appendChild(elmtResponseMode);
		
		return root; 
	}
	
	/**
	 * Constructs the GetObservation request root element 
	 * 
	 * @param document
	 * @param ns True if attributes should be included 
	 * @return
	 */
	private Element createXmlRequestRootElement(Document document, boolean ns) {
		Element root = 
			document.createElementNS("http://www.opengis.net/sos/1.0", 
					"sos:GetObservation");
		// name spaces
		if (ns) {
			root.setAttribute("xmlns:xsi", 
					"http://www.w3.org/2001/XMLSchema-instance");
			root.setAttribute("xsi:schemaLocation", 
					"http://www.opengis.net/sos/1.0 " + 
					"http://schemas.opengis.net/sos/1.0.0/sosAll.xsd"); 
			root.setAttribute("xmlns:sos", "http://www.opengis.net/sos/1.0");
			root.setAttribute("xmlns:om", "http://www.opengis.net/om/1.0");
			root.setAttribute("xmlns:ogc", "http://www.opengis.net/ogc");
			root.setAttribute("xmlns:gml", "http://www.opengis.net/gml/3.2");
		}

		root.setAttribute("service", "SOS");
		root.setAttribute("version", "1.0.0");
		
		return root; 
	}

	/**
	 * Serializes the offering parameter 
	 * 
	 * @param document
	 * @return
	 */
	private Element serializeOffering(Document document) {
		Element elmtOffering = 
			document.createElementNS("http://www.opengis.net/sos/1.0", 
					"sos:offering");
		elmtOffering.setTextContent(sensorOffering.getName());
		return elmtOffering;
	}
	
	/**
	 * Serializes the event time parameter  
	 * 
	 * @param document
	 * @return
	 */
	private Element serializeEventTime(Document document, TimeInterval interval) {
		Element elmtEventTime = 
			document.createElementNS("http://www.opengis.net/sos/1.0", 
					"sos:eventTime");
		
		Element elmtTMDuring = 
			document.createElementNS("http://www.opengis.net/ogc", 
					"ogc:TM_During");
		elmtEventTime.appendChild(elmtTMDuring);
		
		Element elmtPropertyName = 
			document.createElementNS("http://www.opengis.net/ogc", 
					"ogc:PropertyName");
		elmtPropertyName.setTextContent("urn:ogc:data:time:iso8601");
//		elmtPropertyName.setTextContent("om:samplingTime");
		elmtTMDuring.appendChild(elmtPropertyName);
		
		Element elmtTimePeriod = 
			document.createElementNS("http://www.opengis.net/gml/3.2", 
					"gml:TimePeriod");
		elmtTMDuring.appendChild(elmtTimePeriod);
		
		Element elmtBeingPosition = 
			document.createElementNS("http://www.opengis.net/gml/3.2", 
					"gml:beginPosition");
		String begin = TimeUtils.format(interval.getStart()); 
		elmtBeingPosition.setTextContent(begin); 
		elmtTimePeriod.appendChild(elmtBeingPosition);
		
		Element elmtEndPosition = 
			document.createElementNS("http://www.opengis.net/gml/3.2", 
					"gml:endPosition");
		String end = TimeUtils.format(interval.getEnd()); 
		elmtEndPosition.setTextContent(end); 
		elmtTimePeriod.appendChild(elmtEndPosition);		
		
		return elmtEventTime;
	}	
	
	/**
	 * Serializes the observed property parameters 
	 * 
	 * @param document
	 * @return
	 */
	private Element serializeObservedProperty(Document document) {
		Element elmtObservedProperty = 
			document.createElementNS("http://www.opengis.net/sos/1.0", 
					"sos:observedProperty");
		elmtObservedProperty.setTextContent(this.observedProperty);
		return elmtObservedProperty;
	}
	
	/**
	 * Serializes the observed property parameter 
	 * 
	 * @param document
	 * @return
	 */
	private Element serializeResponseFormat(Document document) {
		Element elmtResponseFormat = 
			document.createElementNS("http://www.opengis.net/sos/1.0", 
					"sos:responseFormat");
		elmtResponseFormat.setTextContent(responseFormat);
		return elmtResponseFormat;
	}
	
	/**
	 * Serializes the result model parameter 
	 * 
	 * @param document
	 * @return
	 */
	private Element serializeResultModel(Document document) {
		Element elmtObservedProperty = 
			document.createElementNS("http://www.opengis.net/sos/1.0", 
					"sos:resultModel");
		elmtObservedProperty.setTextContent("om:Observation");
		return elmtObservedProperty;
	}	

	/**
	 * Serializes the response mode parameter 
	 * 
	 * @param document
	 * @return
	 */
	private Element serializeResponseMode(Document document) {
		Element elmtObservedProperty = 
			document.createElementNS("http://www.opengis.net/sos/1.0", 
					"sos:responseMode");
		elmtObservedProperty.setTextContent("inline");
		return elmtObservedProperty;
	}	
	
	/**
	 * Clones this object 
	 * 
	 */
	@Override
	public Object clone() {
		// create a new object 
		GetObservationRequest request = 
				new GetObservationRequest(getSensorOffering(), 
						getObservedProperty(), getResponseFormat());
		// copy the intervals 
		for (TimeInterval interval : getTimeIntervals()) {
			request.addTimeInterval(new TimeInterval(interval.getStart(), 
					interval.getEnd()));
		}
		// return cloned object 
		return request; 
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((observedProperty == null) ? 0 : observedProperty.hashCode());
		result = prime * result
				+ ((responseFormat == null) ? 0 : responseFormat.hashCode());
		result = prime * result
				+ ((sensorOffering == null) ? 0 : sensorOffering.hashCode());
		result = prime * result
				+ ((timeIntervals == null) ? 0 : timeIntervals.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GetObservationRequest other = (GetObservationRequest) obj;
		if (observedProperty == null) {
			if (other.observedProperty != null)
				return false;
		} else if (!observedProperty.equals(other.observedProperty))
			return false;
		if (responseFormat == null) {
			if (other.responseFormat != null)
				return false;
		} else if (!responseFormat.equals(other.responseFormat))
			return false;
		if (sensorOffering == null) {
			if (other.sensorOffering != null)
				return false;
		} else if (!sensorOffering.equals(other.sensorOffering))
			return false;
		if (timeIntervals == null) {
			if (other.timeIntervals != null)
				return false;
		} else if (!timeIntervals.equals(other.timeIntervals))
			return false;
		return true;
	}	
	
	
}
