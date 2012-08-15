package wzw.beans;

import java.io.InputStream;
import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.CachedRowSet;

import oracle.jdbc.driver.OracleResultSet;
import oracle.sql.BLOB;

import org.apache.log4j.Logger;
import org.apache.struts.Globals;
import org.apache.struts.action.ActionError;
import org.apache.struts.action.ActionErrors;

import wzw.sql.SqlUtils;
import wzw.util.DbUtils;
import wzw.util.XmlUtils;
/*
 *   从DBBean.java 文件中删除 on 2005-11-24
 */
public class DeleteFromDBBean {
    /**
     * 保存错误信息的对象。 
     */
    public final ActionErrors errors = new ActionErrors();
	/**
	 *	适配一个保存数据库查询结果集的对象。//,mySet,mySet1,mySet2,mySet3
	 */
    public CachedRowSet crs;
    /**
     *	系统查询结果集缓存数，在system-datasource-conf.xml文件中配置，小于1表示不缓存。
     */
	private static int QUERY_ROWSET_CACHE = -1 ;
	//protected Statement myStmt,inerStmt;
	//public Connection myConn=null;
	//private String table_id,field_id,listname;
	//public OracleResultSet ors  ;
	//public CachedRowSet myCcrs

    /**
     *	log4j日志对象。
     */
	private final static Logger log = Logger.getLogger( DbBean.class) ;
	//public static Log log = LogFactory.getLog(DBBean.class);

	/**
	 *	请求对象。
	 */
	private HttpServletRequest request = null;
	/**
	 *	页面显示数据库查询结果集的参数。
	 */
	public int[] pageInfo ;
	//当前行号(游标指针)
	public int currentRowNumber=0;		

 


	/*
	 *	get a Connection instance.
	 */
	public Connection getConnection()
			throws SQLException{
		return DbUtils.getConnection() ;
	}

	/**
	 * get a Connection instance.
     * @param request 请求对象。
	 */
	public Connection getConnection(HttpServletRequest request)
        						throws SQLException {
		return DbUtils.getConnection(request) ;
    }

    /**
     * transformate string to oracle string
     * @param s String
     * @throws Exception
     * @return String
     */
    public String  toOracleString(String s) throws Exception{
		 if (s==null || s.length()<1) return s;
		 return s.replaceAll("'","''");
		}

	public String getCurdate(int type,String dateStr,int stype) throws Exception {
		String ret;
		SimpleDateFormat dateFmt;
		//System.out.print("\n getCurdate()") ;
		if (stype==1) dateFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		else dateFmt = new SimpleDateFormat("MM/dd/yyyy",Locale.getDefault());
		//System.out.print("\n+MM/dd/yyyy" + dateStr + "stype="+type) ;
		java.util.Date curD = dateFmt.parse(dateStr);

		switch (type){
		  case 1:  dateFmt = new SimpleDateFormat("yyyyMMdd",Locale.getDefault());
		             ret = dateFmt.format(curD);
		             break;
		  case 2:  dateFmt = new SimpleDateFormat("yyyy.MM.dd",Locale.getDefault());
		             ret = dateFmt.format(curD);
		             break;
		  case 3:  dateFmt = new SimpleDateFormat("yyyy-MM-dd",Locale.getDefault());
		             ret = dateFmt.format(curD);
		             break;
		  default: ret = "";
		}
		return ret;
	}

	public String getBlobValue(CachedRowSet crs,int index) throws Exception {
		String ret="";

		//ResultSet rs = null ;
		//rs = aSet.getOriginal() ;
		//if(rs==null)
		//	System.out.print("\ncan't get the origian resultset");
		//else
		//	System.out.print("\nget the origian resultset ok");
		//
		ResultSet rs = null ;
		rs = crs.getOriginal() ;
		  BLOB blob = ((OracleResultSet)rs).getBLOB(index);
		  //BLOB blob = null ;

		  if (blob!=null){
		       InputStream in = blob.getBinaryStream();
		      int bufferSize = blob.getBufferSize();
		      byte[] buffer = new byte[bufferSize];
		      int bytesRead = 0;
		      in.read(buffer);
		       ret = new String(buffer);
		       in.close();
		  }
		  rs.close() ;
		return ret;
	}


////////////////////////////////////////////////////////
//
// 	The methodes below are just for query.
//
//
///////////////////////////////////////////////////////
    /**
     * 查询数据
     * @param request HttpServletRequest
     * @param sqlString 要执行的SQL语句，eg：select a,b,c from mytable
     * @throws Exception
     * @return CachedRowSet对象
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
						     String sqlString )throws Exception {
          return doQuery(request,
          			     sqlString,
						 10);
	}

    /**
     * 查询数据
     * @param request HttpServletRequest
     * @param sqlString 要执行的SQL语句，eg：select a,b,c from mytable
     * @param pageInfor 每页行数
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int pageInfor)throws Exception {
        int[] pInfo= new int[4];
			pInfo[0] = pageInfor;
        return doQuery( request,
                        sqlString,
                        pInfo) ;
    }

    /**
     * 查询数据
     * @param request HttpServletRequest
     * @param sqlString 要执行的SQL语句，eg：select a,b,c from mytable
     * @param pageInfor int[] 页面信息，[0]表示每页行数
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
						   String sqlString,
						   int[] pageInfor)throws Exception {

		return doQuery( request,
						sqlString,
						pageInfor,
						false) ;
	}

    /**
     * 查询数据
     * @param request HttpServletRequest
     * @param sqlString 要执行的SQL语句，eg：select a,b,c from mytable
     * @param pageInfor 页面信息，[0]表示每页行数，其他总页数，总行数
     * @param isBuffered 是否缓存查询对象集合
     * @throws Exception
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int pageInfo,
                           boolean isBuffered)throws Exception {
        int[] pInfo= new int[4];
			pInfo[0] = pageInfo;
		return doQuery(request,
                       sqlString,
                       pInfo,
                       isBuffered) ;
        }
    /**
     * 查询数据
     * @param request HttpServletRequest
     * @param sqlString 要执行的SQL语句，eg：select a,b,c from mytable
     * @param pageInfor 页面信息，[0]表示每页行数，其他总页数，总行数
     * @param isBuffered 是否缓存查询对象集合
     * @throws Exception
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int[] pageInfo,
                           boolean isBuffered)throws Exception {
              return doQuery(request,
                             sqlString,
                             pageInfo,
                             null,
                             isBuffered) ;
   }

    /**
     * 查询数据
     * @param request HttpServletRequest
     * @param sqlString 要执行的SQL语句，eg：select a,b,c from mytable
     * @param pageInfor 页面信息，[0]表示每页行数，其他总页数，总行数
     * @param pageControl 控制信息，当前第一页，共几页等等
     * @throws Exception
     * @return CachedRowSet
     */
    public  CachedRowSet doQuery(HttpServletRequest request,
                           String sqlString,
                           int[] pageInfo,
                           String[] pageControl)throws Exception {
              return doQuery(request,
                             sqlString,
                             pageInfo,
                             pageControl,
                             false) ;
        }

    /**
     * 查询数据，返回crs，减少对象的创建，而且灵活性更好！
     * @param request HttpServletRequest
     * @param sqlString 要执行的SQL语句，eg：select a,b,c from mytable
     * @param pageInfor 页面信息，[0]表示每页行数，其他总页数，总行数
     * @param pageControl 控制信息，当前第一页，共几页等等
	 * @param isBuffered 是否使用缓存，不是所有都缓存，默认为false。
	 * @exception no exception
     * @return CachedRowSet
	 */
	public  CachedRowSet doQuery(HttpServletRequest request,
						String sqlString,
						int[] pageInfo,
						String[] pageControl,
                        boolean isBuffered)throws Exception {

		this.pageInfo = pageInfo ;
		String action = request.getParameter("Action") ;
		if (request.getParameter("PageNumber") == null || request.getParameter("PageNumber").trim().equals(""))
			pageInfo[1] = 1;
		else
			pageInfo[1] = Integer.parseInt(request.getParameter("PageNumber"));

		log.debug("doQuery(), sql="+sqlString) ;
		int i=0,j=0,k=0;
		int pageSum=0,pagePrior,pageNext;
		int pageSize=pageInfo[0];
		int pageNumber=pageInfo[1];		//当前页号
		String s="";
		//Vector ret = null;

		//如果请求是翻页或者排序等操作则；取缓存,如果使用缓存的话
		if( (isBuffered)&&(action!=null) && (action.equals("changePage")||action.equals("sort")) ){
			//如果缓存中没有，则访问数据库，同时缓存
			if( !this.getCachedRowSetFromCache(request,sqlString) ){
				doInnerQuery(request, sqlString);
				setCachedRowSetToCache(request,sqlString,this.crs ) ;
			}
		}else{
			doInnerQuery(request, sqlString);
            if(isBuffered){
                setCachedRowSetToCache(request, sqlString, this.crs);
            }
		}
		//如果缓存中有，游标指针要回到 beforeFirst 处
		this.crs.beforeFirst() ;

		//System.out.print("\n doQuery()"+crs.size()) ;
		if (!crs.next()){
			pageInfo[1] = 0;
			pageInfo[2] = 0;
			pageInfo[3] = 0;
			//s = "共0条记录 第0/0页 ";
			//pageControl[0] = s;			//pageControl在被去掉
			return null;		//crs;
		}

		//ret = new Vector();
		crs.last();
		k = crs.getRow();
		pageInfo[3] = k;//记录总记录条数
		double f = (double) k;
		pageSum= (int)Math.ceil(f/pageSize);
		i = pageNumber;
		if (pageNumber>pageSum) i = pageSum;
		if (pageNumber<1) i = 1;
		pageNumber = i;

		pagePrior = pageNumber - 1;
		pageNext = pageNumber + 1;
		if (pagePrior<1) pagePrior = 1;
		if (pageNext>pageSum) pageNext = pageSum;

		// wuzewen 将结果集中需要显示的数据放到一个Vector中去，
		// 页面根据 Vector来生成页面数据。
		// Vector 内容来自 CachedRowset的一部分
		// 问题是：如果不是String类型，如果获取值到Vector中去？
		// 也可以直接使用CachedRowset 对象，不使用Vector。
		// 以上一点还要考虑确and size定

		// 证明 Integer类型的列，一样可以放到Vector中，
		// 只是页面在取Vector中的值时，仍然使用(String)v.get(0),
		//     而不是(Integer)get(0),不知其它如Double，Date是否也一样可行



		//wzw on 2005-11-28 去掉返回Vector，直接返回CachedRowSet的引用
		//减少对象的创建，提高性能
		//i=0;
		//int columnCount = crs.getMetaData().getColumnCount();
		//do{
		//	Vector v = new Vector();
		//	for (j=1;j<=columnCount;j++)
		//		v.add(crs.getString(j));
		//	ret.add(v);
		//}while ((++i<pageSize)&&(crs.next()));

		this.currentRowNumber = (pageNumber-1)*pageSize;

		//wzw on 2005-11-28 去掉下面的代码
		//while ((j<(pageNumber-1)*pageSize)&&(crs.next())) j++;
		//if (this.currentRowNumber>0)
		//	crs.absolute(this.currentRowNumber+1);
		//else{
		//	crs.first();
		//}

		pageInfo[1] = pageNumber;
		pageInfo[2] = pageSum;

		//无论使用Vector还是直接使用CachedRowset，游标都复位
		if (this.currentRowNumber>0) {
			crs.absolute(this.currentRowNumber);
		}
		else
			crs.beforeFirst() ;

		// wuzewen 页面导航信息由新的函数产生，此处不再使用
		//s = "共" + Integer.toString(k) + "条记录 &nbsp;每页" + Integer.toString(pageSize) + "条记录  &nbsp;&nbsp;&nbsp;当前第" + Integer.toString(pageNumber) + "/" + Integer.toString(pageSum) + "页 ";
		//if (pageNumber>1)
		//  s = s + "<a href=\"javascript:gotoPage(" + Integer.toString(pagePrior) + ")\">前一页</a> ";
		//if (pageNumber!=pageSum)
		//  s = s + "<a href=\"javascript:gotoPage(" + Integer.toString(pageNext) + ")\">后一页</a> ";
		//s = s + "跳转到&nbsp;<input type=text class='input' size=3 name='inputpage' maxlength=3>&nbsp;页&nbsp;" +
		//  "<b><a href='javascript:jumpToPage()'>GO</b></a>";
		//pageControl[0] = s;
		return this.crs;
	}

	/**
     *	获取数据库连接，执行SQL语句，ResutSet初始CachedRowSet
     */
	public void doInnerQuery(HttpServletRequest request, String s) throws Exception {

		//log.debug("doInnerQuery :sql = " +s) ;
		Connection conn = this.getConnection(request) ;
		Statement stmt = conn.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,
                          					ResultSet.CONCUR_READ_ONLY) ;
		ResultSet rs = null ;
		rs = stmt.executeQuery( s ) ;
		//if (rs.next())
		//{
		//	System.out.print("\ndo inerQuery " + rs.getString(1)) ;
		//}else
		//{
		//	System.out.print("\ndo inerQuery ResultSet.") ;
		//}
		//rs.beforeFirst() ;

		crs.populate( rs ) ;

		//System.out.println (conn.getClass().toString());
		if(rs!=null){
			rs.close();
		}
		if(stmt!=null){
			stmt.close();
		}
		if(conn!=null){
			conn.close();
		}
	}


////////////////////////////////////////////////////////
//
// 	The methodes below are just for update.
//	 include Execute an SQL INSERT, UPDATE, or DELETE sql statement.
//
//
///////////////////////////////////////////////////////
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql) throws SQLException {
        return this.doUpdate(request, sql, (Object[]) null);
    }    
    
    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query without replacement
     * parameters.
     *
     * @param sql The SQL to execute.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate( String sql) throws SQLException {
        return DbUtils.executeUpdate( sql );
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query with a single replacement
     * parameter.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param param The replacement parameter.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql, Object param)
        throws SQLException {

        return this.doUpdate(request, sql, new Object[] { param });
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql, Object[] params)
        throws SQLException {

		Connection conn = null ;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
        	conn = getConnection( request ) ;
            stmt = conn.prepareStatement( sql);
            SqlUtils.fillStatement(stmt, params);

            rows = stmt.executeUpdate();
        	//log.fatal("fillStatement3");
        	conn.commit() ;

        } catch (SQLException e) {
            conn.rollback();
        	log.error("DBUtil.doUpdate :"+ e.getMessage() ) ;
            //this.rethrow(e, sql, params);

        } finally {
        	try{
        		org.apache.commons.dbutils.DbUtils.close(stmt);

            }catch(SQLException e)
            {
	            log.fatal("在执行UpdateBean.doUpdate() 出错（Exception），错误信息为：\n", e);
	            //this.addErrors(new ActionError("error.database.deal"));
            }
        	try{
            	conn.close() ;

            }catch(SQLException e)
            {
	            log.fatal("fail when close Connection!", e);
	            //this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return rows;
    }

    /**
     * Execute an SQL INSERT, UPDATE, or DELETE query.
     *
     * @param conn The connection to use to run the query.
     * @param sql The SQL to execute.
     * @param params The query replacement parameters.
     * @return The number of rows updated.
     * @throws SQLException
     */
    public int doUpdate(HttpServletRequest request, String sql, List list)
        throws SQLException {

		Connection conn = getConnection( request ) ;
        PreparedStatement stmt = null;
        int rows = 0;

        try {
            stmt = conn.prepareStatement( sql);
            SqlUtils.fillStatement(stmt, list);

            rows = stmt.executeUpdate();
            conn.commit() ;
        	//log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            log.fatal(this.getClass().getName()+" "+ e.getMessage() ) ;
            conn.rollback();
            //this.rethrow(e, sql, list);

        } finally {
        	try{
            	if(stmt!=null)
            		stmt.close() ;
            	//DbUtils.close(stmt);
            	if(conn!=null)
            		conn.close() ;

            }catch(SQLException e)
            {
	            log.fatal("在执行UpdateBean.doUpdate() 出错（Exception），错误信息为：\n", e);
	            this.addErrors(new ActionError("error.database.deal"));
            }
        }

        return rows;
    }


    /**
     * Execute a batch of sql statements.
     * @param request a http request object.
     * @param allsql 要执行的sql语句组成的数组。
     * @throws 执行批处理失败。
     * @return 每个sql语句影响的行数组成的数组。
     */
    public int[] doBatch(HttpServletRequest request, String[] allsql)
        throws SQLException {

        Connection conn = getConnection( request ) ;
        PreparedStatement pstmt = null;
        String sql=null;
        int returns[];
        try {
            pstmt = conn.prepareStatement( sql);
            for(int i=0;i<allsql.length;i++){
                pstmt.addBatch(allsql[i]);
            }

            returns = pstmt.executeBatch();
            conn.commit() ;
            return returns;
            //log.debug("doUpdate commit success!");

        } catch (SQLException e) {
            conn.rollback();
            log.fatal(this.getClass().getName()+" "+ e.getMessage() ) ;
            //this.rethrow(e, sql, list);

        } finally {
            try{
                if(pstmt!=null)
                    pstmt.close() ;
                //DbUtils.close(stmt);
                if(conn!=null)
                    conn.close() ;
            }catch(SQLException e)
            {
                log.fatal("在执行UpdateBean.doUpdate() 出错（Exception），错误信息为：\n", e);
                this.addErrors(new ActionError("error.database.deal"));
            }
        }
        return null;
    }


    /**
     * Execute a batch of sql statements.
     * @param request a http request object.
     * @param List 要执行的sql语句组成的实现了List接口的对象。
     * @throws 执行批处理失败。
     * @return 每个sql语句影响的行数组成的数组。
     */
    public int[] doBatch(HttpServletRequest request, List allsql)
        throws SQLException {
        if(allsql==null){
            return null;
        }
        return doBatch(request, (String[])allsql.toArray());
    }

    /**
     * 执行SQL语句的批处理。
     * @param allsql sql语句集合对象
     * @return 每个sql语句修改的行数的数组
     * @throws SQLException sql语句执行异常
     */
    public int[] doBatch( List allsql)
        throws SQLException {
    	
        return DbUtils.executeBatch( allsql );
    }

	/**
	 *
	 * 吴泽文 2005-07-16
	 * //不是每次请求都要查询数据库，对于翻页，排序等等
	 * //只要查询的sql语句一样，并不用每次都查询数据库
	 * //只有这样才能提高效率
	 *
	 * //因为javabean放在scope=page|request中都不能缓存数据，
	 * //因为已经是两个不同的 请求了，放到scope=session|application中,
	 * //又汇加重服务器端消耗代价
	 *
	 * //此处的解决方法是采用session中放置一个对象，缓存几个(5)结果集
	 * //这样既可以缓存，又不缓存太多，在两缓存于数据库频繁访问间找到平衡
	 *
	 *	建议：缓存只用于翻页和排序的操作，对于第一次查询，不能总是缓存
	 *	   不过也可以对所有查询(翻页和排序之外的)缓存，
	 *		实际上超过 5个(可定义)就会被踢出缓存，最好的办法是：
	 *	 点击XXX信息查询放入结果集到缓存，而不是去缓存中取
	 *       翻页、排序从缓存取结果集，无论又有就取，没有就查询再放
	 *
	 *   可以考虑放到 scope=application中去，应用的所有用户共享缓存
	 *   modify: 放到 scope=application中去了，整个应用的用户共享缓存
	 */
	public boolean getCachedRowSetFromCache(HttpServletRequest request,
						String sqlString ){
		Hashtable ht = new Hashtable(5) ;
		ht = (Hashtable)request.getSession().getServletContext().getAttribute("CachedRowCache") ;
		log.debug( "getCachedRowSetFromCache ht = " + ht) ;

		if(ht==null)
			return false ;
		log.debug ("当前缓存查询对象数="+ht.size()) ;
		if(ht.get(sqlString)!=null){
			log.debug( "contains in cache" ) ;
			this.crs = (CachedRowSet)ht.get(sqlString) ;
			return true ;
		}
        log.debug( "not contains in cache" ) ;
		return false ;
	}
	/**
	 * WUZEWEN 2005-07-16
	 *
     *	将取得的RowCachedRowSet 放入 session的Cache对象中去
     *  修改为 放入 application 的Cache对象中去
     */
    public void setCachedRowSetToCache(HttpServletRequest request,
						String sqlString,Object obj){
		Hashtable ht = new Hashtable() ;

		//如果缓存数还没有确定，取缓存数！
		//<0:没取值；==0:不缓存；>0已取得的缓存值；
		if(this.QUERY_ROWSET_CACHE<0){
	        Vector vec = new Vector() ;
	        vec.add( "date-cache@query-rowset-cache" ) ;
	        vec = XmlUtils.getElementValues(getClass().getResourceAsStream("/conf/system-datasource-conf.xml"),vec) ;
	        if(vec == null ){
	        	//throw new FileNotFoundException("对应的数据库配置文件不存在！") ;
	        	log.error("对应的数据库配置文件不存在！") ;
	        	this.QUERY_ROWSET_CACHE = 5 ;   //默认5个
	        }else{
	        	this.QUERY_ROWSET_CACHE = Integer.parseInt( vec.elementAt(0).toString() ) ;
	        }
	        log.debug("获取查询数据缓存数："+this.QUERY_ROWSET_CACHE) ;
		}

		ht = (Hashtable)request.getSession().getServletContext().getAttribute("CachedRowCache") ;

		if(ht!=null){
	    	//log.debug("before 获取查询数据缓存数："+ht.size()) ;
			//如果已经有了，移除:  不用移除，hashtable自动覆盖旧值。
			//if(ht.get(sqlString)!=null){
			//	ht.remove(sqlString) ;
			//}

			//如果满了设置的最大数，移除第一个
			if( ht.size()>= this.QUERY_ROWSET_CACHE )
				ht.remove(ht.keys().nextElement()) ;
			//存入缓存中
			ht.put( sqlString, obj ) ;
			log.debug ("set in cache and then size="+ht.size() ) ;

		}else{
			//log.debug ("set 1 !") ;
			ht = new Hashtable() ;
			ht.put( sqlString, obj ) ;
		}
		request.getSession().getServletContext().setAttribute("CachedRowCache",ht) ;
		log.debug ("当前缓存查询对象数="+ht.size()) ;
	}

	/**
     *	获取页数，行数，翻页信息
     *此处必须有js文件中的String.trim() 函数定义的支持。
     *
     *  wuzewen
     *  2005-07-16
     *
     */
	public String pageNavigator(){
	    StringBuffer sb = new StringBuffer();
		String action = "changePage";
		sb.append("<table valign=bottom width=\"100%\" border=0 cellspacing=\"0\" cellpadding=\"0\" class=\"tableBorder\">")
	      .append("<tr valign=bottom>")
	      .append("<td class=\"HeaderBG\" width=\"25%\">")
	      .append("&nbsp;共"+pageInfo[3]+"条记录&nbsp;&nbsp;")
	      .append("每页"+pageInfo[0]+"条</td>")
	      .append("<td valign=bottom class=\"HeaderBG\" width=\"75%\" align=\"right\">")
	      .append("第" + String.valueOf(pageInfo[1]) + "页，")
	      .append("共" + String.valueOf(pageInfo[2]) + "页&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;")
	      .append( (pageInfo[1]==1)?"[首 页]":"<a href=\"?Action=changePage&PageNumber=1\">[首 页]</a>")
	      .append("|")
	      .append( (pageInfo[1] == 1)?"[上一页]":"<a href=\"?Action=changePage&PageNumber=" + String.valueOf(pageInfo[1] - 1) + "\">[上一页]</a>")
	      .append("|")
	      .append((pageInfo[1] == pageInfo[2])?"[下一页]":"<a href=\"?Action=changePage&PageNumber=" + String.valueOf(pageInfo[1] + 1) + "\">[下一页]</a>")
	      .append("|")
	      .append( (pageInfo[1] == pageInfo[2])?"[末 页]":"<a href=\"?Action=changePage&PageNumber=" + String.valueOf(pageInfo[2]) + "\">[末 页]</a>")
	      .append("&nbsp;&nbsp;转到：<input type=\"text\" name=\"PageNumber\" size=\"2\" onkeypress=\"return(!event.shiftKey&amp;&amp;event.keyCode>47 &amp;&amp; event.keyCode<58 )\">&nbsp;")
	      .append("<input class=input type=\"button\" value=\"查看\" onclick=\"javascript:if(document.all.PageNumber.value.trim()=='') return;if(document.all.PageNumber.value.trim()=="+String.valueOf(pageInfo[1])+") return;window.location.href='?Action=changePage&PageNumber='+document.all.PageNumber.value;\">")
	      .append("</td>")
	      .append("</tr>")
	      .append("</table>") ;

		return sb.toString() ;
	}


	/*
	 *  set the value of request parameter from the page
	 */
	public void setRequest( HttpServletRequest request )
	{
		//System.out.print("\n setRequest()") ;
		this.request = request ;
	}
	/*
	 *  get request
	 */
	public HttpServletRequest getRequest( )
	{
		return this.request ;
	}


    /**
     * Throws a new exception with a more informative error message.
     *
     * @param cause The original exception that will be chained to the new
     * exception when it's rethrown.
     *
     * @param sql The query that was executing when the exception happened.
     *
     * @param params The query replacement paramaters; <code>null</code> is a
     * valid value to pass in.
     *
     * @throws SQLException
     */
    protected void rethrow(SQLException cause, String sql, List list)
        throws SQLException {

        StringBuffer msg = new StringBuffer(cause.getMessage());

        msg.append(" Query: ");
        msg.append(sql);
        msg.append(" Parameters: ");

        if (list == null) {
            msg.append("[]");
        } else {
            msg.append( list ) ;
        }

        SQLException e = new SQLException(msg.toString());
        e.setNextException(cause);

        throw e;
    }


////////////////////////////////////////////////////////
//
// 	The methodes below are just for error and message!!! ; ;
//  zewenWoo 2004.09.16
//
///////////////////////////////////////////////////////
	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
    public void addErrors(String key)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, new ActionError(key));
    }
    public void clearErrors(){
    	errors.clear();
    }

	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
    public void addErrors(ActionError error)
    {
        addErrors(ActionErrors.GLOBAL_ERROR, error);
    }
	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
    public void addErrors(String property, ActionError error)
    {
        errors.add(property, error);
    }
	/*
	 *	zewenWoo 2004.09.16
	 *
	 *
	 */
	public void saveErrors(HttpServletRequest request){
		request.setAttribute(Globals.ERROR_KEY ,this.errors) ;

	}

    public void addMessages(String key)
    {

    }


////////////////////////////////////////////////////////
//
// 	以下部分相当于QueryAction执行查询返回的一个
//		QueryDataSet 类对象，都是对内部的 crs的操作
//		参考 com.kingcore.framework.bean.QueryDataSet
//
///////////////////////////////////////////////////////
	/**
     * @deprecated to do something
     * @author WUZEWEN on 2005-07-17
     * @param String a
     * @param String b
     * @return String c
     * @exception no exception
     */
    public boolean nextRowInPage() throws SQLException{
    	//如果到了页尾或者数据极尾，返回 false
    	if(this.currentRowNumber>=this.crs.size() ||
    	   		this.currentRowNumber>=pageInfo[0]*pageInfo[1]){
    		return false ;
    	}
    	//游标指向下一行
    	this.currentRowNumber++ ;
    	if(this.currentRowNumber>this.crs.size()){
    		this.currentRowNumber= this.crs.size();
    	}
    	return this.crs.next() ;
    }


	/**
     *  获取行的序号
     *  wuzewen 2005-07-17
     *
     *
     */
	public void getRowNumber(){
		//return pageInfo[0]*(pageInfo[1] - 1) + i + 1  ;
	}

////////////////////////////////////////////////////////
//
//	下面是采用适配器设计模式，都是调用CachedRowSet 对象的相应的方法。
//
//
///////////////////////////////////////////////////////
	public boolean absolute( int row) throws SQLException
	{
		return ( crs.absolute( row ) ) ;
	}

	public void beforeFirst() throws SQLException
	{
		crs.beforeFirst() ;
	}
	public void afterLast() throws SQLException
	{
		crs.afterLast() ;
	}
	public boolean first() throws SQLException
	{
		return (crs.first() ) ;
	}
	public boolean last() throws SQLException
	{
		return (crs.last() ) ;
	}

	public boolean previous() throws SQLException
	{
		return (crs.previous() ) ;
	}
	public boolean next() throws SQLException
	{
		return (crs.next() ) ;
	}

	/**
	 *	返回总的行数。
	 */
	public int size()
	{
		return ( crs.size() ) ;
	}
	/**
     * 返回当前行号，替换直接使用DBBean的currentRowNumber属性来获取当前行的方法。
     * @author WUZEWEN on 2005-12-10
     * @return the current row number; 0 if there is no current row.
	 */
	public int getRow() throws SQLException
	{
		return ( crs.getRow() ) ;
	}

	/**
	 *	BigDecimal 的geter 方法。
	 */
	public BigDecimal getBigDecimal( int colNum ) throws SQLException
	{
		return ( crs.getBigDecimal( colNum ) ) ;
	}

	public BigDecimal getBigDecimal( String colName ) throws SQLException
	{
		return ( crs.getBigDecimal( colName)) ;
	}

	/**
	 *	Blob 的geter 方法。
	 */
	public Blob getBlob( int colNum) throws SQLException
	{
		return ( crs.getBlob( colNum )) ;
	}

	public Blob getBlob( String colName ) throws SQLException
	{
		return (crs.getBlob( colName )) ;
	}

	/**
	 *	Boolean 的geter 方法。
	 */
	public boolean getBoolean( int colNum) throws SQLException
	{
		return ( crs.getBoolean( colNum) ) ;
	}

	public boolean getBoolean( String colName) throws SQLException
	{
		return ( crs.getBoolean( colName ) ) ;
	}


	/**
	 *	Byte 的geter 方法。
	 */
	public byte getByte( int colNum ) throws SQLException
	{
		return ( crs.getByte( colNum ) ) ;
	}
	public byte getByte ( String colName ) throws SQLException
	{
		return ( crs.getByte( colName ) ) ;
	}


	/**
	 *	Byte[] 的geter 方法。
	 */
	public byte[] getBytes( int colNum ) throws SQLException
	{
		return ( crs.getBytes( colNum ) ) ;
	}
	public byte[] getBytes( String colName )  throws SQLException
	{
		return ( crs.getBytes( colName ) ) ;
	}

	/**
	 *	CharacterStream 的geter 方法。

	public CharacterStream getCharacterStream( int colNum)
	{
		return ( crs.getCharacterStream( colNum) ) ;
	}

	public CharacterStream getCharacterStream( String colName)
	{
		return ( crs.getCharacterStream( colName) ) ;
	}
	*/

	/**
	 *	Clob 的geter 方法。
	 */
	public Clob getClob( int colNum) throws SQLException
	{
		return ( crs.getClob( colNum ) ) ;
	}
	public Clob getClob ( String colName)  throws SQLException
	{
		return ( crs.getClob( colName ) ) ;
	}

	/**
	 *	Date 的geter 方法。
	 */
	public java.util.Date getDate( int colNum) throws SQLException
	{
		return ( crs.getDate( colNum ) ) ;
	}

	public java.util.Date getDate( String colName) throws SQLException
	{
		return ( crs.getDate( colName ) ) ;
	}

	/**
	 *	Double 的geter 方法。
	 */
	public double getDouble( int colNum ) throws SQLException
	{
		return ( crs.getDouble( colNum ) ) ;
	}
	public double getDouble( String colName ) throws SQLException
	{
		return ( crs.getDouble( colName ) ) ;
	}

	/**
	 *	Double 的geter 方法。
	 */
	public float getFloat( int colNum) throws SQLException
	{
		return ( crs.getFloat( colNum ) ) ;
	}
	public float getFloat( String colName) throws SQLException
	{
		return ( crs.getFloat( colName ) ) ;
	}

	/**
	 *	Integer 的geter 方法。
	 */
	public int getInt( int colNum) throws SQLException
	{
		return ( crs.getInt( colNum )) ;
	}
	public int getInt( String colName) throws SQLException
	{
		return ( crs.getInt( colName )) ;
	}

	/**
	 *	Long 的geter 方法。
	 */
	public long getLong( int colNum ) throws SQLException
	{
		return ( crs.getLong( colNum ) ) ;
	}
	public long getLong( String colName ) throws SQLException
	{
		return ( crs.getLong( colName ) ) ;
	}

	/**
	 *	Object 的geter 方法。
	 */
	public Object getObject( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object getObject( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}

	/**
	 *	抽象的 的geter 方法。
	 */
	public Object get( int colNum) throws SQLException
	{
		return ( crs.getObject( colNum) ) ;
	}
	public Object get( String colName)  throws SQLException
	{
		return ( crs.getObject( colName ) ) ;
	}


	/**
	 *	Short 的geter 方法。
	 */
	public short getShort( int colNum ) throws SQLException
	{
		return ( crs.getShort( colNum )) ;
	}
	public short getShort( String colName )  throws SQLException
	{
		return ( crs.getShort( colName ) ) ;
	}

	/**
	 *	String 的geter 方法。
	 */
	public String getString( int colNum) throws SQLException
	{
		return ( crs.getString( colNum ) ) ;
	}
	public String getString( String colName ) throws SQLException
	{
		return ( crs.getString( colName ) ) ;
	}

	/**
	 *	Time 的geter 方法。
	 */
	public Time getTime( int colNum ) throws SQLException
	{
		return ( crs.getTime( colNum ) ) ;
	}
	public Time getTime( String colName ) throws SQLException
	{
		return ( crs.getTime( colName ) ) ;
	}

	/**
	 *	Timestamp 的geter 方法。
	 */
	public Timestamp getTimestamp( int colNum ) throws SQLException
	{
		return ( crs.getTimestamp( colNum ) ) ;
	}

	public Timestamp getTimestamp( String colName ) throws SQLException
	{
		return ( crs.getTimestamp( colName ) ) ;
	}

	//说明：其他的get方法需要再添加。



//		QueryDataSet 类对象，都是对内部的 crs的操作
//		参考 com.kingcore.framework.bean.QueryDataSet
//	QueryDataSet 功能结束！！


////////////////////////////////////////////////////////
//
// 	get Image object
//  zewenWoo 2004.09.21
//
///////////////////////////////////////////////////////
	/*
	public void getImage(javax.servlet.http.HttpServletResponse response){
		ResultSet rs ;
		rs.next() ;
		String dim_image = rs.getString("photo") ;
		byte[] blocco = rs.getBytes("photo");
		response.setContentType("image/jpeg") ;
		javax.servlet.ServletOutputStream op = response.getOutputStream() ;
		for(int i=0; i<Integer.parseInt(dim_image); i++){
			op.write(blocco[i]) ;
		}


	}
	*/
}
