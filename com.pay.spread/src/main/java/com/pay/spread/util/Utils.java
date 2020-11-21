package com.pay.spread.util;

import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

/**
 * @author rio.choi
 *
 */
public class Utils {

	private static SecureRandom random = new SecureRandom();

	public static long randomDivision(long targetAmt) {
		return ThreadLocalRandom.current().nextLong(targetAmt);
	}

	/**
	 * 랜덤 문자열을 생성한다
	 * 
	 * @return
	 */
	public static String generateRandomStr() {
		String ENGLISH_LOWER = "abcdefghijklmnopqrstuvwxyz";
		String ENGLISH_UPPER = ENGLISH_LOWER.toUpperCase();
		String NUMBER = "0123456789";
		String SPEIAL_CHAR = "!@#$%^&*()_+~=-";

		/** 랜덤을 생성할 대상 문자열 **/
		String DATA_FOR_RANDOM_STRING = ENGLISH_LOWER + NUMBER + SPEIAL_CHAR + ENGLISH_UPPER;
		/** 랜덤 문자열 길이 **/
		int random_string_length = 3;

		if (random_string_length < 1)
			throw new IllegalArgumentException("length must be a positive number.");
		StringBuilder sb = new StringBuilder(random_string_length);
		for (int i = 0; i < random_string_length; i++) {
			sb.append(DATA_FOR_RANDOM_STRING.charAt(random.nextInt(DATA_FOR_RANDOM_STRING.length())));
		}
		return sb.toString();
	}

	/**
	 * 날짜 더하기
	 * 
	 * @param addDays
	 * @return
	 */
	public static Date addDay(int addDays) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("current: " + df.format(cal.getTime()));

		cal.add(Calendar.DATE, addDays);
		System.out.println("after: " + df.format(cal.getTime()));
		return cal.getTime();
	}

	/**
	 * 분 더하기
	 * 
	 * @param addMinutes
	 * @return
	 */
	public static Date addMinutes(int addMinutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		System.out.println("current: " + df.format(cal.getTime()));

		cal.add(Calendar.MINUTE, addMinutes);
		System.out.println("after: " + df.format(cal.getTime()));
		return cal.getTime();
	}

	/**
	 * 오늘을 경과한 날짜인지 판단
	 * 
	 * @param targetdDate
	 * @return
	 */
	public static boolean isOverTodate(Date targetdDate) {
		long diffInMillies = Math.abs(targetdDate.getTime() - new Date().getTime());
		long diff = TimeUnit.MILLISECONDS.convert(diffInMillies, TimeUnit.MILLISECONDS);
		return diff > 0 ? false : true;
	}

}
