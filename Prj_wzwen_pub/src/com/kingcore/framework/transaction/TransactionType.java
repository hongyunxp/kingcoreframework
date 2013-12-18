/**
 * Copyright (C) 2002-2011 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package com.kingcore.framework.transaction;

import java.util.HashMap;

/**
 * <p>java类文件的说明...</p>
 * @author Zeven on 2011-8-9
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public enum TransactionType {

	NEW_TRANSACTION,      //开启新事务，隔离上下文事务
	REQUIRED_TRANSACTION,      //有则加入事务，没有就开启新事物
	MAYBE_TRANSACTION,      //有则加入事务，没有就不开启事务
	NO_TRANSACTION      //不开启事务也不加入上下文事务
	//WIDE_TRANSACTION      //
}
