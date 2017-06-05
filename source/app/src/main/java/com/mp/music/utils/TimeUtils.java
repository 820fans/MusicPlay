package com.mp.music.utils;

import java.text.SimpleDateFormat;
import java.util.*;

public class TimeUtils {

	public static int getDay() {
		return Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
	}
	
	public static long getCurrentTime() {
		return System.currentTimeMillis();
	}

	public static String getDateDifftime(Date date,Date now){

		String result="";

		long l=now.getTime() - date.getTime();

		long year=l/(365*24*60*60*1000);
		long month=l/(30*24*60*60*1000);
		long day=l/(24*60*60*1000);
		long hour=(l/(60*60*1000)-day*24);
		long min=((l/(60*1000))-day*24*60-hour*60);
		long s=(l/1000-day*24*60*60-hour*60*60-min*60);

//		System.out.println(now.toString()+","+date.toString());
//		System.out.println(day+","+hour+","+min+","+s);
		if(year>0 || month>0 || (day>=5)){
			SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
			result = sdf.format(date);
		}
		else if(day>0 && day<5){
			result=day + "天前";
		}
		else if(hour>0){
			result=hour + "小时前";
		}
		else if(min>0){
			result=min +"分钟前";
		}
		else if(s>0){
			result=s + "秒前";
		}

		return result;
	}
}
