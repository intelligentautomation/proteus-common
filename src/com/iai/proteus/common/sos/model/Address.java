/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

/**
 * Represents the <ows:Address /> part of a Capabilities document 
 * 
 * @author Jakob Henriksson
 *
 */
public class Address {

	private String deliveryPoint; 
	private String city;
	private String administrativeArea;
	private String postalCode;
	private String country; 
	private String email;
	
	/**
	 * Constructor 
	 * 
	 */
	public Address() {
		
	}

	/**
	 * @return the deliveryPoint
	 */
	public String getDeliveryPoint() {
		return deliveryPoint;
	}

	/**
	 * @param deliveryPoint the deliveryPoint to set
	 */
	public void setDeliveryPoint(String deliveryPoint) {
		this.deliveryPoint = deliveryPoint;
	}

	/**
	 * @return the city
	 */
	public String getCity() {
		return city;
	}

	/**
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return the administrativeArea
	 */
	public String getAdministrativeArea() {
		return administrativeArea;
	}

	/**
	 * @param administrativeArea the administrativeArea to set
	 */
	public void setAdministrativeArea(String administrativeArea) {
		this.administrativeArea = administrativeArea;
	}

	/**
	 * @return the postalCode
	 */
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode the postalCode to set
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return the country
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this.country = country;
	}

	/**
	 * @return the emaill
	 */
	public String getEmaill() {
		return email;
	}

	/**
	 * @param emaill the emaill to set
	 */
	public void setEmaill(String emaill) {
		this.email = emaill;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((administrativeArea == null) ? 0 : administrativeArea
						.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result
				+ ((deliveryPoint == null) ? 0 : deliveryPoint.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result
				+ ((postalCode == null) ? 0 : postalCode.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Address other = (Address) obj;
		if (administrativeArea == null)
		{
			if (other.administrativeArea != null)
				return false;
		}
		else if (!administrativeArea.equals(other.administrativeArea))
			return false;
		if (city == null)
		{
			if (other.city != null)
				return false;
		}
		else if (!city.equals(other.city))
			return false;
		if (country == null)
		{
			if (other.country != null)
				return false;
		}
		else if (!country.equals(other.country))
			return false;
		if (deliveryPoint == null)
		{
			if (other.deliveryPoint != null)
				return false;
		}
		else if (!deliveryPoint.equals(other.deliveryPoint))
			return false;
		if (email == null)
		{
			if (other.email != null)
				return false;
		}
		else if (!email.equals(other.email))
			return false;
		if (postalCode == null)
		{
			if (other.postalCode != null)
				return false;
		}
		else if (!postalCode.equals(other.postalCode))
			return false;
		return true;
	}
}
