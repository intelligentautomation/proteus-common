/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common.sos;

import java.net.MalformedURLException;
import java.net.SocketTimeoutException;

import com.iai.proteus.common.Util;
import com.iai.proteus.common.sos.model.GetObservationRequest;

public class GetObservation {

	/**
	 * Issues a GetObservation request
	 *
	 * @param service
	 * @param request
	 * @param timeoutConnection
	 * @param timeoutRead
	 * @return
	 */
	public static String getObservation(String service,
			GetObservationRequest request, int timeoutConnection, int timeoutRead)
					throws SocketTimeoutException, MalformedURLException
	{

		String result =
				Util.post(service, request.getXmlRequest(),
						timeoutConnection, timeoutRead);

		if (result != null) {
			if (result.trim().startsWith("<") &&
					result.contains("ExceptionReport")) {
					// TODO: parse error and throw an exception
			}

			return result;
		}

		return null;
	}

}
