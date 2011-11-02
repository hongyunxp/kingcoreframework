/**
 * Copyright (C) 2002-2006 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package com.kingcore.framework;

import java.io.Serializable;
import java.util.HashMap;

/**
 * <p>系统常量，跨系统公用的。
 *  说明：凡是位于 "**xx/base/xx**" 目录下、或者名称为 Base***.class 的类(接口)，
 * 		都是做基类，用于被继承，不要直接使用该类的方法或成员。
 * 	/p>
 * @author	WUZEWEN on 2006-05-17
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class BaseConstants implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public BaseConstants() {
		super();
	} 
	
	static{
	} 

	/******************************************************************
						 Constants for System Level
	******************************************************************/
	//主要路径信息
	public static String System_App_Home = "";	  
	public static String System_App_ConfigPath = "";	

	//系统运行模式
	//key - value - value items - default
	public static final String RunMode_Dev = "dev";
	public static final String RunMode_Test = "test";
	public static final String RunMode_Stage = "stage";
	public static final String RunMode_Prod = "prod";

	public static String System_RunMode_Default = RunMode_Dev;
	public static String System_RunMode_Val = System_RunMode_Default;
	
	//用户输入延时，属于用户体验
	public static int User_Input_Delay = 500;  //millisecond
	
	
	/******************************************************************
	                      Constants for Desktop System
	 ******************************************************************/
	//Vista和Windows 7小图标模式都是30像素，Windows 7大图标模式则是40像素，即增高了三分之一。 
	public static final int System_TaskBar_Height = 30;	
	public static int Min_TableColumn_Width = 55;
	public final static String MainFrame_Size_Width   ="MainFrame_Size_Width";
	public final static String MainFrame_Size_Height   ="MainFrame_Size_Height";
	
	/** 系统风格 */
	//key - value - value items - default
	public static String LookAndFeel_Current="Current";
	public static String LookAndFeel_Windows="Windows";
	public static String LookAndFeel_Metal  ="Metal";
	public static String LookAndFeel_Motif  ="Motif";

	public final static String System_LookAndFeel_Key="system.lookAndFeel";
	public final static String System_LookAndFeel_Default=LookAndFeel_Metal;
	
	/** 系统语言 */
	//key - value - value items - default
	public final static String System_Language_CN   ="Chinese.CN";
	public final static String System_Language_TW   ="Chinese.TW";
	public final static String System_Language_EN   ="English";
	public final static String System_Language_JP   ="Japanese";

	public final static String System_Language_Key   ="system.language";
	public final static String System_Language_Default = System_Language_CN;	

	//国际化之-语言国际化资源名称
	public final static String I18n_Language_Name = "Language";
}

