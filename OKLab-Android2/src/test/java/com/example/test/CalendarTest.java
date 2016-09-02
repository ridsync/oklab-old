package com.example.test;
 
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class CalendarTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
//		간단한 현재시간 찍기
//		  long startTime = System.currentTimeMillis();    
//		  System.out.println("Job 시작 시간 : "+ new Timestamp(startTime));
//		  System.out.println("Job 시작 시간 : "+startTime);

//		현재시간 가져오기 : SimpleDateFormat 이용
//		  Calendar calendar = Calendar.getInstance();
//		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//		  System.out.println("현재 시간 : " + dateFormat.format(calendar.getTime()));
//		  System.out.println( System.currentTimeMillis() );

//			System.out.println( getServerTime() );
//			
//			long startTime = System.currentTimeMillis();
//			Thread th = new Thread();
//			try {
//				th.sleep(2000);
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//			long finishTime = System.currentTimeMillis();
//			long resptime = finishTime - startTime;
//			System.out.println( reformDate("20120109025747.4319") );
			System.out.println( convertStringToDate("0600") );
			System.out.println( diffBothTimes("0330", "0530") );
//			System.out.println( diffBothTimes2("1230", "1900") ); // 시간은 사칙연산 하면 안된다
//			List list = Arrays.asList(new String[] { "A", "B", "C", "D", "G", "V", "W" });
//			    List list = new ArrayList();
//			    System.out.println(list.indexOf("A"));
//			    System.out.println(list.isEmpty());
//			    System.out.println(list.size());
		
//	    loadAnnidayInfo();
		
		Calendar annivDay = Calendar.getInstance();
		Calendar annivDay2 = Calendar.getInstance();
//	    annivDay.set(2016, 9, 13, 22, 59, 59);
	    annivDay2.set(2010, 9, 14, 23, 59, 59);
	    annivDay2.set(Calendar.YEAR, annivDay.get(Calendar.YEAR) +1);
//	    System.out.println( annivDay.getTimeInMillis() );
	    System.out.println( getDate ("yyyy/MM/dd a hh:mm", annivDay2.getTimeInMillis() ) );
//	    System.out.println( getKoreanDate( annivDay.getTimeInMillis() ) );
//	    System.out.println( isExpiration ( annivDay.getTimeInMillis() ) );
//	    System.out.println( calculateDDay ( annivDay.getTimeInMillis() ) );
//	    System.out.println( isValidTermOfBothDays(  annivDay.getTimeInMillis() , annivDay2.getTimeInMillis() )  );
	    
	    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH:mm:ss");
	    sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
	    System.out.println(sdf.format(new Date(0))); //1970-01-01-00:00:00
	   
	    System.out.println( convertFromMinsToTimeType(100));
	    
	    
	    Date date = convertFromStringToDate("20150211 09:23", "yyyyMMdd HH:mm");
	    
	    System.out.println( doCompateStringNumeric());
	}
	
	/**
	 * 시간 비교시 써도 됨.  자리수가 같아야 Compare비교가 정상적으로 크다(양수) 같다 (0) 작다 (음수) 로 판단 할 수있음.
	 * 실제 수로 비교하는것이 아닌 자리로 비교...
	 * @return
	 */
	private static int doCompateStringNumeric(){
		String date = "20120131";
		String time = "1000";
		String date2 = "20121207";
		String time2 = "1200";
		System.out.println( date.compareTo(date2));
		System.out.println( time.compareTo(time2));
		return date.compareTo(date2);
    }
	
	private static String convertFromMinsToTimeType(int minutes){
		int hour = minutes / 60;
		int min = minutes % 60;
		return hour + "." + min;
    }
	
	public static long calculateDDaydd(long timeMillis){
		long diffDays = 0; 
		try {
		    
		    long diff = System.currentTimeMillis() - timeMillis ;
		    diffDays = diff / (24 * 60 * 60 * 1000);
		    
		    return diffDays;
		}
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return diffDays; 
	}
	
	public static String getDate(String format, long time) {
		SimpleDateFormat dateformatter = new SimpleDateFormat(format);
		
		String result = dateformatter.format(new Date(time));;
		
		return result;		
	}
	
	public static String getKoreanDate(long time) {
		Calendar now = Calendar.getInstance();
		String year = String.valueOf( now.get(Calendar.YEAR) ) ;
		SimpleDateFormat dateformatter = new SimpleDateFormat("M월 d일 ");		
		
		String result = null;
		
		result = dateformatter.format(new Date(time));
		
		return year+"년 "+result;
	}
	
	/**
	 *  입력된 날짜(일단위) 비교
	 * @param timeMillis
	 * @return 날짜가 지난경우 true , 지나지않은경우 false
	 */
	public static boolean isExpiration(long time) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
		 String today = dateFormat.format(System.currentTimeMillis());
		String date = dateFormat.format(time);
			
		System.out.println("현재 시간 : " + today);
		System.out.println("입력 시간 : " + date);
			if ( today.compareTo(date) > 0) {
				return true;
			}else {
				return false;
			}
	}
	
	/**
	 * DDay 날짜 계산 - 정확함
	 * @param timeMillis
	 * @return (현재날짜와 입력날짜와의 차이 반환 / 계산 단위: 일)
	 */
	public static long calculateDDay(long timeMillis){
		long diffDays = 0; 
		try {
		    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		    Date today = formatter.parse( formatter.format(System.currentTimeMillis()) );
		    Date dDay = formatter.parse( formatter.format(timeMillis) );
		    
		    long diff = today.getTime() - dDay.getTime() ;
		    diffDays = diff / (24 * 60 * 60 * 1000);
		    
		    return diffDays;
		}
        catch (Exception e)
        {
            e.printStackTrace();
        }
		return diffDays; 
	}
	
	/**
	 * DDay 날짜 계산 - 특정 시간별 문제있음
	 * @param timeMillis
	 * @return (현재날짜와 입력날짜와의 차이 반환 / 계산 단위: 일)
	 */
	public static int calculateDDay2 (long timeMillis){
		int count = 0; 
		try {
		    Calendar today = Calendar.getInstance(); 
		    Calendar date = (Calendar) today.clone();
		    date.setTimeInMillis(timeMillis);
		    
			 long tday = today.getTimeInMillis()/86400000;
			 long dday = date.getTimeInMillis()/86400000;
			 
			 count = (int)dday - (int)tday + 1;
		    return count;
		}
        catch (Exception e)
        {
            e.printStackTrace();
            return count;
        } 
	}
//	/**
//	 * DDay 날짜 계산  2
//	 * @param timeMillis
//	 * @return (현재날짜와 입력날짜와의 차이 반환 / 계산 단위: 일)
//	 */
//	private static String calculateDday(long time){
//	    int count = -1;
//
//	    Calendar today = Calendar.getInstance();
//		Calendar dDay = Calendar.getInstance();
//	    dDay.setTime(new Date(time));
//
//	    while ( !today.after( dDay ) ){
//	    	today.add(Calendar.DATE, 1);
//	    	count++;
//	    }
//	    return String.valueOf(count);
//	}
	
	/**
	 * 기간 시작-만료 여부 계산
	 * @param Low timeMillis
	 * @param High timeMillis
	 * @return (오늘이 두 날짜 기간 사이에 있으면 true / 계산 단위: 일)
	 */
	public static boolean isValidTermOfBothDays(long bgnDttm, long endDttm){
		boolean result = false; 
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
		    Date today = formatter.parse( formatter.format(System.currentTimeMillis()) );
		    Date bgnDate = formatter.parse( formatter.format(bgnDttm) );
		    Date endDate = formatter.parse( formatter.format(endDttm) );
		    
			 long tDay = today.getTime();
			 long bgnDay = bgnDate.getTime();
			 long endDay = endDate.getTime();
			 
			 if ( tDay >= bgnDay && tDay <= endDay )
				 result = true;
			 
		    return result;
		}
        catch (Exception e)
        {
            e.printStackTrace();
            return result;
        } 
	}
	
 
	public static String getServerTime() {
		SimpleDateFormat dateFormat= new SimpleDateFormat("yyyyMMddHHmmssZ");
		return dateFormat.format(new Date());
	}
	public static String reformDate(String date) {
		String retVal = date.split("\\.")[0] + "+0900";
		return retVal;
	}
	
	 /**
     * 0600 String간의 시간차를 계산하는 메소드
     * @author ojungwon
     * @see 같은날 outTime이 더 큰 시간이어야 된다...
     * @return
     */
	public static String diffBothTimes(String inTime, String outTime) {
		String retVal = "";
		
		Calendar inCal = Calendar.getInstance();
		Calendar outCal = (Calendar) inCal.clone();
		try {
			inCal.set(Calendar.HOUR_OF_DAY , Integer.valueOf(inTime.substring(0,2) ) );
			inCal.set(Calendar.MINUTE,  Integer.valueOf(inTime.substring(2,4) ) );
			outCal.set(Calendar.HOUR_OF_DAY , Integer.valueOf(outTime.substring(0,2) ) );
			outCal.set(Calendar.MINUTE,  Integer.valueOf(outTime.substring(2,4) ) );
		
		} catch (NumberFormatException e) {
		}
		retVal = getGapTime(inCal.getTimeInMillis(), outCal.getTimeInMillis()) ;
		return retVal;
	}
	// 각 시간ms값으로 시간차 구하기 ms
	public static String getGapTime( long startTime, long endTime ){

        int gapTime = (int) ( endTime - startTime ) / 1000; // 1000ms -> 1초

        int hour, min, sec;
        sec = gapTime % 60; // 60초는 1분으로 단위 변경
        min = gapTime / 60 % 60; // 60분은 1시간으로 단위변경
        hour = (gapTime / 60)/ 60; // 전체초/60 -> x분/60 -> 몫 시간

        return hour + "시간 " + min + "분 " + sec + "초";

    }
	//  ms값으로 시 일 초 단위로 변환  TimeUnit 사용
	public static String getGapTime2( long startTime, long endTime ){
		
		long mil = Math.abs(endTime - startTime);
				 
		//시로 변환<p></p>
		long hour = TimeUnit.MILLISECONDS.toHours(mil);
		 
		//일로 변환<p></p>
		long min = TimeUnit.HOURS.toMinutes(hour);
		
		//일로 변환<p></p>
		long sec = TimeUnit.HOURS.toSeconds(min);
		
		return hour + "시간 " + min + "분 " + sec + "초";
		
	}
	
	 /**
	  * 시간은 이렇게하면 안됨!!!
     */
	public static String diffBothTimes2(String inTime, String outTime) {
		String retVal = "";
		
		Integer subtract = Integer.valueOf(outTime) -  Integer.valueOf(inTime);
		retVal = String.valueOf(subtract);
		return retVal;
	}
	
	/*
	 * 0600 String 을 06:00AM 형식으로 변환 하는 쓸데없는 메소드 
	 */
	public static String convertStringToDate(String time) {
		String retVal = "";
		int hour = 0;
		int min = 0;
		try {
			hour = Integer.valueOf( time.substring(0,2) ) ;
			min = Integer.valueOf( time.substring(2,4) ) ;
		} catch (NumberFormatException e) {
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("hh:mm a");
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR_OF_DAY , hour);
		cal.set(Calendar.MINUTE, min);
		retVal = dateFormat.format(cal.getTime());
		return retVal;
	}
	
	private static void loadAnnidayInfo(){

		  Calendar calendar = Calendar.getInstance();
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  System.out.println("현재 시간 : " + dateFormat.format(calendar.getTime()));
		  
			Calendar today = Calendar.getInstance();
			Calendar annivDay = (Calendar) today.clone();
		    annivDay.set(2012, 4, 11);
		    System.out.println("AnnivDay = " + annivDay.getTimeInMillis() + " / " + dateFormat.format(annivDay.getTimeInMillis()) );
		  
		    int count = 0; //기념일부터 1일째면 0, 다음날이 1일째면 -1
		    while ( !annivDay.after(today) ){
		    	annivDay.add(Calendar.DATE, 1);
		    	count++;
		    }
		    
		    
			System.out.println("Today = " + today.getTimeInMillis() + " / " + dateFormat.format(today.getTimeInMillis()) );
			System.out.println("Count = " + count);


	}
	
	 /**
     * @return
     */
    public static Date convertFromStringToDate(String date, String format){
    	Date retVal = null;
    	if (format == null || date == null) return retVal;
    	try {
    		SimpleDateFormat transFormat = new SimpleDateFormat(format);
    		Date to = transFormat.parse(date);
    		return to;
    	} catch (Exception e) {
		}
		return retVal;
    }
}
