package com.iai.proteus.common;


/**
 * Utility class where labeling of various UI elements is collected 
 * in one place 
 * 
 * @author Jakob Henriksson
 *
 */
public class Labeling {
	
	/**
	 * Label an Observation Property 
	 * 
	 * @param property
	 * @return
	 */
	public static String labelProperty(String property) {
		if (property != null) {
			String str = Util.readableLocalURL(property.trim());
			return str.substring(0, 1).toUpperCase() + str.substring(1); 
		}
		return "";
	}

}
