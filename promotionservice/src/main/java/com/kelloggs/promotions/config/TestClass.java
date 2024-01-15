package com.kelloggs.promotions.config;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestClass {

	public static void main(String[] args) throws ParseException{
		// TODO Auto-generated method stub
		Date startDate = new Date("01/01/2024 00:00:01");
		boolean before = startDate.before(new Date());
		System.out.println(before);
		
		
		
//		2024-01-01T00:00:01.418Z
//		let isoDate = "2021-09-19T05:30:00.000Z";
//		let newDate =  moment.utc(isoDate).format('MM/DD/YY');
		
//		String myDate = "2024-01-01T00:00:01.418Z"; 
//		Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(myDate.replaceAll("Z$", "+0000"));
//		long millis = date. getTime();
//		System.out.println(millis);
		
		
//		String string = "2013-03-05T18:05:05.000Z";
//		String defaultTimezone = TimeZone.getDefault().getID();
//		Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(string.replaceAll("Z$", "+0000"));
		
		
		
		
		
	}

}
