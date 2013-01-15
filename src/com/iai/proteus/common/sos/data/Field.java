/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.data;

import java.io.Serializable;

/**
 * Represents a data field (essentially a data column header, or a variable) 
 * 
 * @author Jakob Henriksson
 *
 */
public class Field implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private String name;
	private String definition; 
	private String uom; 
	
	private boolean isTimeField; 
	
	/**
	 * Default constructor  
	 * 
	 */
	public Field() {
		
	}
	
	/**
	 * Constructor 
	 * 
	 * @param name
	 */
	public Field(String name) {
		this.name = name;
		// defaults
		definition = null;
		uom = null;
		isTimeField = false;
	}
	
	/**
	 * Returns the name of the field 
	 * 
	 * @return 
	 */
	public String getName() {
		return name;
	}	
	
	/**
	 * Sets the name 
	 * 
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}	
	
	/**
	 * Returns the definition of the field 
	 * 
	 * @return 
	 */
	public String getDefinition() {
		return definition;
	}

	/**
	 * Sets the definition of the field  
	 * 
	 * @param definition 
	 */
	public void setDefinition(String definition) {
		this.definition = definition;
	}

	/**
	 * Returns the Unit of Measurement of the field 
	 * 
	 * @return the uom
	 */
	public String getUom() {
		return uom;
	}

	/**
	 * Sets the Unit of Measurement of the field 
	 * 
	 * @param uom 
	 */
	public void setUom(String uom) {
		this.uom = uom;
	}
	
	/**
	 * Mark this field as a time field 
	 * 
	 */
	public void timeField() {
		setTimeField(true);
	}
	
	/**
	 * @param isTimeField the isTimeField to set
	 */
	public void setTimeField(boolean isTimeField) {
		this.isTimeField = isTimeField;
	}

	/**
	 * Returns true if this field is a time field, false otherwise 
	 * 
	 * @return
	 */
	public boolean isTimeField() {
		return isTimeField;
	}
	
	@Override 
	public String toString() {
		return name;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Field other = (Field) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
