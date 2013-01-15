/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.data;

import java.io.IOException;
import java.io.StringReader;

import org.apache.log4j.Logger;

import au.com.bytecode.opencsv.CSVReader;

/**
 * Represents Comma Separated Values data 
 * 
 * @author Jakob Henriksson
 *
 */
public class CSVData extends DSVData {
	
	private static final Logger log = Logger.getLogger(CSVData.class);		
	
	/**
	 * Constructor 
	 */
	public CSVData() {
		sepToken = ",";
	}
	
	/**
	 * Parses the CSV data and returns an object containing the parsed data 
	 * 
	 * @param dataStr
	 * @return
	 */
	public static CSVData parse(String dataStr) {
		try {
			CSVData csv = new CSVData(); 
			CSVReader reader = new CSVReader(new StringReader(dataStr));
			csv.data = reader.readAll();
			/*
			 * Headers
			 */
			String[] headers = csv.data.get(0); 
			for (String header : headers) {
				csv.addField(new Field(header.trim())); 
			}

			/*
			 * Data (remove the headers) 
			 */
			csv.data.remove(0);
			
			return csv; 
		} catch (IOException e) {
			log.error("IO Exception while reading CSV data");
		}
		// default
		return null;
	}
}
