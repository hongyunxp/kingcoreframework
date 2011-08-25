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

package com.kingcore.framework.base.service;

import java.util.HashMap;

/**
 * <p>这是一个业务处理的基类。
 * 	   * 业务处理类在系统中的功能定位是：
 * 			接收来自控制器(controller 或者是桌面系统的事件处理器)的数据，
 * 			需要做的事情是业务逻辑处理，各种事务的一致性控制，...
 * 				事务类型包括：
 * 					> 调用数据访问对象(DAO)的方法( JDBC Connection 事务)；
 * 					> 文件读写( File R&W 事务)；
 * 					> 远程服务调用( Remoting Service Calling 事务)；
 * 					> ...
 * 
 * 		* 为了提高性能，本类在一个应用中只需要建立一个实例即可，所以要注意以下几点：
 *			> 类中不要有成员对象(instance静态成员除外)，以免线程冲突；
 *			> 采用单件设计模式或者Spring的只加载一次的方法，保证只有一个实例； 
 *     </p>
 * @author Zeven on 2006-8-23
 * @version	2.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public interface Service {

}
