package com.kingcore.framework.base.controller;


//import com.wzwen.system.LoginUserBean ;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * This class is a custom action for sending a redirect request,
 * with possible parameter values URL encoded and the complete URL
 * encoded for URL rewriting (session tracking).
 *
 * @author Hans Bergsten, Gefion software <hans@gefionsoftware.com>
 * @version 1.0
 */
public class UsgnTreeAction extends BaseAction{


    /**
     * 定义一些常量
     */
    
    public ActionForward executeAction(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
    {
    	
    	/*
    	 *
    	 *
    	 *
    	 *
    	 *
    	 */
    	
		Connection conn = null ;
    	
		try{
			conn = wzw.util.DbUtils.getConnection( request ) ;
		}catch(SQLException ex)
		{
			System.out.print("\ncan't get connection!") ;
		}
			System.out.print("\n111-----------------") ;
		
		String tStr = null ;
		String param = request.getParameter("params") ;
		if(param!=null){
			tStr = getTreeString(conn, param);
		}else{
			String[] params = new String[8] ;
	    	params[0] = request.getParameter("sql1") ;
	    	params[1] = request.getParameter("sql2") ;
	    	params[2] = request.getParameter("lbbh") ;
	    	params[3] = request.getParameter("lbmc") ;
	    	params[4] = request.getParameter("bh") ;
	    	params[5] = request.getParameter("mc") ;
	    	params[6] = request.getParameter("lb") ;
			tStr = getTreeString(conn, params);
		}
			System.out.print("\n111-----------------"+tStr) ;
			
		request.setAttribute("treeString", tStr) ;
		
		//
		tStr = getuserGnqx( request );
		request.setAttribute("userGnqx", tStr) ;

		try{
			if(conn!=null)
				conn.close() ;
		}catch(SQLException ex)
		{
			System.out.print("\nfail when close connection!") ;
		}
        return doForword(mapping,form,request,response) ;
    }



    public ActionForward doForword(
        ActionMapping mapping,
        ActionForm form,
        HttpServletRequest request,
        HttpServletResponse response)
    {
        
        return (mapping.findForward("success" ));
    }
    
    public String getTreeString(Connection conn, String[] params){
    	
    	String sql ;
        Statement stmt = null;
        Statement stmt2 = null;
        ResultSet set = null;
        ResultSet set2 = null;

        StringBuffer sb = new StringBuffer();
        try
        {
            	System.out.print("\n222a---------") ;
            stmt = conn.createStatement();
            stmt2 = conn.createStatement();
            //get lb
            //sql = "select " +lbbh+ "," +lbmc+ " from "+ from ;
            System.out.print("\n"+params[0]) ;
            set = stmt.executeQuery(params[0]);
            String lbbhvl =null,lbmcvl =null ;
            String bhvl=null, mcvl = null ;
            int  i = 0 ;
            	System.out.print("\n222bb---------") ;
            while (set.next())
            {
            	System.out.print("\neee---------") ;
            	i++ ;
                lbbhvl = set.getString( params[2] ) ;
                lbmcvl = set.getString( params[3] ) ;
                sb.append("var nodeH"+i+"= new treeNode('"+lbbhvl+"','"+lbmcvl+"','#', nodeH);" ) ;
               
                //sql = "select " +bh+ "," +mc+ " from "+ from where
                //sqlH2 = sqlH2 + " where sub_number = '"+lbbhvl+"'" ;
                set2 = stmt2.executeQuery(params[1] + " where "+params[6]+" = '"+lbbhvl+"'") ;
            	System.out.print("\nfff---------") ;
            	System.out.print("\n"+params[1]) ;
                while(set2.next()){                	
	                bhvl = set2.getString( params[4] ) ;
	                mcvl = set2.getString( params[5] ) ;
	                sb.append("new treeNode('"+bhvl+"','"+mcvl+"','#', nodeH"+i+");" ) ;
                }
            	System.out.print("\naaa---------") ;
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
	        try
	        {
	        	if(stmt!=null)
	        		stmt.close() ;
	        	if(stmt2!=null)
	        		stmt2.close() ;
	        	if(set!=null)
	        		set2.close() ;
        	}
	        catch (Exception e)
	        {
	        	System.out.print("\nerror when close conn and stmtaaa!") ;
	        }
        }
        
        
        return sb.toString() ;
    }
    
    
	public String getTreeString(Connection conn, String param ){
    	
    	String sql ;
        Statement stmt = null;
        ResultSet set = null;

        StringBuffer sb = new StringBuffer();
        try
        {
            	System.out.print("\n222a---------") ;
            //String table=null, bhCol=null,mcCol=null ;
            int  i = 0 ;
            String[] params = new String[6] ;
            System.out.print("\n"+param) ;
            StringTokenizer st = new StringTokenizer(param,",") ;
            while( st.hasMoreTokens()){
            	params[i] = st.nextToken(",") ;
            	System.out.print("\n"+params[i]) ; 
            	i++ ;
            }
            stmt = conn.createStatement();
            //get lb
            sql = "select " +params[1]+ "," +params[2]+ " from "+ params[0] ;
            set = stmt.executeQuery( sql );
            String bhVl=null, mcVl = null ;
            	System.out.print("\n222bb---------") ;
            while (set.next())
            {
                bhVl = set.getString( params[1] ) ;
                mcVl = set.getString( params[2] ) ;
                sb.append("var nodeH"+i+"= new treeNode('"+bhVl+"','"+mcVl+"','#', nodeH);" ) ;
            
            	System.out.print("\naaa---------") ;
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
	        try
	        {
	        	if(stmt!=null)
	        		stmt.close() ;
	        	if(set!=null)
	        		set.close() ;
        	}
	        catch (Exception e)
	        {
	        	System.out.print("\nerror when close conn and stmt!") ;
	        }
        }
        
        
        return sb.toString() ;
    }
    
    
    /*
     *
     *
     *
     */
    public String getuserGnqx( HttpServletRequest request ){
    	String xtmc = null ;
    	String username = null ;
    	Connection conn = null ;
        Statement stmt = null;
        ResultSet rs = null;
        String str_qxs ="";
    	System.out.println("\nttttttttt") ;
		try{
			conn = wzw.util.DbUtils.getConnection( request ) ;
			xtmc = request.getParameter("xtmc") ;
			username = request.getParameter("selUser") ;
			stmt = conn.createStatement() ;
			rs = stmt.executeQuery("SELECT GVUSGN_QXBH FROM GVUSGN WHERE GVUSGN_XTMC='"+xtmc+"' AND GVUSGN_USER='"+username+"'" ) ;
    	
    		while(rs.next())
				str_qxs+= rs.getString("GVUSGN_QXBH") + ',' ;

			if(!str_qxs.equals(""))
				str_qxs = str_qxs.substring(0, str_qxs.length()-1 ) ; 
				
		}catch(SQLException ex)
		{
			System.out.print("\ncan't get connection!") ;
		}
        finally
        {
	        try
	        {
	        	if(rs!=null)
	        		rs.close() ;
	        	if(stmt!=null)
	        		stmt.close() ;
	        	if(conn!=null)
	        		conn.close() ;
        	}
	        catch (Exception e)
	        {
	        	System.out.print("\nerror when close conn and stmt!") ;
	        }
        }
		
		System.out.print("\nqxList = "+ str_qxs ) ;
    	return str_qxs;
    }
    
    
}
