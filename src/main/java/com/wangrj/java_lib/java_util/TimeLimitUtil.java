package com.wangrj.java_lib.java_util;

import java.sql.Date;

/**
 * 控制试用期的工具类
 */
public class TimeLimitUtil {

	// 保存试用期信息的私密地址
	private static final String privateFilePath = "C:/Windows/guests.log";

	// 试用天数
	public static final int limitDays = 10;

	/**
	 * @param date
	 *            格式如：2016-02-01
	 */
	public static void recordFirstUse(String date) {
		try {
			FileUtil.writeObject(date, privateFilePath);

		} catch (Exception e) {

		}
	}

	public static boolean isFirstUse() {
		// 这里的try-catch是防止文件有问题导致强制类型转换出错而设置
		try {
			String date = (String) FileUtil.readObject(privateFilePath);
			return date == null ? true : false;
		} catch (Exception e) {
			return true;
		}
	}

	/**
	 * @param currentDate
	 *            格式如：2016-02-01
	 */
	public static int leaveDays(String currentDate) {
		try {
			String preUseDate = (String) FileUtil.readObject(privateFilePath);
			if (preUseDate == null) {
				return limitDays;
			}

			// date1为第一次使用的日期
			Date date1 = Date.valueOf(preUseDate);
			// date2为当前网络日期
			Date date2 = Date.valueOf(currentDate);
			long usedTime = date2.getTime() - date1.getTime();
			// usedDays为当前可继续试用的天数
			int leaveDays = limitDays - (int) (usedTime / 1000 / 60 / 60 / 24);

			return leaveDays;

		} catch (Exception e) {

			return 0;
		}
	}
}
