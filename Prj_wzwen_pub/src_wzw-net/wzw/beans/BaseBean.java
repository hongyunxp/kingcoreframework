/*
 * @(#)BaseAction..java		    2004/03/21
 *
 * Copyright (c) 1998- personal zewen.wu
 * New Technology Region, ChangSha, Hunan, 410001, CHINA.
 * All rights reserved.
 *
 * This software is the confidential and proprietary information of
 * zewen.wu, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with zewen.woo
 */


package wzw.beans ;


import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import wzw.util.HttpUtils;


//import com.forescape.ldt.base.action.LdtActionServlet ;
//import com.kingcore.framework.bean.timer.TimerLogout ;

/**
 * @version		1.00 2004.03.21
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */

public class BaseBean
{

	public HttpServletRequest request ;
    public final ActionErrors errors = new ActionErrors();
	public static Log log = LogFactory.getLog(BaseBean.class);

    public void addErrors(String property, ActionError error)
    {
        errors.add(property, error);
    }

    public void addErrors(ActionError error)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, error);
    }

    public void addErrors(String key)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, new ActionError(key));
    }

    public void clearErrors(){
    	errors.clear();
    }

    public void saveError(HttpServletRequest request)
    {
        //this.saveErrors(request, errors);
    }

    public void doUpdate(){

    }
    public void doQuery(){

    }

    public Connection getConnection(HttpServletRequest request)
        throws SQLException
    {
        DataSource datasource = null;
        Connection conn = null;
        try
        {
            datasource = (DataSource)HttpUtils.getObjectInApplication( request,Globals.DATA_SOURCE_KEY) ;
            conn = datasource.getConnection();
        }
        catch (SQLException se)
        {
            addErrors("error.database.missing");

        }
        return conn;
    }

    public String getParameter(
        HttpServletRequest request,
        String parameterName,
        String defaultValue)
    {
        String[] parameterValues = null;
        String paramValue = null;
        if (request != null)
        {
            parameterValues = request.getParameterValues(parameterName);
            if (parameterValues != null)
                paramValue = parameterValues[0];
            if (paramValue == null)
                paramValue = defaultValue;
        }
        return paramValue;
    }

    public Object getObjectInSession(
        HttpServletRequest request,
        String objectName)
    {
    	return HttpUtils.getObjectInSession( request, objectName) ;
    }


    public void removeObjectInSession(
        HttpServletRequest request,
        String objectName)
    {
        HttpUtils.removeObjectInSession( request, objectName ) ;
    }

    public void setObjectInSession(
        HttpServletRequest request,
        String objectName,
        Object obj)
    {
        wzw.util.HttpUtils.setObjectInSession(request, objectName, obj ) ;
    }

    /*
     *
     *
     *
     */
	public void setRequest( HttpServletRequest request )
	{
		//System.out.print("\n setRequest()") ;
		this.request = request ;
	}

}
