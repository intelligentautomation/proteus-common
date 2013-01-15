/*
 * Copyright (C) 2013 Intelligent Automation Inc. 
 * 
 * All Rights Reserved.
 */
package com.iai.proteus.common;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;
import org.xml.sax.SAXException;

public class Util {

	/**
	 * Retrieves a response from a service using GET
	 *
	 * (using default timeout options)
	 *
	 * @param serviceAddress
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SocketTimeoutException
	 */
	public static String get(String serviceAddress)
			throws MalformedURLException, IOException, SocketTimeoutException {
		// defaults
		return get(serviceAddress, 10, 60);
	}

	/**
	 * Retrieves a response from a service using GET
	 *
	 * @param serviceAddress
	 * @param timeoutConncetion
	 * @param timeoutRead
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws SocketTimeoutException
	 */
	public static String get(String serviceAddress,
			int timeoutConnection, int timeoutRead)
		throws MalformedURLException, IOException, SocketTimeoutException
	{

		/* open a URL connection */
		URL url = new URL(serviceAddress);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setConnectTimeout(timeoutConnection * 1000);
		urlConnection.setReadTimeout(timeoutRead * 1000);

		BufferedReader br =
			new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));

		StringBuffer sb = new StringBuffer();
		String input;
		while ((input = br.readLine()) != null) {
			sb.append(input + "\n");
		}

		/* close buffered reader */
		br.close();

		return sb.toString();
	}


	/**
	 * Issues a request to a service using POST
	 *
	 * (using default timeout options)
	 *
	 * @param service
	 * @param request
	 * @return
	 * @throws SocketTimeoutException
	 */
	public static String post(String service, String request) 
			throws SocketTimeoutException, MalformedURLException 
	{
		return post(service, request, 10, 60);
	}
	
	/**
	 * Issues a request to a service using POST
	 *
	 * @param service
	 * @param request
	 * @param timeoutConncetion
	 * @param timeoutRead
	 * @return
	 * @throws SocketTimeoutException
	 */
	public static String post(String service, String request, 
			int timeoutConnection, int timeoutRead)
					throws SocketTimeoutException, MalformedURLException
	{
		return post(service, request, "text/xml", timeoutConnection, timeoutRead);
	}

	/**
	 * Issues a request to a service using POST
	 *
	 * @param service
	 * @param request
	 * @param contentType 
	 * @param timeoutConncetion
	 * @param timeoutRead
	 * @return
	 * @throws SocketTimeoutException
	 */
	public static String post(String service, String request, String contentType, 
			int timeoutConnection, int timeoutRead)
					throws SocketTimeoutException, MalformedURLException
	{

		try {

			HttpURLConnection urlConnection =
				(HttpURLConnection)new URL(service).openConnection();

			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true); // triggers POST
			urlConnection.setUseCaches(false);
			urlConnection.setDefaultUseCaches(false);
			urlConnection.setConnectTimeout(timeoutConnection * 1000);
			urlConnection.setReadTimeout(timeoutRead * 1000);

			urlConnection.setRequestMethod("POST");
			urlConnection.setRequestProperty("Content-Type", contentType);
			urlConnection.setRequestProperty("Accept", "input/xml");

			// write request
			OutputStream out = urlConnection.getOutputStream();
			out.write(request.getBytes());
			out.close();

			// read response
			BufferedReader br =
				new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuffer sb = new StringBuffer();
			String input;
			while ((input = br.readLine()) != null) {
				sb.append(input + "\n");
			}
			// close buffered reader
			br.close();

			return sb.toString();

		} catch (SocketTimeoutException e) {
			// re-throw the exception
			throw e;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


		// default
		return null;
	}


	/**
	 * Parses the XML passed in as an argument and returns a Document object
	 * that represents the parsed XML
	 *
	 * @param xml
	 * @return
	 */
	public static Document parseXML(String xml) throws SAXException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			Document domDoc =
				db.parse(new ByteArrayInputStream(xml.getBytes()));
			domDoc.normalize();

			return domDoc;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		// default for exceptions
		return null;
	}

	/**
	 * Parses the XML stored in the given file and returns a Document object
	 * that represents the parsed XML file
	 *
	 * @param xml
	 * @return
	 */
	public static Document parseXML(File file) {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

		try {

			// Using factory get an instance of document builder
			DocumentBuilder db = dbf.newDocumentBuilder();

			// parse using builder to get DOM representation of the XML file
			FileInputStream fis = new FileInputStream(file);
			Document domDoc = db.parse(fis);
			domDoc.normalize();
			fis.close();

			return domDoc;

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.err.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			e.printStackTrace();
		}

		// default for exceptions
		return null;
	}

	/**
	 * Serializes an XML document to a string
	 *
	 * @param doc
	 * @return
	 */
	public static String getStringFromXMLDocument(Document doc) {
	    DOMImplementationLS domImplementation =
	    	(DOMImplementationLS) doc.getImplementation();
	    LSSerializer lsSerializer = domImplementation.createLSSerializer();
	    return lsSerializer.writeToString(doc);
	}

	/**
	 * Serializes an XML document to disk
	 *
	 */
	public static void serializeXMLToFile(Document document, File output) {

		try {

			TransformerFactory transformerFactory =
				TransformerFactory.newInstance();
	        Transformer transformer = transformerFactory.newTransformer();
	        DOMSource domSource = new DOMSource(document);
	        StreamResult result =  new StreamResult(output);
	        transformer.transform(domSource, result);

		} catch (TransformerConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * "Normalizes" a name as described in the method body
	 *
	 * @param name
	 * @return
	 */
	public static String normalizeName(String name) {
		return name.toLowerCase().replaceAll(" ", "-");
	}

	/**
	 * Replaces all newline and tab character with spaces
	 *
	 * @param str
	 * @return
	 */
	public static String cleanUpString(String str) {
		if (str != null) {
			String newStr = str.replaceAll("\n", " ").replaceAll("\t", " ");
			return removeDoubleSpaces(newStr).trim();
		}
		return str;
	}

	public static String removeWhitespaces(String str) {
		if (str != null) {
			return str.replaceAll("\\s","");
		}
		return str;
	}

	/**
	 * Returns a readable string of a URL
	 *
	 * @param name
	 * @return
	 */
	public static String readableLocalURL(String name) {
		if (name.startsWith("http")) {
			String local = name.substring(name.lastIndexOf('/') + 1);
			if (local.contains("#")) {
				local = local.substring(local.indexOf("#") + 1);
			}
			return local.replaceAll("_", " ");
		}
		return name.replaceAll("_", " ");
	}

	/**
	 * Removes double spaces from the string
	 *
	 * @param str
	 * @return
	 */
	private static String removeDoubleSpaces(String str) {
		int idx = str.indexOf("  ");
		if (idx == -1)
			return str;
		return removeDoubleSpaces(str.replace("  ", " "));
	}

	/**
	 * Implements join
	 *
	 * @param strings
	 * @param delimiter
	 * @return
	 */
	public static String join(Collection<?> strings, String delimiter) {
        StringBuffer buffer = new StringBuffer();
        Iterator<?> iter = strings.iterator();
        if (iter.hasNext()) {
            buffer.append(iter.next().toString());
            while (iter.hasNext()) {
                buffer.append(delimiter);
                buffer.append(iter.next().toString());
            }
        }
        return buffer.toString();
    }

	/**
	 * Join on an Array of Strings
	 *
	 * @param strings
	 * @param delimiter
	 * @return
	 */
	public static String join(String[] strings, String delimiter) {
		if (strings != null && strings.length > 0) {
			Collection<Object> collection = new ArrayList<Object>();
			for (String str : strings) {
				collection.add(str);
			}
			return join(collection, delimiter);
		}
		return null;
	}

	/**
	 * Join on an Array of objects
	 *
	 * @param strings
	 * @param delimiter
	 * @return
	 */
	public static String join(Object[] strings, String delimiter) {
		if (strings != null && strings.length > 0) {
			Collection<Object> collection = new ArrayList<Object>();
			for (Object obj : strings) {
				collection.add(obj.toString());
			}
			return join(collection, delimiter);
		}
		return null;
	}

}
