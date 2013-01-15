/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.data;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.iai.proteus.common.TimeUtils;

/**
 * Abstract class representing delimiter separated values (DSV) data. 
 * This class can be used both by CSV data and other formats such as 
 * Observation & Measurements (O&M).  
 * 
 * @author Jakob Henriksson 
 *
 */
public abstract class DSVData implements SensorData {
	
	private static final Logger log = Logger.getLogger(DSVData.class);		
	
	protected String sepToken; 
	
	protected List<Field> fields; 
	protected List<String[]> data; 
 
	/**
	 * Constructor 
	 */
	public DSVData() {
		fields = new ArrayList<Field>();
		data = new ArrayList<String[]>();
		// defaults
		sepToken = ",";
	}
	
	
	public void addField(Field field) {
		fields.add(field);
	}
	
	
	private boolean fields_initialized = false;
	
	private void initializeFields()
	{
		if (!fields_initialized)
		{
			fields_initialized = true;
			for (Field field : fields) 
			{
				if (field.getName().contains("date"))
				{
					System.err.println();
				}
				if (isColumnType(field, FieldType.TIMESTAMP)) 
				{
					field.timeField();
				} 
			}
		}
	}
	
	/**
	 * Returns the headers 
	 * 
	 * @return
	 */
	public List<Field> getFields() {
		initializeFields();
		return fields; 
	}
	
	/**
	 * Returns an array of fields that seem to be of the given type  
	 * 
	 * @param type 
	 * @return
	 */
	public List<Field> getFields(FieldType type) {
		List<Field> matchingFields = new ArrayList<Field>();
		for (Field field : getFields()) {
			if (isColumnType(field, type)) {
				matchingFields.add(field);
			} 
		}
		return matchingFields; 
	}	
	
	public List<String[]> getData() {
		return data;
	}
	
	/**
	 * Returns the data for the given headers 
	 * 
	 * @param variables
	 * @return
	 */
	public List<String[]> getData(List<Field> variables) {
		// get all data 
		return getData(variables, -1); 
	}
	

	/**
	 * Returns the data for just one variable
	 * 
	 * @param variable
	 * @return
	 */
	public String[] getData(Field variable) {
		List<Field> list = new ArrayList<Field>();
		list.add(variable);
		List<String[]> data = getData(list);
		String[] output = new String[data.size()];
		int i = 0;
		for (String[] s : data)
		{
			output[i++] = s[0];
		}
		return output;
	}
	
	
	/**
	 * Returns the data for the given variables, restricted by the earliest
	 * and latest time stamps. The time stamp is read from the provided field. 
	 * 
	 * @param variables
	 * @param earliest
	 * @param latest
	 * @param timestamp
	 * @return
	 */
	public List<String[]> getData(List<Field> variables, Date earliest, 
			Date latest, Field timestamp) 
	{
		
		List<String[]> data = getData(variables);
		
		if (data != null) {
			int index = getFieldIndex(timestamp, variables);
			if (index != -1) {
				List<String[]> filtered = new ArrayList<String[]>();
				for (String[] row : data) {
					String strTimestamp = row[index];
					Date date = TimeUtils.parseDefault(strTimestamp);
					if (date != null) {
						// true if the date is equal to either end-points, 
						// or between the end-points 
						if ((earliest.compareTo(date) < 0 && 
								latest.compareTo(date) > 0) ||
								earliest.compareTo(date) == 0 ||
								latest.compareTo(date) == 0) {
							filtered.add(row);
						}					
					}
				}
				return filtered; 
			}
		}
		// default 
		return null;
	}
	
	/**
	 * Returns the data for the given variables, restricted by the earliest
	 * and latest time stamps. The time stamp is read from the provided field. 
	 * 
	 * @param variables
	 * @param earliest
	 * @param latest
	 * @param timestamp
	 * @return
	 */
	public List<String[]> getData(Date earliest, Date latest, Field timestamp) 
	{
		
		List<String[]> data = getData();
		
		if (data != null) {
			int index = getFieldIndex(timestamp, getFields());
			if (index != -1) {
				List<String[]> filtered = new ArrayList<String[]>();
				for (String[] row : data) {
					String strTimestamp = row[index];
					Date date = TimeUtils.parseDefault(strTimestamp);
					if (date != null) {
						// true if the date is equal to either end-points, 
						// or between the end-points 
						if ((earliest.compareTo(date) < 0 && 
								latest.compareTo(date) > 0) ||
								earliest.compareTo(date) == 0 ||
								latest.compareTo(date) == 0) {
							filtered.add(row);
						}					
					}
				}
				return filtered;
			}
		}
		// default 
		return null;
	}	
	
	/**
	 * Returns the n first data points for the given headers 
	 * 
	 * @param wantedFields
	 * @param n number of data points to get (-1 for all) 
	 * @return
	 */
	public List<String[]> getData(List<Field> wantedFields, int n) {
		List<String[]> filteredData = new ArrayList<String[]>();
		Map<String, Integer> headerIndexes = getFieldIndexes(wantedFields);
		// we need to have found at least one header 
		if (headerIndexes.size() > 0) {
			int count = 0; 
			for (String[] row : data) {
				
				/* break if we have enough data, and n is not -1 (which
				 * means get all data 
				 */
				if (n != -1 && count >= n)
					break; 
				
				if (row.length <=1)
				{
					// this is not a valid row
					continue;
				}
				
				String[] filteredRow = new String[headerIndexes.size()];
				int i = 0; 
				for (String headerWithIndex : headerIndexes.keySet()) {
					int index = headerIndexes.get(headerWithIndex);
					filteredRow[i] = row[index];
					i++; 
				}
				filteredData.add(filteredRow);
				
				count++;
			}
		}
		return filteredData; 
	}	
	
	/**
	 * Returns the indexes that the given headers are located at
	 * 
	 * @param wantedFields
	 * @return
	 */
	private Map<String, Integer> getFieldIndexes(List<Field> wantedFields) {
		// NOTE: the LinkedHashMap should keep things in order 
		Map<String, Integer> indexes = new LinkedHashMap<String, Integer>();
		List<Field> allFields = getFields();
		for (Field wantedField : wantedFields) {
			int index = 0; 
			for (Field field : allFields) {
				String wanted = wantedField.getName().trim();
				if (wanted.equals(field.getName().trim())) {
					indexes.put(wanted, index);
				}
				index++; 
			}
//			
//			for (int i = 0; i < headers.length; i++) {
//				String wanted = wantedField.trim();
//				if (wanted.equals(headers[i].trim()))
//					indexes.put(wanted, i); 
//			}
		}
		if (indexes.size() != wantedFields.size()) {
			log.warn("Not all wanted headers were found");
		}
		return indexes; 
	}
	
	/**
	 * Returns the index of the given field it if exists, -1 otherwise 
	 * 
	 * @param field
	 * @param fields
	 * @return
	 */
	public int getFieldIndex(Field field, List<Field> fields) {
		int index = 0;
		for (Field f : fields) {
			if (f.equals(field))
				return index;
			index++;
		}
		// default 
		return -1;
	}
	
	/**
	 * Returns true if the column seems to contain values of the given type,
	 * false otherwise
	 * 
	 * @param field
	 * @param type
	 * @return
	 */
	public boolean isColumnType(Field field, FieldType type) {
		String value = getFirstDatapoint(field);
		
		if (type == FieldType.TIMESTAMP)
		{
			/*
			 * Approach: 
			 * 
			 * 1. Check if it is known to be a time
			 * 2. Try to parse it 
			 * 3. Check if the field name contains "date"
			 */
			if (field.isTimeField())
			{
				return true; 
			}
			else if (field.getName().contains("date"))
			{
				return true;
			}
			else 
			{ 
				Date date = TimeUtils.parseDefault(value, false);
				return  (date != null);
			}
		}
		
		if (value != null) {
			value = value.trim();
			switch(type) {
			case INTEGER: 
				try {
					// try to parse as integer
					Integer.parseInt(value);
					return true; 
				} catch (NumberFormatException e) {
				}
				break;
			case DOUBLE:
				try {
					// try to parse as double
					Double.parseDouble(value);
					return true; 
				} catch (NumberFormatException e) {
				}
				break;
		
			}
		}
		// default 
		return false; 
	}	
	
	/**
	 * Returns the name of the data fields of the given type that seem to 
	 * contain changing values 
	 * 
	 * @param type
	 * @return
	 */
	public List<Field> getChangingFields(FieldType type) {
		List<Field> fields = getFields(type);
		List<Field> changingHeaders = new ArrayList<Field>(); 
		for (Field field : fields) {
			// check the first 20 values 
			String[] values = getNDataPoints(field, 20);
			int changes = 0;
			String old = null; 
			for (String value : values) {
				if (old != null) {
					if (!value.equalsIgnoreCase(old))
						changes++;
				}
				old = value;
			}
			/* deem the column to be 'changing' if half the returned values 
			 * are changing 
			 */ 
			if (changes > (values.length / 2))
				changingHeaders.add(field);
		}
		return changingHeaders;
	}
	
	/**
	 * Returns the first data point in the column with the given header
	 * 
	 * @param field
	 * @return
	 */
	public String getFirstDatapoint(Field field) {
		String[] strs = getNDataPoints(field, 1);
		// since we only get one data point, we return the first one 
		if (strs.length == 1)
			return strs[0];
		// default 
		return null;
	}
	
	private String[] getNDataPoints(final Field field, int n) {
		List<String[]> filtered = 
			getData(new ArrayList<Field>() {
				private static final long serialVersionUID = 1L;
				{ 
					add(field); 
				}
			}, n); 
		/*
		 * Since we only have one header the list will contain 1-arrays
		 */
		String[] result = new String[filtered.size()];
		int i = 0; 
		for (String[] strs : filtered) {
			result[i++] = strs[0].trim();
		}
		return result; 
	}
	
	/**
	 * Implements {@link SensorData} 
	 */
	public int size() {
		return data.size();
	}
}
