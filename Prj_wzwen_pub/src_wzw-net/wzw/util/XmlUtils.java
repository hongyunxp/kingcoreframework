/**
 * Copyright (C) 2002-2005 ChangSha WUZEWEN. All rights reserved.
 * WZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;


/**
 * <BLOCKQUOTE>对XML文件处理工具类。</BLOCKQUOTE>
 * @author	zewen.wu on 2005.03.19
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK1.4
 */

public class XmlUtils {
    /**
     * log4j日志对象。
     */
    public static Logger log = Logger.getLogger( XmlUtils.class);


	/**
     * 从xml文件中取多个值
	 * @author WUZEWEN
     * @version V1.0.0(2005-07-24)
	 * @param  InputStream is--文件输入流对象
	 * @param  Vector vec_str--需要取的xml的节点路径集
     * @return Vector对象,取到的节点的值的集
     * @throws no Exception throw
	 */
	public static Vector<String> getElementValues(java.io.InputStream inStream,Vector<String> vec_str){

		Vector<String> result = new Vector<String>() ;
		try{
			if(vec_str==null) return null ;
			if(inStream==null ) return null ;

			StringTokenizer st = null ;
			String params = null ;
			String noteName="",noteAttr="",attrValue="";

			//create a new Document
			SAXBuilder builder = new SAXBuilder(false);
			//得到Document
			Document doc = builder.build( inStream );
			//得到根节点LIT:StuInfo
			Element element = null ;

			Iterator<String> it = vec_str.iterator() ;
			List<?> list =null;
			while(it.hasNext()){
				element = doc.getRootElement();
				params = it.next() ;  //(String)
				//System.out.println("params="+params) ;
				st = new StringTokenizer(params,"@") ;
				//取得修要的节点
				while( st.hasMoreTokens()) {
					//System.out.println( element.getName() ) ;
					noteName= st.nextToken();
					if(noteName.indexOf("[")>0){
						//System.out.println (noteName);
						noteAttr =noteName.substring(noteName.indexOf("[")+1,noteName.indexOf("="));
						attrValue=noteName.substring(noteName.indexOf("=")+1,noteName.indexOf("]"));
						noteName =noteName.substring(0,noteName.indexOf("["));
						list = element.getChildren( noteName ) ;
						//System.out.println (noteName+" "+noteAttr+" "+attrValue);
						for(int i=0;i<list.size();i++){
							element=(Element)list.get(i);
							if(element.getAttribute(noteAttr)!=null
								&&element.getAttribute(noteAttr).getValue().equals(attrValue))
								break;
						}
					}else{
						element = element.getChild(noteName) ;	//只有一个child,多个可不可以得到第一个？
					}
					//element = element.getChild( st.nextToken() ) ;
					//System.out.println( element.getName() ) ;
				}
				if( element.getAttributeValue("value") == null )
					result.add( element.getText() ) ;
				else
					result.add( element.getAttributeValue("value") ) ;

				//System.out.println( element.getAttributeValue("value") ) ;
			}


			//List menu01s = elmtStuInfo.getChildren("menu");
			//p( "next menu is empty! " +menu01s.size()) ;

			//修改bigmouse的CAD分数
			//for (int i = 0; i < menu01s.size(); i++)
			//{

		}catch(JDOMException ex){
			log.fatal("faile to parse xml file!"+ex.toString() ) ;
		}catch(Exception ex){
			log.fatal("faile to parse xml file!"+ex.toString() ) ;
		}

		return result ;

	}

	/**
     * 从xml文件中取多个值
	 * @author WUZEWEN
     * @version V1.0.0(2005-03-10)
	 * @param  String fileURI--文件名称(包含详细路径)
	 * @param  Vector vec_str--需要取的xml的节点路径集
     * @return Vector对象,取到的节点的值的集
     * @throws no Exception throw
	 */
	public static Vector<String> getElementValues(String fileURI,Vector<String> vec_str)
				throws java.io.FileNotFoundException {

		return getElementValues( new java.io.FileInputStream( fileURI ), vec_str) ;

	}

    /**
     * 从xml文件中取一个值
     * @author WUZEWEN
     * @version V1.0.0(2005-03-10)
     * @param  String fileURI--文件名称(包含详细路径)
     * @param  String parms--需要取的xml的节点路径
     * @return String对象,取到的节点的值
     * @throws no Exception throw
     */
	public static String getElementValue(String fileURI,String parms)
				throws java.io.FileNotFoundException {
		Vector<String> vec = new Vector<String>() ;
		vec.add(parms) ;
		return getElementValues(fileURI,vec).toString() ;

	}

}
