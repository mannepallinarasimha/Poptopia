package com.kelloggs.promotions.config;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateTestClass {

	public static void main(String[] args) throws ParseException {
		// TODO Auto-generated method stub
//		long startdate=1704067201000L;
//		long periodTime=86399800;
		String settingsValue = "10";
		String startDate = "2024-01-01T00:00:01.418Z"; 
		Date date = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(startDate.replaceAll("Z$", "+0000"));
		long startDateMillis = date. getTime();
		System.out.println(startDateMillis);
		
		String endDate = "2024-01-10T23:59:59.176Z"; 
		Date dateEnd = (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")).parse(endDate.replaceAll("Z$", "+0000"));
		long endDateMillis = dateEnd. getTime();
		System.out.println(endDateMillis);
		
		int periodTime = (int) ((endDateMillis - startDateMillis)
				/ Integer.parseInt(settingsValue));
		for (int i=0;i<Integer.parseInt(settingsValue);i++) {
			int randomNumber=getRandomNumber(1,periodTime);
			System.out.println("startdate"+i+"=="+startDateMillis);
			long prizetime=startDateMillis+randomNumber;
			DateFormat obj = new SimpleDateFormat("dd MMM yyyy HH:mm:ss:SSS Z");   
            Date prizeTimeDate = new Date(prizetime);
            long endtime = endDateMillis;
            Date resend = new Date(endtime);
            System.out.println("End Time=="+resend);
            System.out.println("Prize Time"+i+"=="+prizetime + "==time=="+prizeTimeDate);
            startDateMillis=startDateMillis+periodTime;
		}
 
	}
	
	public static int getRandomNumber(int min, int max) {
	    return (int) ((Math.random() * (max - min)) + min);
	}

}
