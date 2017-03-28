package cz.martlin.defrost.forums.impl;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EmiminoDateFormat extends DateFormat {

	private static final long serialVersionUID = -5278512681267464653L;

	private static final String YESTERDAY_STR = "včera v";
	private static final String TODAY_STR = "dnes v";

	private final DateFormat dateFormat = new SimpleDateFormat("d.M.yy");
	private final DateFormat timeFormat = new SimpleDateFormat("hh:mm");

	@Override
	public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
		Date now = new Date();

		String dateStr;
		if (getDay(date) == getDay(now) - 1) {
			dateStr = YESTERDAY_STR;
		} else if (getDay(date) == getDay(now)) {
			dateStr = TODAY_STR;
		} else {
			dateStr = dateFormat.format(date);
		}

		String timeStr = timeFormat.format(date);

		StringBuffer stb = new StringBuffer();
		stb.append(dateStr);
		stb.append(" ");
		stb.append(timeStr);

		return stb;
	}

	@Override
	public Date parse(String source, ParsePosition pos) {

		String[] parts = splitAtLastSpace(source);
		String dateStr = parts[0];
		String timeStr = parts[1];

		Date now = new Date();
		Date date = new Date(now.getTime());
		if (YESTERDAY_STR.equals(dateStr)) {
			date = setDay(date, getDay(now) - 1);
		} else if (TODAY_STR.equals(dateStr)) {
			date = setDay(date, getDay(now) - 0);
		} else {
			try {
				date = dateFormat.parse(dateStr);
			} catch (ParseException e) {
				pos.setErrorIndex(0);
				return null;
			}
		}
		pos.setIndex(dateStr.length());

		Date time;
		try {
			time = timeFormat.parse(timeStr);
		} catch (ParseException e) {
			pos.setIndex(dateStr.length());
			return null;
		}
		pos.setIndex(source.length());

		return mergeDateAndTime(date, time);
	}

	private int getDay(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		return cal.get(Calendar.DAY_OF_MONTH);
	}

	private Date setDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);

		cal.set(Calendar.DAY_OF_MONTH, day);

		return cal.getTime();
	}

	public static Date mergeDateAndTime(Date date, Date time) {
		Calendar dateCal = Calendar.getInstance();
		Calendar timeCal = Calendar.getInstance();
		Calendar cal = Calendar.getInstance();

		dateCal.setTime(date);
		timeCal.setTime(time);

		cal.set(dateCal.get(Calendar.YEAR), dateCal.get(Calendar.MONTH), dateCal.get(Calendar.DAY_OF_MONTH),
				timeCal.get(Calendar.HOUR_OF_DAY), timeCal.get(Calendar.MINUTE), timeCal.get(Calendar.SECOND));
		
		cal.set(Calendar.MILLISECOND, 0);

		return cal.getTime();
	}

	public static String[] splitAtLastSpace(String string) {
		int index = string.lastIndexOf(" ");

		String first = string.substring(0, index);
		String second = string.substring(index + 1);

		return new String[] { first, second };
	}
}
