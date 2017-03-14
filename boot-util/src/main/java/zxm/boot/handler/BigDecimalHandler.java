package zxm.boot.handler;


import zxm.boot.exc.HandlerException;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class BigDecimalHandler {
	// 默认除法运算精度
	private static final int DEF_DIV_SCALE = 6;
	
	/**
	 * 由string转换
	 * @param value
	 * @return
	 */
	public static BigDecimal valueOf(String value){
		if(StringUtils.isBlank(value)){
			return BigDecimal.ZERO;
		}
		
		BigDecimal bd = new BigDecimal(value);
		return mul(bd, BigDecimal.ONE, DEF_DIV_SCALE);
	}
	
	/**
	 * 提供精确的加法运算。
	 * @param v1 被加数
	 * @param v2 加数
	 * @return 两个参数的和
	 */
	public static BigDecimal add(Number v1, Number v2) {
		BigDecimal b1 = new BigDecimal(v1 == null ? "0" : v1 + "");
		BigDecimal b2 = new BigDecimal(v2 == null ? "0" : v2 + "");
		return b1.add(b2);
	}
	
	/**
	 * 提供精确的减法运算。
	 * @param v1 被减数
	 * @param v2 减数
	 * @return 两个参数的差
	 */
	public static BigDecimal sub(Number v1, Number v2) {
		BigDecimal b1 = new BigDecimal(v1 == null ? "0" : v1 + "");
		BigDecimal b2 = new BigDecimal(v2 == null ? "0" : v2 + "");
		return b1.subtract(b2);
	}
	
	/**
	 * 提供精确的乘法运算，精确到 小数点以后6位，以后的数字四舍五入。
	 * @return 两个参数的乘积
	 */
	public static BigDecimal mul(BigDecimal a, BigDecimal b) {
		return mul(a, b, DEF_DIV_SCALE);
	}
	
	/**
	 * 提供精确的乘法运算。
	 * @param scale 精确小数点位数
	 * @return 两个参数的乘积
	 */
	public static BigDecimal mul(BigDecimal a, BigDecimal b, int scale) {
		if (scale < 0) {
			throw new HandlerException("The scale must be a positive integer or zero");
		}
		BigDecimal c = a.multiply(b);
		return c.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 除法运算，当发生除不尽的情况时，精确到 小数点以后6位，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @return 两个参数的商
	 */
	public static BigDecimal div(Number v1, Number v2) {
		return div(v1, v2, DEF_DIV_SCALE);
	}
	
	/**
	 * 除法运算。当发生除不尽的情况时，由scale参数指 定精度，以后的数字四舍五入。
	 * @param v1 被除数
	 * @param v2 除数
	 * @param scale 表示表示需要精确到小数点以后几位。
	 * @return 两个参数的商
	 */
	public static BigDecimal div(Number v1, Number v2, int scale) {
		if (scale < 0) {
			throw new HandlerException("The scale must be a positive integer or zero");
		}
		BigDecimal b1 = new BigDecimal(v1 + "");
		BigDecimal b2 = new BigDecimal(v2 + "");
		return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
	}

	/*根据给定的舍入模式将输入数字舍入为一位数的结果
	输入数字	UP	DOWN	CEILING	FLOOR	HALF_UP	HALF_DOWN	HALF_EVEN	UNNECESSARY
	5.5		6	5		6		5		6		5			6			抛出 ArithmeticException
	2.5		3	2		3		2		3		2			2			抛出 ArithmeticException
	1.6		2	1		2		1		2		2			2			抛出 ArithmeticException
	1.1		2	1		2		1		1		1			1			抛出 ArithmeticException
	1.0		1	1		1		1		1		1			1			1
	-1.0	-1	-1		-1		-1		-1		-1			-1			-1
	-1.1	-2	-1		-1		-2		-1		-1			-1			抛出 ArithmeticException
	-1.6	-2	-1		-1		-2		-2		-2			-2			抛出 ArithmeticException
	-2.5	-3	-2		-2		-3		-3		-2			-2			抛出 ArithmeticException
	-5.5	-6	-5		-5		-6		-6		-5			-6			抛出 ArithmeticException*/
	/**
	 * 四舍五入
	 * @param v
	 * @param scale
	 * @return
	 */
	public static BigDecimal round(BigDecimal v, int scale) {
		if (scale < 0) {
			throw new HandlerException("The scale must be a positive integer or zero");
		}

		return v.setScale(scale, BigDecimal.ROUND_HALF_UP);
	}
	
	/**
	 * 使用默认区域格式化百分数
	 * @param d
	 * @param pattern
	 * @return
	 */
	public static String formatPercent(BigDecimal d, String pattern) {
		return formatPercent(d, pattern, Locale.getDefault());
	}

	/**
	 * 按指定区域格式化百分数
	 * @param d
	 * @param pattern:"##,##.000%"-->不要忘记“%”
	 * @param l
	 * @return
	 */
	public static String formatPercent(BigDecimal d, String pattern, Locale l) {
		String s = "";
		try {
			DecimalFormat df = (DecimalFormat) NumberFormat.getPercentInstance(l);
			df.applyPattern(pattern);
			if (d.compareTo(BigDecimal.ZERO) > 0) {
				s = df.format(d);
			} else {
				s = "0.00%";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 使用默认方式显示货币： 例如:￥12,345.46 默认保留2位小数，四舍五入
	 * @param d double
	 * @return String
	 */
	public static String formatCurrency(BigDecimal d) {
		String s = "";
		try {
			DecimalFormat nf = (DecimalFormat) NumberFormat.getCurrencyInstance(Locale.CHINA);
			if (d.compareTo(BigDecimal.ZERO) == 1) {
				s = nf.format(d);
			} else {
				s = "￥0.00";
			}
		} catch (Exception e) {
			// e.printStackTrace();
		}
		return s;
	}
	
	/**
	 * 转换为字符串
	 */
	public static String formatString(BigDecimal value, int scale) {
		value = round(value, scale);
		return value.toString();
	}

	/**
	 * 比较俩个数字大小
	 * @param v1 数字1
	 * @param v2  数字2
	 * @return 0:相等 1:大于 -1:小于
	 */
	public static int compareTo(Number v1, Number v2) {
		BigDecimal b1 = new BigDecimal(v1 == null ? "0" : v1 + "");
		BigDecimal b2 = new BigDecimal(v2 == null ? "0" : v2 + "");
		return b1.compareTo(b2);
	}
}
