package com.kingcore.framework.bean.timer ;


import java.util.Timer;

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

public class TimerDog {

	private ActionServlet actionServlet = null ;
	private long interval = 0 ;
	private TimerLogout timerLogout = null ;

	public TimerDog(ActionServlet actionServlet,int interval )
	{
		init( actionServlet,interval = interval ) ;


	}
	public TimerDog(ActionServlet actionServlet )
	{
		init( actionServlet,30 ) ;
	}

	//创建并且启动计时器
	private void init(ActionServlet actionServlet,int interval )
	{
		this.actionServlet = actionServlet ;
		this.interval = interval*1000*60 ;

		TimerLogout tmlg = new TimerLogout( actionServlet );
		Timer timer  = new Timer() ;
		timer.schedule( tmlg, 1000, interval ) ;
	}

	public void setActionServlet(ActionServlet actionServlet )
	{
		this.actionServlet = actionServlet ;
	}
	public void setInterval(long setInterval )
	{
		this.interval = interval*1000*60 ;
	}

	public ActionServlet getActionServlet( )
	{
		return this.actionServlet ;
	}

	public long getInterval()
	{
		return this.interval ;
	}
	public TimerLogout getTimerLogout()
	{
		return this.timerLogout ;
	}
}

