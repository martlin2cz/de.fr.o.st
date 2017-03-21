package cz.martlin.defrost.impl;

import static org.junit.Assert.*;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.junit.Test;

public class EmiminoDateFormatTest {

	private static final long DELTA = 10;

	private final DateFormat dateFormat = new SimpleDateFormat("dd.MM.yy");
	private final DateFormat timeFormat = new SimpleDateFormat("hh:mm");

	private final EmiminoDateFormat format = new EmiminoDateFormat();

	public EmiminoDateFormatTest() {
	}
	///////////////////////////////////////////////////////////////////////////////

	@Test
	public void testSplitAtLastSpace() {
		String input = "aaa bbb ccc";
		String[] expected = new String[] { "aaa bbb", "ccc" };
		String[] actual = EmiminoDateFormat.splitAtLastSpace(input);

		assertEquals(expected[0], actual[0]);
		assertEquals(expected[1], actual[1]);
	}

	@Test
	public void testMergeDateAndTime() {
		Date date = createDate(4, 02, 04, 18);
		Date time = createDate(99, 11, 22, 33);

		Date expected = createDate(4, 11, 22, 33);
		Date actual = EmiminoDateFormat.mergeDateAndTime(date, time);

		assertEquals(expected.getTime(), actual.getTime(), DELTA);
	}

	///////////////////////////////////////////////////////////////////////////////

	@Test
	public void testFormatBack() {
		Date date = createDate(100, 11, 22, 33);

		String expected = dateFormat.format(date) + " " + timeFormat.format(date);
		String actual = format.format(date);

		assertEquals(expected, actual);
		// System.out.println(expected);
	}

	@Test
	public void testFormatYesterday() {
		Date date = createDate(1, 11, 22, 33);

		String expected = "včera v " + timeFormat.format(date);
		String actual = format.format(date);

		assertEquals(expected, actual);
		// System.out.println(expected);
	}

	@Test
	public void testFormatToday() {
		Date date = createDate(0, 11, 22, 33);

		String expected = "dnes v " + timeFormat.format(date);
		String actual = format.format(date);

		assertEquals(expected, actual);
		// System.out.println(expected);
	}

	///////////////////////////////////////////////////////////////////////////////

	@Test
	public void testParseBack() throws ParseException {
		Date expected = createDate(100, 11, 22, 00);

		String input = dateFormat.format(expected) + " " + timeFormat.format(expected);
		Date actual = format.parse(input);

		assertEquals(expected.getTime(), actual.getTime(), DELTA);
		// System.out.println(expected);
	}

	@Test
	public void testParseYesterday() throws ParseException {
		Date expected = createDate(1, 11, 22, 00);

		String input = "včera v " + timeFormat.format(expected);
		Date actual = format.parse(input);

		assertEquals(expected.getTime(), actual.getTime(), DELTA);
		// System.out.println(expected);
	}

	@Test
	public void testParseToday() throws ParseException {
		Date expected = createDate(0, 11, 22, 00);

		String input = "dnes v " + timeFormat.format(expected);
		Date actual = format.parse(input);

		assertEquals(expected.getTime(), actual.getTime(), DELTA);
		// System.out.println(expected);
	}

	///////////////////////////////////////////////////////////////////////////////
	private Date createDate(int daysAgo, int hours, int minutes, int seconds) {
		Calendar cal = Calendar.getInstance();

		cal.set(Calendar.DAY_OF_MONTH, cal.get(Calendar.DAY_OF_MONTH) - daysAgo);

		cal.set(Calendar.HOUR_OF_DAY, hours);
		cal.set(Calendar.MINUTE, minutes);
		cal.set(Calendar.SECOND, seconds);

		return cal.getTime();
	}

}
