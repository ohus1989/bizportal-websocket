package com.kdax.bizportal.common.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class DateStringUtil {
    public static final int YEAR = 1;
    public static final int MONTH = 2;
    public static final int DATE = 3;
    public static final int MONTHFIRST = 4;
    public static final int MONTHEND = 5;

    /**
     *
     * 현재 날짜와 시각을 yyyyMMdd 형태로 변환 후 return.
     *
     * @return yyyyMMdd
     *
     *         - 사용 예 String date = DateStringUtil.getYyyymmdd()
     */
    public static String getYyyymmdd(Calendar cal) {
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMdd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(cal.getTime());
    }

    /**
     *
     * GregorianCalendar 객체를 반환함.
     *
     * @param yyyymmdd
     *            날짜 인수
     * @return GregorianCalendar
     * @see java.util.Calendar
     * @see java.util.GregorianCalendar
     *
     *      - 사용 예 Calendar cal =
     *      DateStringUtil.getGregorianCalendar(DateStringUtil.getCurrentYyyymmdd())
     */
    public static GregorianCalendar getGregorianCalendar(String yyyymmdd) {

        int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int dd = Integer.parseInt(yyyymmdd.substring(6));

        GregorianCalendar calendar = new GregorianCalendar(yyyy, mm - 1, dd, 0, 0, 0);

        return calendar;

    }

    /**
     *
     * 현재 날짜와 시각을 yyyyMMddhhmmss 형태로 변환 후 return.
     *
     * @return yyyyMMddhhmmss
     * @see java.util.Date
     * @see java.util.Locale
     *
     *      - 사용 예 String date = DateStringUtil.getCurrentDateTime()
     */
    public static String getCurrentDateTime() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }

    /**
     *
     * 현재 날짜와 시각을 yyyy-MM-dd hh:mm:ss 형태로 변환 후 return.
     *
     * @return yyyy-MM-dd hh:mm:ss
     * @see java.util.Date
     * @see java.util.Locale
     *
     *      - 사용 예 String date = DateStringUtil.getCurrentDateTime()
     */
    public static String getCurrentDateTime2() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }

    /**
     *
     * 현재 날짜와 시각을 yyyy-MM-dd hh:mm:ss 형태로 변환 후 return.
     *
     * @return yyyy-MM-dd hh:mm:ss
     * @see java.util.Date
     * @see java.util.Locale
     *
     *      - 사용 예 String date = DateStringUtil.getCurrentDateTime()
     */
    public static String getCurrentDateTimeYYMMDD() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }

    /**
     *
     * 입력받은 날짜를 yyyyMMddhhmmss 형태로 변환 후 return.
     *
     * @param date
     * @return yyyyMMddhhmmss
     * @see java.util.Date
     * @see java.util.Locale
     *
     *      - 사용 예 String date = DateStringUtil.getCurrentDateTime(date)
     */
    public static String getDateToString(Date date) {
        Date today = date;
        if (today == null)
            today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }

    /**
     *
     * 입력받은 날짜를 pattern 형태로 변환 후 return.
     *
     * @param date
     * @return yyyyMMddhhmmssSSS
     * @see java.util.Date
     * @see java.util.Locale
     *
     *      - 사용 예 String date = DateStringUtil.getCurrentDateTime(date, "yyyyMMddhhmmss")
     */
    public static String getDateToString(Date date, String pattern) {
        Date today = date;
        if (today == null)
            today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        // String pattern = "yyyyMMddHHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);
    }

    /**
     *
     * 현재 시각을 hhmmss 형태로 변환 후 return.
     *
     * @return hhmmss
     * @see java.util.Date
     * @see java.util.Locale
     *
     *      - 사용 예 String date = DateStringUtil.getCurrentDateTime()
     */
    public static String getCurrentTime() {
        Date today = new Date();
        Locale currentLocale = new Locale("KOREAN", "KOREA");
        String pattern = "HHmmss";
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, currentLocale);
        return formatter.format(today);

    }

    /**
     *
     * 현재 날짜를 yyyyMMdd 형태로 변환 후 return.
     *
     * @return yyyyMMdd *
     *
     *         - 사용 예 String date = DateStringUtil.getCurrentYyyymmdd()
     */
    public static String getCurrentYyyymmdd() {
        return getCurrentDateTime().substring(0, 8);
    }

    /**
     *
     * 주로 일자를 구하는 메소드.
     *
     * @param yyyymm
     *            년월
     * @param week
     *            몇번째 주
     * @param pattern
     *            리턴되는 날짜패턴 (ex:yyyyMMdd)
     * @return 연산된 날짜
     * @see java.util.Calendar
     *
     *      - 사용 예 String date = DateStringUtil.getWeekToDay("200801" , 1, "yyyyMMdd")
     */
    public static String getWeekToDay(String yyyymm, int week, String pattern) {

        Calendar cal = Calendar.getInstance(Locale.FRANCE);

        int new_yy = Integer.parseInt(yyyymm.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymm.substring(4, 6));
        int new_dd = 1;

        cal.set(new_yy, new_mm - 1, new_dd);

        // 임시 코드
        if (cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            week = week - 1;
        }

        cal.add(Calendar.DATE, (week - 1) * 7 + (cal.getFirstDayOfWeek() - cal.get(Calendar.DAY_OF_WEEK)));

        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.FRANCE);

        return formatter.format(cal.getTime());

    }

    /**
     *
     * 지정된 플래그에 따라 연도 , 월 , 일자를 연산한다.
     *
     * @param field
     *            연산 필드
     * @param amount
     *            더할 수
     * @param date
     *            연산 대상 날짜
     * @return 연산된 날짜
     * @see java.util.Calendar
     *
     *      - 사용 예 String date = DateStringUtil.getOpDate(java.util.Calendar.DATE , 1,
     *      "20080101")
     */
    public static String getOpDate(int field, int amount, String date) {

        GregorianCalendar calDate = getGregorianCalendar(date);

        if (field == Calendar.YEAR) {
            calDate.add(GregorianCalendar.YEAR, amount);
        } else if (field == Calendar.MONTH) {
            calDate.add(GregorianCalendar.MONTH, amount);
        } else {
            calDate.add(GregorianCalendar.DATE, amount);
        }

        return getYyyymmdd(calDate);

    }

    /**
     *
     * 입력된 일자를 더한 주를 구하여 return한다
     *
     * @param yyyymmdd
     *            년도별
     * @param addDay
     *            추가일
     * @return 연산된 주
     * @see java.util.Calendar
     *
     *      - 사용 예 int date = DateStringUtil.getWeek(DateStringUtil.getCurrentYyyymmdd() , 0)
     */
    public static int getWeek(String yyyymmdd, int addDay) {
        Calendar cal = Calendar.getInstance(Locale.FRANCE);
        int new_yy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int new_mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int new_dd = Integer.parseInt(yyyymmdd.substring(6, 8));

        cal.set(new_yy, new_mm - 1, new_dd);
        cal.add(Calendar.DATE, addDay);

        int week = cal.get(Calendar.DAY_OF_WEEK);
        return week;
    }

    /**
     *
     * 입력된 년월의 마지막 일수를 return 한다.
     *
     * @param year
     * @param month
     * @return 마지막 일수
     * @see java.util.Calendar
     *
     *      <pre>
     *  - 사용 예
     * int date = DateStringUtil.getLastDayOfMon(2008 , 1)
     *      </pre>
     */
    public static int getLastDayOfMon(int year, int month) {

        Calendar cal = Calendar.getInstance();
        cal.set(year, month, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);

    }// :

    /**
     *
     * 입력된 년월의 마지막 일수를 return한다
     *
     * @param yyyymm
     * @return 마지막 일수
     *
     *         - 사용 예 int date = DateStringUtil.getLastDayOfMon("2008")
     */
    public static int getLastDayOfMon(String yyyymm) {

        Calendar cal = Calendar.getInstance();
        int yyyy = Integer.parseInt(yyyymm.substring(0, 4));
        int mm = Integer.parseInt(yyyymm.substring(4)) - 1;

        cal.set(yyyy, mm, 1);
        return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     *
     * 입력된 날자가 올바른지 확인합니다.
     *
     * @param yyyymmdd
     * @return boolean
     *
     *
     *         <pre>
     *  - 사용 예
     * boolean b = DateStringUtil.isCorrect("20080101")
     *         </pre>
     */
    public static boolean isCorrect(String yyyymmdd) {
        boolean flag = false;
        if (yyyymmdd.length() < 8) {
            return false;
        }
        //try {
        int yyyy = Integer.parseInt(yyyymmdd.substring(0, 4));
        int mm = Integer.parseInt(yyyymmdd.substring(4, 6));
        int dd = Integer.parseInt(yyyymmdd.substring(6));
        flag = DateStringUtil.isCorrect(yyyy, mm, dd);
        //} catch (Exception ex) {
        //return false;
        //}
        return flag;
    }// :

    /**
     *
     * 입력된 날자가 올바른 날자인지 확인합니다.
     *
     * @param yyyy
     * @param mm
     * @param dd
     * @return boolean
     *
     *         - 사용 예 boolean b = DateStringUtil.isCorrect(2008,1,1)
     */
    public static boolean isCorrect(int yyyy, int mm, int dd) {
        if (yyyy < 0 || mm < 0 || dd < 0)
            return false;
        if (mm > 12 || dd > 31)
            return false;

        String year = "" + yyyy;
        String month = "00" + mm;
        String year_str = year + month.substring(month.length() - 2);
        int endday = DateStringUtil.getLastDayOfMon(year_str);

        if (dd > endday)
            return false;

        return true;

    }// :

    /**
     *
     * 현재 일자를 입력된 type의 날짜로 반환합니다.
     *
     * @param type
     * @return String
     * @see java.text.DateFormat
     *
     *      - 사용 예 String date = DateStringUtil.getThisDay("yyyymmddhhmmss")
     */
    public static String getThisDay(String type) {
        Date date = new Date();
        SimpleDateFormat sdf = null;

        //try {
        if (type.toLowerCase(new Locale("KOREAN", "KOREA")).equals("yyyymmdd")) {
            sdf = new SimpleDateFormat("yyyyMMdd");
            return sdf.format(date);
        }
        if (type.toLowerCase(new Locale("KOREAN", "KOREA")).equals("yyyymmddhh")) {
            sdf = new SimpleDateFormat("yyyyMMddHH");
            return sdf.format(date);
        }
        if (type.toLowerCase(new Locale("KOREAN", "KOREA")).equals("yyyymmddhhmm")) {
            sdf = new SimpleDateFormat("yyyyMMddHHmm");
            return sdf.format(date);
        }
        if (type.toLowerCase(new Locale("KOREAN", "KOREA")).equals("yyyymmddhhmmss")) {
            sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            return sdf.format(date);
        }
        if (type.toLowerCase(new Locale("KOREAN", "KOREA")).equals("yyyymmddhhmmssms")) {
            sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            return sdf.format(date);
        } else {
            sdf = new SimpleDateFormat(type);
            return sdf.format(date);
        }
        //} catch (IOException e) {
        //return "[ ERROR ]: parameter must be 'YYYYMMDD', 'YYYYMMDDHH', 'YYYYMMDDHHSS'or 'YYYYMMDDHHSSMS'";
        //}
    }

    /**
     *
     * 입력된 일자를 '9999년 99월 99일' 형태로 변환하여 반환한다.
     *
     * @param yyyymmdd
     * @return String
     *
     *         - 사용 예 String date = DateStringUtil.changeDateFormat("20080101")
     */
    public static String changeDateFormat(String yyyymmdd) {
        String rtnDate = null;

        String yyyy = yyyymmdd.substring(0, 4);
        String mm = yyyymmdd.substring(4, 6);
        String dd = yyyymmdd.substring(6, 8);
        rtnDate = yyyy + " 년 " + mm + " 월 " + dd + " 일";

        return rtnDate;

    }

    /**
     *
     * 두 날짜간의 날짜수를 반환(윤년을 감안함)
     *
     * @param startDate
     *            시작 날짜
     * @param endDate
     *            끝 날짜
     * @return 날수
     * @see java.util.GregorianCalendar
     *
     *  - 사용 예
     * long date = DateStringUtil.getDifferDays("20080101","20080202")
     */
    public static long getDifferDays(String startDate, String endDate) {

        GregorianCalendar StartDate = getGregorianCalendar(startDate);
        GregorianCalendar EndDate = getGregorianCalendar(endDate);
        long difer = (EndDate.getTime().getTime() - StartDate.getTime()
                .getTime()) / 86400000;
        return difer;

    }

    /**
     *
     * 현재의 요일을 구한다.
     *
     * @param
     * @return 요일
     * @see java.util.Calendar
     *
     *  - 사용 예
     * int day = DateStringUtil.getDayOfWeek()
     *  SUNDAY    = 1
     *  MONDAY    = 2
     *  TUESDAY   = 3
     *  WEDNESDAY = 4
     *  THURSDAY  = 5
     *  FRIDAY    = 6
     */
    public static int getDayOfWeek() {
        Calendar rightNow = Calendar.getInstance();
        int day_of_week = rightNow.get(Calendar.DAY_OF_WEEK);
        return day_of_week;
    }

    /**
     *
     * 현재주가 올해 전체의 몇째주에 해당되는지 계산한다.
     *
     * @param
     * @return 요일
     * @see java.util.Calendar
     *
     *      <pre>
     *  - 사용 예
     * int day = DateStringUtil.getWeekOfYear()
     * </pre>
     */
    public static int getWeekOfYear() {
        Locale LOCALE_COUNTRY = Locale.KOREA;
        Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
        int week_of_year = rightNow.get(Calendar.WEEK_OF_YEAR);
        return week_of_year;
    }

    /**
     * 현재주가 현재월에 몇째주에 해당되는지 계산한다.
     *
     * @param
     * @return 요일
     * @see java.util.Calendar
     *
     *  - 사용 예
     * int day = DateStringUtil.getWeekOfMonth()
     */
    public static int getWeekOfMonth() {
        Locale LOCALE_COUNTRY = Locale.KOREA;
        Calendar rightNow = Calendar.getInstance(LOCALE_COUNTRY);
        int week_of_month = rightNow.get(Calendar.WEEK_OF_MONTH);
        return week_of_month;
    }

    /**
     * 해당 p_date날짜에 Calendar 객체를 반환함.
     *
     * @param p_date
     * @return Calendar
     * @see java.util.Calendar
     *
     *  - 사용 예
     * Calendar cal = DateStringUtil.getCalendarInstance(DateStringUtil.getCurrentYyyymmdd())
     */
    public static Calendar getCalendarInstance(String p_date) {
        // Locale LOCALE_COUNTRY = Locale.KOREA;
        Locale LOCALE_COUNTRY = Locale.FRANCE;
        Calendar retCal = Calendar.getInstance(LOCALE_COUNTRY);

        if (p_date != null && p_date.length() == 8) {
            int year = Integer.parseInt(p_date.substring(0, 4));
            int month = Integer.parseInt(p_date.substring(4, 6)) - 1;
            int date = Integer.parseInt(p_date.substring(6));

            retCal.set(year, month, date);
        }
        return retCal;
    }

    /**
     *
     * 현재 일시의 UnixTime을 구한다
     *
     * @return long
     *
     * - 사용 예
     * long uTime = DateStringUtil.getUnixTime()
     */
    public static long getUnixTime() {

        Calendar c = Calendar.getInstance();

        return c.getTimeInMillis() / 1000;

    }

    /**
     *
     * 현재 일시의 UnixTime Millis을 구한다
     *
     * @return long
     *
     * - 사용 예
     * long uTime = DateStringUtil.getUnixTimeMillis()
     */
    public static long getUnixTimeMillis() {

        Calendar c = Calendar.getInstance();

        return c.getTimeInMillis();

    }

    @SuppressWarnings("deprecation")
    public static long getUnixTimeMillis(String date) {

        Calendar c = Calendar.getInstance();

        try {
            Date sd = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
            c.set(sd.getYear()+1900, sd.getMonth(), sd.getDate(), sd.getHours(), sd.getMinutes(), sd.getSeconds());

            return c.getTimeInMillis();

        } catch (ParseException e) {
            return -1;
        }

    }

    /**
     *
     * 입력받은 현재 일시의 UnixTime을 구한다
     *
     * @param date : 'yyyyMMddhhmmss'
     * @return UnixTime, 에러 발생시 -1 반환
     *
     * - 사용 예
     * long uTime = DateStringUtil.getUnixTime('yyyyMMddhhmmss')
     */
    @SuppressWarnings("deprecation")
    public static long getUnixTime(String date) {

        Calendar c = Calendar.getInstance();

        try {
            Date sd = new SimpleDateFormat("yyyyMMddHHmmss").parse(date);
            c.set(sd.getYear()+1900, sd.getMonth(), sd.getDate(), sd.getHours(), sd.getMinutes(), sd.getSeconds());

            return c.getTimeInMillis() / 1000;

        } catch (ParseException e) {
            return -1;
        }
    }


    /**
     * UnixTime을 Calendar 객체로 반환
     * @param uTime : UnixTime
     * @return Calendar
     *
     * - 사용예
     * Calendar cal = DateStringUtil.getUnixTimeConvert(DateStringUtil.getUnixTime())
     */
    public static Calendar getUnixTimeToCalendar(long uTime) {

        long mTime = uTime * 1000;
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(mTime);

        return c;
    }

    /**
     * String Date를 Date 객체로 반환
     * @param dStr : 문자형 날짜
     * @param patten : yyyyMMddHHmmss
     * @return Date
     *
     * - 사용예
     * Date date = DateStringUtil.getStringToDate("20160623131401", "yyyyMMddHHmmss")
     */
    public static Date getStringToDate(String dStr, String patten){
        SimpleDateFormat sdf = new SimpleDateFormat(patten);
        try {
            return sdf.parse(dStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * String Date를 patten이 다른 String Date 객체로 반환
     * @param dStr : 문자형 날짜
     * @param beforePatten : yyyyMMddHHmmss
     * @param afterPatten : yyyyMMddHHmmss
     * @return String
     *
     * - 사용예
     * Date date = DateStringUtil.getStringToString("20160623", "yyyyMMdd", "yyyy-MM-dd")
     */
    public static String getStringToString(String dStr, String beforePatten, String afterPatten) {

        try {
            SimpleDateFormat bdf = new SimpleDateFormat(beforePatten);
            Date d = bdf.parse(dStr);

            SimpleDateFormat adf = new SimpleDateFormat(afterPatten);

            return adf.format(d);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     * Kendo-UI Grid에서 사용되는 날짜 포멧을 Date형으로 변환
     * @param dStr : 문자형 날짜
     * @return Date
     *
     * - 사용예
     * Date date = DateStringUtil.getStringToDate("2015-03-12T15:00:00.000Z")
     */
    public static Date getKendoStringToDate(String dStr){
        String d = dStr.replaceAll("Z", "+0000");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ");
        try {
            return sdf.parse(d);
        } catch (ParseException e) {
            return null;
        }
    }

    public static Date getTimeZeroDate(Date date){
        String sDate = getDateToString(date, "yyyy-MM-dd");
        try {
            return getStringToDate(sDate, "yyyy-MM-dd");
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * String Date를 Date 객체로 반환
     * @param dStr : yyyyMMddHHmmss 패턴의 문자형 날짜
     * @return Date
     *
     * - 사용예
     * Date date = DateStringUtil.getStringToDate("20160623131401")
     */
    public static Date getStringToDate(String dStr){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            return sdf.parse(dStr);
        } catch (ParseException e) {
            return null;
        }
    }

    /**
     *
     * @MethodName : 5분 단위 절사
     * @ClassName : DateUtil
     * @Method : Trunc5MinDate
     * @author : user
     * @date : 2014. 10. 10. 오후 1:13:52
     * @Comment : 현재 시간을 입력하면 5분 단위로 절사하여 반환.
     * @param dStr
     * @return : Date
     */
/*	public static Date Trunc5MinDate(Date dStr){
		try {
			return new Date(
				(DateStringUtil.getUnixTime(DateStringUtil.getDateToString(dStr)) -
				DateStringUtil.getUnixTime(DateStringUtil.getDateToString(dStr)) % 300)
				*1000);
		} catch (Exception e) {
			return null;
		}

	}*/

    /**
     *
     * @MethodName : 날짜 계산
     * @ClassName : DateUtil
     * @Method : DateAdd
     * @author : user
     * @date : 2014. 10. 10. 오후 1:13:52
     * @Comment : 현재 시간에서 일,월,년,시간,분,초 를 추가해서 계산
     * @param nDate : 현재시간
     * @param val : 계산한 값
     * @param type : 날짜 타입, Calendar.MINUTE, Calendar.SECOND .....
     * @return : Date
     */
	/*public static Date DateAdd(Date nDate, int val, int type){
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(nDate);
			cal.add(type, val);
			return cal.getTime();

		} catch (Exception e) {
			return null;
		}
	}*/



    /**
     * HH:mm 형태의 시간 데이터를 0 ~ 1440 형태의 분 데이터로 변환
     * @param minuteData
     * @return
     */
    public static String getMinuteData(String minuteData) {
        int hour = 0;
        int minute = 0;
        if(minuteData != null && !"".equals(minuteData)) {
            String[] temp = minuteData.split(":");
            hour = Integer.parseInt(temp[0]) * 60 ;
            minute = Integer.parseInt(temp[1]);
            return String.valueOf(hour+minute);
        }else {
            return null;
        }

    }


    /**
     * 30일전 날짜 리턴
     * @return
     */
    public static String getPrevMonthDay() {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DATE, -30); // 오늘날짜로부터 -1
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷
        String yDate = sdf.format(c1.getTime()); // String으로 저장

        return yDate;
    }

    /**
     * yyyy-mm-dd 어제 날짜 리턴
     * @return
     */
    public static String getYesterDay() {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DATE, -1); // 오늘날짜로부터 -1
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷
        String yDate = sdf.format(c1.getTime()); // String으로 저장

        return yDate;
    }

    /**
     * 다음날짜 리턴
     * @return
     */
    public static String getTomorowDay() {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DATE, 1); // 오늘날짜로부터 -1
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷
        String yDate = sdf.format(c1.getTime()); // String으로 저장

        return yDate;
    }

    /**
     *
     * 현재 날짜와 시각을 yyyyMMdd 형태로 변환 후 return.
     *
     * @return yyyyMMdd
     *
     *         - 사용 예 String date = DateStringUtil.getYyyymmdd()
     */
    public static String getToday() {
        Calendar c1 = new GregorianCalendar();
        c1.add(Calendar.DATE, 0); // 오늘날짜로부터 -1
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd"); // 날짜 포맷
        String yDate = sdf.format(c1.getTime()); // String으로 저장

        return yDate;
    }
    /**
     * 시간의 문자열 HH:mm:ss형태를 초로 변경
     * @return : long
     */
    public static long convertTimeToSec(String secondtTime) {
        long result = 0;
        try {

            DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
            Date reference = dateFormat.parse("00:00:00");
            Date date = dateFormat.parse(secondtTime);
            result = (date.getTime() - reference.getTime()) / 1000L;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 시간의 초를 문자열 HH:mm:ss형태로 변경
     * @return : String
     */
    public static String convertSecToTime(long secondtTime) {
        String result = "";
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
        df.setTimeZone(tz);
        result = df.format(new Date(secondtTime*1000L));

        return result;
    }
    /**
     * 현재시간 다음날 리턴 포멧 
     *포맷형식 : yyyyMMddHHmmss
     * @return : String
     */
    public static String getTomorowDayNew() {
        Date date = new Date();
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyyMMddHHmmss");
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE,+1);
        return sdformat.format(cal.getTime());
    }

}
