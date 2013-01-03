package com.iai.proteus.common.sos;

/**
 * Enumerates the GetObservation response formats we support
 * 
 * @author Jakob Henriksson
 *
 */
public enum SupportedResponseFormats {

	CSV("text/csv"); 
//	XML_OM_1_0("text/xml;subtype=\"om/1.0\""), 
//	XML_OM_1_0_0("text/xml;subtype=\"om/1.0.0\"");
	
	private String specificiation;
	
	private SupportedResponseFormats(String spec) {
		this.specificiation = spec;
	}
	
	@Override
	public String toString() {
		return specificiation;
	}
	
	/**
	 * Parses the given string and returns a valid ENUM if possible, 
	 * null otherwise 
	 * 
	 * @param str
	 * @return
	 */
	public static SupportedResponseFormats parse(String str) {
		if (str != null) {
			for (SupportedResponseFormats f : SupportedResponseFormats.values()) {
				if (str.equalsIgnoreCase(f.toString())) {
					return f;
				}
			}
		}
		throw new IllegalArgumentException("No constant with text " + 
				str + " found");
	}
}
