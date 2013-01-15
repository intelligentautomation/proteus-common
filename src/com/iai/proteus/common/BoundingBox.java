/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common;

public class BoundingBox {

	private String lowerCorner;
	private String upperCorner;
	
	/**
	 * Constructor 
	 * 
	 * @param lowerCorner
	 * @param upperCorner
	 */
	public BoundingBox(String lowerCorner, String upperCorner) {
		this.lowerCorner = lowerCorner;
		this.upperCorner = upperCorner;
	}

	/**
	 * @return the lowerCorner
	 */
	public String getLowerCorner() {
		return lowerCorner;
	}

	/**
	 * @return the upperCorner
	 */
	public String getUpperCorner() {
		return upperCorner;
	}
}
