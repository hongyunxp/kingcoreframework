/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.util;

import java.math.BigDecimal;
import java.util.HashMap;

import org.apache.commons.lang.StringUtils;

/**
 * <p>对数字的各种运算、精度处理。</p>
 * <pre>
 * NumberUtils.add(1.2, 1.32)         =2.52
 * NumberUtils.add(1, 1.122)          =2.122
 * NumberUtils.sub(1.32, 1.2)         =0.12
 * NumberUtils.mul(2,  1.32)          =2.64
 * NumberUtils.div(1.32, 2)           =0.66
 * NumberUtils.div(1.32, 2, 1)        =0.7
 * NumberUtils.round(1.32525, 2)      =1.33
 * NumberUtils.round(1.32525, 3)      =1.325
 * </pre>
 * @author Zeven on 2007-2-10
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class NumberUtils {
	
	/*
	 * 缺省的进位制为10进制。其他如2进制、8进制、16进制
	 */
	private static final int DEF_DIV_SCALE = 10;
	
	/**
	 
	 * 提供精确的加法运算。
	 
	 * @param v1 被加数
	 
	 * @param v2 加数
	 
	 * @return 两个参数的和
	 
	 */
	
	public static double add(double v1,double v2)
	{
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.add(b2).doubleValue();
		
	}
	
	/**
	 
	 * 提供精确的减法运算。
	 
	 * @param v1 被减数
	 
	 * @param v2 减数
	 
	 * @return 两个参数的差
	 
	 */
	
	public static double sub(double v1,double v2)
	{
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.subtract(b2).doubleValue();
		
	}
	
	
	/**
	 
	 * 提供精确的乘法运算。
	 
	 * @param v1 被乘数
	 
	 * @param v2 乘数
	 
	 * @return 两个参数的积
	 
	 */
	
	public static double mul(double v1,double v2)
	{
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.multiply(b2).doubleValue();
		
	}
	
	
	/**
	 
	 * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
	 
	 * 小数点以后10位，以后的数字四舍五入。
	 
	 * @param v1 被除数
	 
	 * @param v2 除数
	 
	 * @return 两个参数的商
	 
	 */
	
	public static double div(double v1,double v2)
	{
		
		return div(v1,v2,DEF_DIV_SCALE);
		
	}
	
	
	
	/**
	 
	 * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
	 
	 * 定精度，以后的数字四舍五入。
	 
	 * @param v1 被除数
	 
	 * @param v2 除数
	 
	 * @param scale 表示表示需要精确到小数点以后几位。
	 
	 * @return 两个参数的商
	 
	 */
	
	public static double div(double v1,double v2,int scale)
	{
		
		if(scale<0)
		{
			
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
			
		}
		
		BigDecimal b1 = new BigDecimal(Double.toString(v1));
		
		BigDecimal b2 = new BigDecimal(Double.toString(v2));
		
		return b1.divide(b2,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	
	
	
	/**
	 * 
	 * 提供精确的小数位四舍五入处理。
	 * 
	 * @param v
	 *            需要四舍五入的数字
	 * 
	 * @param scale
	 *            小数点后保留几位
	 * 
	 * @return 四舍五入后的结果
	 * 
	 */
	
	public static double round(double v,int scale){
		
		if(scale<0)
		{
			
			throw new IllegalArgumentException("The scale must be a positive integer or zero");
			
		}
		
		BigDecimal b = new BigDecimal(Double.toString(v));
		
		BigDecimal one = new BigDecimal("1");
		
		return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue();
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		//test add
		System.out.println( StringUtils.rightPad("NumberUtils.add(1.2, 1.32) ", 35," ")
								+ "=" +NumberUtils.add(1.2, 1.32) );
		System.out.println( StringUtils.rightPad("NumberUtils.add(1, 1.122) ", 35," ")
				+ "=" +NumberUtils.add(1, 1.122) );
		
		//test sub
		System.out.println( StringUtils.rightPad("NumberUtils.sub(1.32, 1.2) ", 35," ")
								+ "=" +NumberUtils.sub(1.32, 1.2) );
		
		//test mul 
		System.out.println( StringUtils.rightPad("NumberUtils.mul(2,  1.32) ", 35," ")
								+ "=" +NumberUtils.mul(2,  1.32) );
		
		//test div
		System.out.println( StringUtils.rightPad("NumberUtils.div(1.32, 2) ", 35," ")
								+ "=" +NumberUtils.div(1.32, 2) );
		System.out.println( StringUtils.rightPad("NumberUtils.div(1.32, 2, 1) ", 35," ")
								+ "=" +NumberUtils.div(1.32, 2, 1) );
		
		//test round
		System.out.println( StringUtils.rightPad("NumberUtils.round(1.32525, 2) ", 35," ")
								+ "=" +NumberUtils.round(1.32525, 2) );
		System.out.println( StringUtils.rightPad("NumberUtils.round(1.32525, 3) ", 35," ")
								+ "=" +NumberUtils.round(1.32525, 3) );
		
	}
	
}
