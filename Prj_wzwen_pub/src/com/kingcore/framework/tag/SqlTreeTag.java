package com.kingcore.framework.tag ;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.TagSupport;

import com.kingcore.framework.base.database.DataSourcImpl;


/**
 * @version		1.00 2004.04.16
 * @author		zewen.wu
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 * To enable and disable the creation of type comments go to
 * Window>Preferences>Java>Code Generation.
 *
 */
 
public class SqlTreeTag extends TagSupport
{
    protected String sqlH1 = null;
    protected String sqlH2 = null;
    
    /**
    * 要查询的sql语句
    */
    protected String from = null;
    /**
     * 在空白处显示的内容，如果没有就不允许为空
     */
    protected String where = null;
    /**
     * 显示对应sql语句的列名
     */
    protected String lbbh = null;
    /**
     * 取值的列名
     */
    protected String lbmc = null;
    /**
     * 是否显示取值的列
     */
    protected String bh = null;
     /**/
    protected String mc = null;
    

    public int doStartTag() throws JspException
    {
    	String sql ;
        ServletContext context = null;
        Connection conn = null;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet set = null;
        ResultSet set2 = null;

        Vector shows = new Vector();
        Vector values = new Vector();
        StringBuffer sb = new StringBuffer();
        try
        {
            context = (ServletContext) pageContext.getServletContext();
            conn = DataSourcImpl.getDefConnection(context);
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            //get lb
            //sql = "select " +lbbh+ "," +lbmc+ " from "+ from ;
            set = stmt.executeQuery(sqlH1);
            System.out.print("\n"+sqlH1) ;
            String lbbhvl =null,lbmcvl =null ;
            String bhvl=null, mcvl = null ;
            int  i = 0 ;
            while (set.next())
            {
            	System.out.print("\neee---------") ;
            	i++ ;
                lbbhvl = set.getString(lbbh) ;
                lbmcvl = set.getString(lbmc) ;
                sb.append("var nodeH"+i+"= new treeNode('"+lbbhvl+"','"+lbmcvl+"','#', nodeH);" ) ;
               
                //sql = "select " +bh+ "," +mc+ " from "+ from where
                //sqlH2 = sqlH2 + " where sub_number = '"+lbbhvl+"'" ;
                set2 = stmt2.executeQuery(sqlH2 + " where sub_number = '"+lbbhvl+"'") ;
            	System.out.print("\nfff---------") ;
            	System.out.print("\n"+sqlH2) ;
                while(set2.next()){                	
	                bhvl = set2.getString(bh) ;
	                mcvl = set2.getString(mc) ;
	                sb.append("new treeNode('"+bhvl+"','"+mcvl+"','#', nodeH"+i+");" ) ;
                }
            	System.out.print("\naaa---------") ;
            }
            
            System.out.print("\nbbb---------") ;
           

            JspWriter out = pageContext.getOut();
            try
            {
            	System.out.print("\ncc---------") ;
            	System.out.print("\n"+ sb.toString() ) ;
                out.write(sb.toString());
            }
            catch (IOException e)
            {
            }
        }
        catch (SQLException se)
        {
        	System.out.print("\n"+ se.getErrorCode()) ;
        }
        catch (Exception e)
        {
        }
        finally
        {
        }
        JspWriter out = pageContext.getOut();
        try
        {
            out.write("");
        }
        catch (IOException e)
        {
        }
        return this.SKIP_BODY;
    }
    
   	/**
	 * Releases all instance variables.
	 */
	public void release() {
		from = null;	
		where = null;
		lbbh = null;
		lbmc = null;
		bh = null;	
		mc = null;
	    super.release();
	}

	
	public void setFrom(String from) {
		this.from = from; 
	}

	public void setWhere(String where) {
		this.where = where; 
	}

	public void setLbbh(String lbbh) {
		this.lbbh = lbbh; 
	}

	public void setLbmc(String lbmc) {
		this.lbmc = lbmc; 
	}

	public void setBh(String bh) {
		this.bh = bh; 
	}

	public void setMc(String mc) {
		this.mc = mc; 
	}

	
	public void setSqlH1(String sqlH1) {
		this.sqlH1 = sqlH1; 
	}

	public void setSqlH2(String sqlH2) {
		this.sqlH2 = sqlH2; 
	}

}
