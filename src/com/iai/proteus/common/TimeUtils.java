package com.iai.proteus.common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.joda.time.Interval;

public class TimeUtils {

	private static final Logger log =
		Logger.getLogger(TimeUtils.class);

	private final static String zuluFormat = "yyyy-MM-dd'T'HH:mm:ss'Z'";
	private final static String zuluFormatShort = "yyyy-MM-dd'T'HH:mm'Z'";
	private final static String zuluFormatLong = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

	// formats to try to parse against
	private final static String[] formats =
		new String[] { zuluFormat, zuluFormatShort, zuluFormatLong };

	/**
	 * Formats the given timestamp according OGC standard
	 *
	 * @param timestamp
	 * @return
	 */
	public static String format(Date timestamp) {
		SimpleDateFormat formatter = new SimpleDateFormat(zuluFormat);
		formatter.setTimeZone(TimeZone.getTimeZone("GMT"));
		return formatter.format(timestamp);
	}

	/**
	 * Parse a string into a Date
	 *
	 * @param str
	 * @return
	 */
	public static Date parseDefault(String str) {
		return parseDefault(str, true);
	}

	/**
	 * Parse a string into a Date
	 *
	 * @param str
	 * @param toLog
	 * @return
	 */
	public static Date parseDefault(String str, boolean toLog) {
		if (str == null)
		{
			return null;
		}
		str = str.trim();
		if (str.endsWith("Z")) {
			boolean parseSuccess = false;
			List<String> errors = new ArrayList<String>();
			for (String format : formats) {
				DateFormat df = new SimpleDateFormat(format);
				// explicitly set timezone of input if needed
				df.setTimeZone(TimeZone.getTimeZone("Zulu"));
				try {
					Date date = df.parse(str);
					parseSuccess = true;
					return date;
				} catch (ParseException e) {
					errors.add(e.getMessage());
				}
			}
			if (!parseSuccess && toLog) {
				for (String error : errors) {
					// TODO: we cannot assume that we are running this code
					//       with a RCP UI
//					UIUtil.log(Activator.PLUGIN_ID, error);
					log.error(error);
				}
			}
		}

		if (toLog)
			log.warn("Time '" + str + "' does not seem to be in standard format");

		return null;
	}

	public static long msDay() {
		return 24 * msHour();
	}

	public static long msHour() {
		return 1000 * 60 * 60;
	}

	/**
	 * Returns true if the start and end are within the last 24 hours
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean includesLastDay(Date start, Date end) {
		// this should not be the case, return false if it is
		if (start == null)
			return false;

		// if end is null we may not have an end time, return true
		if (end == null)
			return true;

		DateTime now = new DateTime();
		Interval required = new Interval(now.minusHours(24), now);

		if (!start.before(end))
			return false;

		Interval intervalData =
				new Interval(start.getTime(), end.getTime());

		if (intervalData.overlaps(required)) {
			return true;
		}

		// default
		return false;
	}

	/**
	 * Returns true if the start and end are within the last 24 hours
	 *
	 * @param start
	 * @param end
	 * @return
	 */
	public static boolean includesLastWeek(Date start, Date end) {
		// this should not be the case, return false if it is
		if (start == null)
			return false;

		// if end is null we may not have an end time, return true
		if (end == null)
			return true;

		DateTime now = new DateTime();
		Interval required = new Interval(now.minusDays(7), now);

		if (!start.before(end))
			return false;

		Interval intervalData =
				new Interval(start.getTime(), end.getTime());

		if (intervalData.overlaps(required)) {
			return true;
		}

		// default
		return false;
	}
}
