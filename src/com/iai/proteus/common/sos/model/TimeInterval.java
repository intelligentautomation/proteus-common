/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos.model;

import java.io.Serializable;
import java.util.Date;

import org.joda.time.Interval;

/**
 * Simple class to represent an interval 
 * 
 * @author Jakob Henriksson
 *
 */
public class TimeInterval implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Date start;
	private Date end;
	
	/**
	 * Default constructor 
	 * 
	 */
	public TimeInterval() {

	}
	
	/**
	 * Constructor 
	 * 
	 * @param start
	 * @param end
	 */
	public TimeInterval(Date start, Date end) {
		this.start = start;
		this.end = end;
	}

	/**
	 * @return the start
	 */
	public Date getStart() {
		return start;
	}

	/**
	 * @param start the start to set
	 */
	public void setStart(Date start) {
		this.start = start;
	}

	/**
	 * @return the end
	 */
	public Date getEnd() {
		return end;
	}

	/**
	 * @param end the end to set
	 */
	public void setEnd(Date end) {
		this.end = end;
	}
	
	/**
	 * Returns a TimeInterval instance from a JODA interval object
	 *  
	 * @param interval
	 * @return
	 */
	public static TimeInterval fromJoda(Interval interval) {
		TimeInterval timeInterval = new TimeInterval();
		timeInterval.setStart(interval.getStart().toDate());
		timeInterval.setEnd(interval.getEnd().toDate());
		return timeInterval;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((end == null) ? 0 : end.hashCode());
		result = prime * result + ((start == null) ? 0 : start.hashCode());
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
		TimeInterval other = (TimeInterval) obj;
		if (end == null) {
			if (other.end != null)
				return false;
		} else if (!end.equals(other.end))
			return false;
		if (start == null) {
			if (other.start != null)
				return false;
		} else if (!start.equals(other.start))
			return false;
		return true;
	}
	
}
