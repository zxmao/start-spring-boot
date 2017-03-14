/**
 *
 */
package zxm.boot.handler;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class DateHandler {
	static Logger logger = LoggerFactory.getLogger(DateHandler.class);

	public static final String YYYYMMDD = "yyyy-MM-dd";

	public static String getCreateTime() {
		return getCurrentTime("yyyy-MM-dd HH:mm:ss");
	}

	private static final String [] WEEKDAYS_CHN = {"周日","周一","周二","周三","周四","周五","周六"};

	private static final String TIMEZONE_SH = "Asia/Shanghai";
	/**
	 * 获取当前 时间
	 *
	 * @return
	 */
	public static String getCurrentTime(String fmt) {
		Calendar calendar = Calendar.getInstance();
		return getTime(calendar.getTime(), fmt);
	}

	/**
	 * 获取当前 时间
	 *
	 * @return
	 */
	public static String getTime(Date date) {
		return getTime(date, YYYYMMDD);
	}

	/**
	 * 获取当前 时间
	 *
	 * @return
	 */
	public static String getTime(Date date, String fmt) {
		if (StringUtils.isBlank(fmt)) {
			fmt = YYYYMMDD;
		}
		SimpleDateFormat myFormat = new SimpleDateFormat(fmt);
		String mystrdate = myFormat.format(date);
		return mystrdate;
	}

	/**
	 * 时间转换
	 * @return
	 */
	public static String getTime(String dateStr, String from, String to) {
		Date date = formatDate(dateStr, from);
		return getTime(date, to);
	}

	/**
	 * 获取指定日期开始时间 yyyy-MM-dd 00:00:00
	 *
	 * @return
	 */
	public static String getDateBeginStr(Date date) {
		return getTime(date, "yyyy-MM-dd 00:00:00");
	}

	/**
	 * 获取指定日期结束时间yyyy-MM-dd 23:59:59
	 *
	 * @return
	 */
	public static String getDateEndStr(Date date) {
		return getTime(date, "yyyy-MM-dd 23:59:59");
	}


	public static Date getTime(String strDate , String fmt){
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			return sdf.parse(strDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 格式化毫秒数 时间
	 *
	 * @param time
	 * @param fmt
	 *            ,fmt为空的时候，默认是yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String formatTime(Long time, String fmt) {
		return getTime(new Date(time), fmt);
	}

	public static Date getCreateDate(){
		return getCurrentDate("yyyy-MM-dd HH:mm:ss");
	}

	/**
	 * 计算当前时间
	 *
	 */
	public static Date getCurrentDate(String fmt) {
		if (StringUtils.isBlank(fmt)) {
			fmt = YYYYMMDD;
		}

		// 当前时间
		Date currentTime = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat(fmt);
		String strDate = formatter.format(currentTime);
		ParsePosition pos = new ParsePosition(0);
		return formatter.parse(strDate, pos);
	}

	/**
	 * 格式化日期
	 *
	 *            ,fmt为空，默认是 yyyy-MM-dd
	 * @return
	 */
	public static Date formatDate(String dateStr) {
		return formatDate(dateStr, YYYYMMDD);
	}

	/**
	 * 格式化日期
	 *
	 * @param fmt
	 *            ,fmt为空，默认是 yyyy-MM-dd
	 */
	public static Date formatDate(String dateStr, String fmt) {
		if (StringUtils.isBlank(dateStr)) {
			return null;
		}
		if (fmt == null) {
			fmt = YYYYMMDD;
		}
		SimpleDateFormat formetter = new SimpleDateFormat(fmt);
		try {
			return formetter.parse(dateStr);
		} catch (ParseException e) {
			logger.error("时间转换异常！", e);
		}
		return null;
	}

	/**
	 * 获取今天开始时间 yyyy-MM-dd 00:00:00
	 *
	 */
	public static Date getDateBegin(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 获取今天最后一秒时间 yyyy-MM-dd 23:59:59
	 *
	 */
	public static Date getDateEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return calendar.getTime();
	}

	/**
	 * 判断是否是48小时之前
	 *
	 */
	static Boolean isBefore48h(Long time) {
		Long cTime = new Date().getTime() - time;
		return cTime < 172800000l;
	}

	/**
	 * 计算月份间隔
	 *
	 * @throws ParseException
	 */
	public static int getMonthSpace(String date1, String date2) throws ParseException {
		int result = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Calendar c1 = Calendar.getInstance();
		Calendar c2 = Calendar.getInstance();
		c1.setTime(sdf.parse(date1));
		c2.setTime(sdf.parse(date2));
		result = c2.get(Calendar.MONTH) - c1.get(Calendar.MONTH);
		return result == 0 ? 1 : Math.abs(result);
	}

	/**
	 * 计算日期间隔
	 *
	 * @throws ParseException
	 */
	public static int getDateSpace(String date1, String date2) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

		return daysBetween(sdf.parse(date1), sdf.parse(date2));
	}

	/**
	 * 计算日期间隔
	 *
	 */
	public static int daysBetween(Date early, Date late) {
		int days = 0;
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();

		calst.setTime(early);
		caled.setTime(late);

		calst.set(Calendar.HOUR_OF_DAY, 0);
		calst.set(Calendar.MINUTE, 0);
		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.HOUR_OF_DAY, 0);
		caled.set(Calendar.MINUTE, 0);
		caled.set(Calendar.SECOND, 0);

		days = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 3600 / 24;
		return days == 0 ? 0 : Math.abs(days);
	}

	/**
	 * 计算日期间隔
	 *
	 */
	public static int minutesBetween(Date early, Date late) {
		int minutes = 0;
		Calendar calst = Calendar.getInstance();
		Calendar caled = Calendar.getInstance();

		calst.setTime(early);
		caled.setTime(late);

		calst.set(Calendar.SECOND, 0);
		caled.set(Calendar.SECOND, 0);

		minutes = ((int) (caled.getTime().getTime() / 1000) - (int) (calst.getTime().getTime() / 1000)) / 60;
		return minutes == 0 ? 0 : Math.abs(minutes);
	}

	/**
	 * 获取下几miao
	 *
	 */
	public static Date getNextSeconds(Date date, int temp) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.SECOND, rightNow.get(Calendar.SECOND) + temp);

		return rightNow.getTime();
	}

	/**
	 * 获取下几分钟
	 *
	 */
	public static Date getNextMinutes(Date date, int temp) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.MINUTE, rightNow.get(Calendar.MINUTE) + temp);

		return rightNow.getTime();
	}

	/**
	 * 获取下几小时
	 *
	 */
	public static Date getNextHours(Date date, int temp) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.HOUR_OF_DAY, rightNow.get(Calendar.HOUR_OF_DAY) + temp);

		return rightNow.getTime();
	}

	/**
	 * 获取下几天
	 */
	public static Date getNextDay(Date date, int temp) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.DATE, rightNow.get(Calendar.DATE) + temp);

		return rightNow.getTime();
	}

	/**
	 * 获取下几月
	 *
	 */
	public static Date getNextMonth(Date date, int temp) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.MONTH, rightNow.get(Calendar.MONTH) + temp);

		return rightNow.getTime();
	}

	/**
	 * 获取下几年
	 *
	 */
	public static Date getNextYear(Date date, int temp) {
		Calendar rightNow = Calendar.getInstance();
		rightNow.setTime(date);
		rightNow.set(Calendar.YEAR, rightNow.get(Calendar.YEAR) + temp);

		return rightNow.getTime();
	}

	public static boolean compare(Date first, Date second, String pattern){
		String firstStr = getTime(first, pattern);
		String secondStr = getTime(second, pattern);
		return firstStr.equals(secondStr);
	}

	public static String getWeekByDate(String strDate,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			Date date = sdf.parse(strDate);
			Calendar cal = Calendar.getInstance();
			cal.setTime(date);
			return WEEKDAYS_CHN[cal.get(Calendar.DAY_OF_WEEK) - 1];
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return null;
	};

	public static int getAge(final Date birthday) {

		if (birthday == null) {
			return 0;
		}
		return (int) Math.round(dateDiff(getCurrent(), birthday, Calendar.YEAR));

	}

	public static Calendar getCurrentCalendar() {
		Calendar shanghaiTime = Calendar.getInstance(TimeZone.getTimeZone(TIMEZONE_SH));

		Calendar current = Calendar.getInstance();

		current.set(shanghaiTime.get(Calendar.YEAR), shanghaiTime.get(Calendar.MONTH), shanghaiTime.get(Calendar.DAY_OF_MONTH),
						shanghaiTime.get(Calendar.HOUR_OF_DAY), shanghaiTime.get(Calendar.MINUTE), shanghaiTime.get(Calendar.SECOND));

		return current;
	}

	public static Date getCurrent() {
		return getCurrentCalendar().getTime();
	}
	/**
	 * 计算两个时间之间的间隔
	 *
	 * @param date1  时间一
	 * @param date2  时间二
	 * @param unit
	 *          间隔单位,可以是秒Calendar.SECOND，分钟Calendar.MINUTE，小时Calendar.HOUR，
	 *          天Calendar.DATE
	 * @return 返回指定间隔单位而计算出的时间间隔，date1-date2
	 */
	public static double dateDiff(Date date1, Date date2, int unit) {

		long diff = date1.getTime() - date2.getTime();
		if (unit == Calendar.SECOND) {
			return diff / 1000.0;
		} else if (unit == Calendar.MINUTE) {
			return diff / 1000.0 / 60.0;
		} else if (unit == Calendar.HOUR) {
			return diff / 1000.0 / 60.0 / 60.0;
		} else if (unit == Calendar.DAY_OF_YEAR) {
			return diff / 1000.0 / 60.0 / 60.0 / 24.0;
		} else if (unit == Calendar.WEEK_OF_MONTH) {
			return diff / 1000.0 / 60.0 / 60.0 / 24.0 / 7.0;
		} else if (unit == Calendar.MONTH) {
			return diff / 1000.0 / 60.0 / 60.0 / 24.0 / 30.5;
		} else if (unit == Calendar.YEAR) {
			return diff / 1000.0 / 60.0 / 60.0 / 24.0 / 30.5 / 12.0;
		} else {
			return diff;
		}
	}

	public static Integer getWeekday(Date today) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(today);
	    int dayForWeek = 0;
	    if(calendar.get(Calendar.DAY_OF_WEEK) == 1){
	    	dayForWeek = 7;
	    }else{
	    	dayForWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1;
	    }
	    return dayForWeek;
	}

	public static Date getFirstDayInWeekday(Date date) {
		Calendar calendar = Calendar.getInstance();
		int weekday = getWeekday(date);
		if(weekday == 7){
			date = getNextDay(date, -1);
		}
		
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_WEEK, 2);
		return calendar.getTime();
	}

	public static int getDayOfMonth(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		return calendar.get(Calendar.DAY_OF_MONTH) + 1;
	}

	/*
	 *获取当月第一天	2016-09-01 00:00:01
	 */
	public static String getFirstDayInMonthday() {
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH,1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 1);
		return DateHandler.getTime(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");
	}

	/*
	 *获取当月最后一天	2016-09-30 23:59:59
	 */
	public static String getLastDayInMonthday() {
		Calendar calendar=Calendar.getInstance();
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH,calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		return DateHandler.getTime(calendar.getTime(),"yyyy-MM-dd HH:mm:ss");
	}

	public static Date getMonthBegin(Date date) {
		String dateStr = getTime(date, "yyyy-MM-01");
		return formatDate(dateStr, "yyyy-MM-dd");
	}
	
	public static Date getMonthEnd(Date date) {
		Date endDate = getNextMonth(getMonthBegin(date), 1);
		return getNextDay(endDate, -1);
	}

	public static boolean isValidate(String dateStr, String pattern) {
		Date date = formatDate(dateStr, pattern);
	    if(date == null){
	    	return false;
	    }
	    
	    return true;
	}
	
	public static void main(String[] args) {
		Date date = getFirstDayInWeekday(DateHandler.formatDate("20170130", "yyyyMMdd"));
		System.out.println(getTime(date, "yyyyMMdd"));
	}
}
