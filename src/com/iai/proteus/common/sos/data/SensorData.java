/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.data;

import java.util.Date;
import java.util.List;

/**
 * Interface for sensor data 
 * 
 * @author Jakob Henriksson
 *
 */
public interface SensorData {

	public List<Field> getFields(); 
	public List<Field> getFields(FieldType type);

	public String[] getData(Field variable);
	public List<String[]> getData(); 
	public List<String[]> getData(List<Field> variables); 
	public List<String[]> getData(List<Field> variables, 
			Date earliest, Date latest, Field timestamp);
	public List<String[]> getData(Date earliest, Date latest, Field timestamp); 
	
	public int size(); 
	
	
}
