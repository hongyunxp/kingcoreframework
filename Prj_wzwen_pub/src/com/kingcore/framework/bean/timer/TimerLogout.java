package com.kingcore.framework.bean.timer ;


import java.util.TimerTask;

import org.apache.struts.action.ActionServlet;

/**woo
 * This class represents a value used by the SQL tags. It contains
 * default implementations of get methods for all supported types,
 * to avoid casting when using a concrete implementation. All default
 * implementations throw an UnsupportedConversionException.
 *
 * Each subclass must override the getString() method, returning the
 * value as a String, plus the get method for the appropriate data
 * type, and provide a constructor to set the value.
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */

public class TimerLogout extends TimerTask {

	private ActionServlet actionServlet = null ;

	public TimerLogout(ActionServlet actionServlet )
	{
		this.actionServlet = actionServlet ;
	}

	public void setActionServlet(ActionServlet actionServlet )
	{
		this.actionServlet = actionServlet ;
	}

	public ActionServlet getActionServlet(ActionServlet actionServlet )
	{
		return this.actionServlet ;
	}


	//退出系统登录状态；不是为了减少数据库连接而是为了安全!!
	public void run()
	{
		this.actionServlet.destroy() ;
	}

}

