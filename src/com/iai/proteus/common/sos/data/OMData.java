/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents Observation & Measurement (O&M) data 
 * 
 * @author Jakob Henriksson
 *
 */
public class OMData extends DSVData {

	private String sepBlock;
	private String sepDecimal;
	
	/**
	 * Constructor 
	 * 
	 */
	public OMData() {
		// defaults
		sepBlock = " ";
		sepDecimal = ".";
		sepToken = ","; 
	}
	
	/**
	 * Parses the values and adds them to this data set 
	 * 
	 * @param values
	 */
	public void parseAndAddValues(String values) {
		String[] parts = values.split(sepToken);
		data.add(parts);
	}
	
	/**
	 * Adds a field 
	 * 
	 * @param field
	 */
	public void addField(Field field) {
		fields.add(field);
	}
	
	/**
	 * Set the token separator 
	 * 
	 * @param sep
	 */
	public void setTokenSeparator(String sep) {
		sepToken = sep;
	}
	
	/**
	 * Set the block separator 
	 * 
	 * @param sep
	 */
	public void setBlockSeparator(String sep) {
		sepBlock = sep;
	}
	
	/**
	 * Returns the block separator 
	 * 
	 * @param sep
	 */
	public String getBlockSeparator() {
		return sepBlock;
	}
		
	/**
	 * Set the decimal separator 
	 * 
	 * @param sep
	 */
	public void setDecimalSeparator(String sep) {
		sepDecimal = sep;
	}
	
	/**
	 * Returns the decimal separator 
	 * 
	 * @return
	 */
	public String getDecimalSeparator() {
		return sepDecimal;
	}
	
	/**
	 * Returns an array of fields that seem to be of the given type
	 * 
	 * In O&M Data we can handle timestamp headers without having to 
	 * attempt to parse them, but instead make use of information from
	 * the parsed XML  
	 * 
	 * @param type 
	 * @return
	 */
	@Override
	public List<Field> getFields(FieldType type) {
		if (type.equals(FieldType.TIMESTAMP)) {
			List<Field> matchingFields = new ArrayList<Field>();
			for (Field field : getFields()) {
				// do not attempt to parse, but make use of available info
				if (field.isTimeField())
					matchingFields.add(field);
			}
			return matchingFields;			
		}
		return super.getFields(type);
	}	
}
