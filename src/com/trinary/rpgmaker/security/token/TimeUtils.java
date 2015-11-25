package com.trinary.rpgmaker.security.token;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

public class TimeUtils {
	protected static Date getLaterDate(String duration) throws ParseException {
		return getLaterDate(new Date(), duration);
	}
	
	protected static Date getLaterDate(Date start, String duration) throws ParseException {
		String[] parts = duration.split(" ");
		
		if (parts.length < 2) {
			throw new ParseException("Unable to parse time duration", 0);
		}
		
		Integer number = Integer.parseInt(parts[0]);
		String  unitOfTime = parts[1];
		
		Integer multiplier = 1;
		switch(unitOfTime) {
		case "milliseconds":
			multiplier *= 1;
			break;
		case "seconds":
			multiplier *= 1000;
			break;
		case "minutes":
			multiplier *= 1000 * 60;
			break;
		case "hours":
			multiplier *= 1000 * 60 * 60;
			break;
		case "days":
			multiplier *= 1000 * 60 * 60 * 24;
			break;
		case "weeks":
			multiplier *= 1000 * 60 * 60 * 24 * 7;
			break;
		case "months":
			multiplier *= 1000 * 60 * 60 * 24 * 7 * 4;
			break;
		case "years":
			multiplier *= 1000 * 60 * 60 * 24 * 7 * 4 * 12;
			break;
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(start.getTime() + (number * multiplier));
		return calendar.getTime();
	}
}