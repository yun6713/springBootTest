package com.bonc.test.qs;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.YearMonth;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

public class JavaBase {
	@Test
	public void javaBase1() {
		//==判定
		System.out.println(2==new Double(2.0));//true
		System.identityHashCode(1);
	}
	@Test
	public void mathApi() {
		//Math api
		System.out.println(Math.round(-1.5));//.5时，向正无穷大取值
		System.out.println(Math.round(1.5));
		System.out.println(Math.ceil(1.5));//向上取整
		System.out.println(Math.floor(1.5));//向下取整
		System.out.println(Math.floorDiv(7, 2));
		System.out.println(Math.floorMod(7, 2));
		System.out.println(Math.floorDiv(-7, 2));
		System.out.println(Math.floorMod(-7, 2));
	}
	@Test
	public void strApi() {
		//String相关
		//f
		String a1 = new String("");
		String a2 = new String("").intern();//常量池对象
		System.out.println(a1==a2);
		//f
		String b1 = "wang";
		String b2=b1+"na";
		System.out.println("wangna"==b2);
		//t
		final String c1 = "wang";
		String c2=c1+"na";
		System.out.println("wangna"==c2);
	}
//	不可同时使用abstract final
//	public abstract final class A{}
	@Test
	public void random() {
		Random random1=new Random(13);
		System.out.println(random1.nextDouble());
		System.out.println(random1.nextInt());
		System.out.println(random1.nextInt(1314));
		Random random2=new Random(13);
		System.out.println(random2.nextDouble());
		System.out.println(random2.nextInt());
		System.out.println(random2.nextInt(1314));
		random2.ints(10).forEach(System.out::println);
	}
	@Test
	public void bigDecimal() {
		BigDecimal bd1=new BigDecimal(0.1);
		BigDecimal bd2=new BigDecimal("0.1");
		BigDecimal bd3=BigDecimal.valueOf(1/10d);

		System.out.println(bd1.equals(bd2));
		System.out.println(bd3.equals(bd2));
//		double d1=100L;
	}
	/**
	 * 精确运算
	 * @author litianlin
	 * @date   2020年3月18日上午11:28:35
	 * @Description TODO
	 */
	public static class Arith {
		public static void main(String[] args) {
			System.out.println(add(0.1, 1/10d));
			System.out.println(sub(0.1, 1/10d));
			System.out.println(mul(0.1, 1/10d));
			System.out.println(div(0.1, 1/10d));
			System.out.println(pow(0.12, 2));
		}
		public static double add(double d1, double d2) {
			return calculate(d1, d2, 0);
		}
		public static double sub(double d1, double d2) {
			return calculate(d1, d2, 1);
		}
		public static double mul(double d1, double d2) {
			return calculate(d1, d2, 2);
		}
		public static double div(double d1, double d2) {
			return calculate(d1, d2, 3);
		}
		public static double pow(double d1, int d2) {
			return calculate(d1, d2, 4);
		}
		/**
		 * 
		 * @param d1
		 * @param d2
		 * @param mode 0-4，加减乘除幂
		 * @return
		 */
		public static double calculate (double d1, double d2, int mode) {
			BigDecimal bd1 = BigDecimal.valueOf(d1);
			BigDecimal bd2 = BigDecimal.valueOf(d2);
			switch (mode) {
			case 0:				
				return bd1.add(bd2).doubleValue();
			case 1:				
				return bd1.subtract(bd2).doubleValue();
			case 2:				
				return bd1.multiply(bd2).doubleValue();
			case 3:				
				return bd1.divide(bd2).doubleValue();
			case 4:				
				return bd1.pow(bd2.intValue()).doubleValue();
			default:
				throw new RuntimeException(String.format("Error;d1:%s,d2:%s,mode:%s", d1,d2,mode));
			}
		}
	}
	
	@Test
	public void dateTime() {
		Date date=new Date();
		System.out.println(date.getTime());
		System.out.println(System.currentTimeMillis());
		Calendar cal=Calendar.getInstance();
		cal.set(2003, 8, 31);
		System.out.println(cal.isLenient());
//		cal.setLenient(false);
		cal.set(Calendar.MONTH, 5);
		System.out.println(cal.getTime());
		Clock clock = Clock.systemUTC();
		System.out.println(clock.instant());
		Instant ins=Instant.now();
		Duration dur=Duration.ofDays(10);
		System.out.println(dur.toHours());
		YearMonth ym=YearMonth.now();
		System.out.println(ym);
		ZonedDateTime zdt=ZonedDateTime.now();
		System.out.println(zdt.getDayOfYear());
		System.out.println(zdt);
		System.out.println(zdt.getZone());
	}

	@Test
	public void regex() {
		Pattern p=Pattern.compile("l+",0);
		System.out.println(Pattern.matches(Pattern.quote("\\w"),"\\w"));
		Matcher m=p.matcher("hello world");
		while(m.find()) {
			System.out.println(m.group());
		}
		m.reset();
		System.out.println(m);
	}

	@Test
	public void format() {
		NumberFormat nf1=NumberFormat.getInstance(Locale.GERMANY);
		System.out.println(nf1.format(1234567890));
		NumberFormat nf2=NumberFormat.getInstance(Locale.CHINA);
		System.out.println(nf2.format(1234567890));
		
		DateFormat df=DateFormat.getInstance();
		System.out.println(df.format(Calendar.getInstance().getTime()));
		System.out.println(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
	
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
		System.out.println(sdf.format(new Date()));
	
		System.out.println(String.format("hello %2$s %1$s %1$s", "a","b"));
		System.out.println(String.format("hello %s %s", "a","b"));
	}
}
