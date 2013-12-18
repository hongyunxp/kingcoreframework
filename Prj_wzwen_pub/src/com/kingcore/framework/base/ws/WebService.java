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

package com.kingcore.framework.base.ws;

import java.util.HashMap;

/**
 * <p>　所有　Web Service 服务器端组件的接口。
 * 		提供 公用的基本操作方法定义、对于Web Service (WS)标准的统一调整。
 * 　
 *		> 直接对外提供服务，不需要使用接口;
 *		> ws 与 controller 是同一个层次的，可以直接调用Dao(2层)，也可以调用Handler(3层，由handler调用Dao);
 *
 * 		</p>
 * @author Zeven on 2007-6-22
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public abstract class WebService {

}
