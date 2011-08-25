/**
 * Copyright (C) 2005 WUZEWEN. All rights reserved.
 * WZW PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.apache.log4j.Logger;

/**
 * <p>use simple mail translate protocal.
 * 		在java版经常看到有人问如何用javamail发送邮件？如何接收邮件？如何访问多个文件夹等。问题零散，而历史的回复早已经淹没在问题的海洋之中。
 * 本人之前所做过一个java项目，其中包含有WebMail功能，当初为用java实现而对javamail摸索了一段时间，总算有点收获。看到论坛中的经常有此方面的问题，因此把我的一些经验帖出来，希望对大家有些帮助。
 * 此篇仅介绍用javamail实现发送邮件功能，其中涉及smtp认证，邮件附件发送，及HTML内容邮件等。
 * 其它有关多邮箱的实现，接收POP3邮件及IMAP等内容，将在后续文章中介绍。
 * 如下程序需要：javamail，JAF包，j2ee.jar包含了上述两个包，建议大家安装J2SDKEE或直接拷贝j2ee.jar，将其添加到jbuilder的library中，或系统ClassPath中</p>
 * @author	WUZEWEN on 2005-05-25
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class MailSendBean {

	/**
	 * 日志处理对象。
	 */
	protected static Logger log = Logger.getLogger( wzw.mail.MailSendBean.class);

	private MimeMessage mimeMsg; //MIME邮件对象
	private Session session; //邮件会话对象
	private Properties props; //系统属性
	//private boolean needAuth = false; //smtp是否需要认证	
	private String username = ""; //smtp认证用户名和密码
	private String password = "";
	
	private Multipart mp; //Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象
	
	
	/**
	 *	Constructor function
	 */
	public MailSendBean() {
		this("smtp.163.com");//getConfig.mailHost);//如果没有指定邮件服务器,就从getConfig类中获取
	}
	
	/**
     * <p>Constructor with one param</p>
     * @author WUZEWEN
     * @param smtp 邮件服务器的simple mail transplate protocal
     * @return none
     * @exception no exception
     */
	public MailSendBean(String smtp){
		setSmtpHost(smtp);
		createMimeMessage();
	}
	
	
	/**
     * <p>a setter for SmtpHost</p>
     * @author WUZEWEN
     * @param hostName name of mail service host
     * @return void
     * @exception no exception
     */
	public void setSmtpHost(String hostName) {
		System.out.println("设置系统属性：mail.smtp.host = "+hostName);
		
		if(props == null)props = System.getProperties(); //获得系统属性对象		
		props.put("mail.smtp.host",hostName); //设置SMTP主机
		
		// props.put("mail.smtp.starttls.enable","true");
	}

	
	/**
     * <p>1.获得邮件会话对象; 2.创建MIME邮件对象</p>
     * @author WUZEWEN
     * @param String a
     * @param String b
     * @return String c
     * @exception no exception
     */
	public boolean createMimeMessage(){
		try{
			System.out.println("准备获取邮件会话对象！");
			session = Session.getDefaultInstance(props,null); //获得邮件会话对象
		}
		catch(Exception e){
			System.err.println("获取邮件会话对象时发生错误！"+e);
			return false;
		}
	
		System.out.println("准备创建MIME邮件对象！");
		try{
			mimeMsg = new MimeMessage(session); //创建MIME邮件对象
			mp = new MimeMultipart();
			return true;
		}
		catch(Exception e){
			System.err.println("创建MIME邮件对象失败！"+e);
			return false;
		}
	}


	/**
     * <p>set the smtp server is need auth.</p>
     * @author WUZEWEN
     * @param boolean need
     * @return String void
     * @exception no exception
     */
	public void setNeedAuth(boolean need) {
		System.out.println("设置smtp身份认证：mail.smtp.auth = "+need);
		if(props == null)props = System.getProperties();
		
		if(need){
			props.put("mail.smtp.auth","true");
		}else{
			props.put("mail.smtp.auth","false");
		}
	}


	/**
     * <p>设置用户和口令</p>
     * @author WUZEWEN
     * @param name 发送人登录邮箱用户
     * @param pass 发送人登录邮箱口令
     * @return void
     * @exception no exception
     */
	public void setUserPass(String name,String pass) {
		username = name;
		password = pass;
	}


	/**
     * <p>设置MIME邮件对象的主题</p>
     * @author WUZEWEN
     * @param String mailSubject
     * @return boolean
     * @exception no exception
     */
	public boolean setSubject(String mailSubject) {
		System.out.println("设置邮件主题！");
		try{
			mimeMsg.setSubject(mailSubject);
			return true;
		}
		catch(Exception e) {
			System.err.println("设置邮件主题发生错误！");
			return false;		
		}
	}


	/**
     * <p>设置MIME邮件对象的正文</p>
     * @author WUZEWEN
     * @param String mailBody
     * @return String c
     * @exception no exception
     */
	public boolean setBody(String mailBody) {
		try{
			 
			BodyPart bp = new MimeBodyPart();
			bp.setContent("<meta http-equiv=Content-Type content=text/html; charset=gb2312>"+mailBody,"text/html;charset=GB2312");
			mp.addBodyPart(bp);
		
			return true;
		}
			catch(Exception e){
			System.err.println("设置邮件正文时发生错误！"+e);
			return false;
		}
	}

	
	/**
     * <p>添加邮件的附件</p>
     * @author WUZEWEN
     * @param String filename
     * @return boolean c
     * @exception no exception
     */
	public boolean addFileAffix(String filename) {
	
		System.out.println("增加邮件附件："+filename);
		
		try{
			BodyPart bp = new MimeBodyPart();
			FileDataSource fileds = new FileDataSource(filename);
			bp.setDataHandler(new DataHandler(fileds));
			bp.setFileName(fileds.getName());
			
			mp.addBodyPart(bp);
			return true;
		}catch(Exception e){
			System.err.println("增加邮件附件："+filename+"发生错误！"+e);
			return false;
		}
	}


	/**
     * <p>设置发信人！</p>
     * @author WUZEWEN
     * @param String from
     * @return String c
     * @exception no exception
     */
	public boolean setFrom(String from) {
		System.out.println("设置发信人！");
		try{
			mimeMsg.setFrom(new InternetAddress(from)); //设置发信人
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
	}


	/**
     * <p>设置收信人信息</p>
     * @author WUZEWEN
     * @param to 收信人邮箱，多个可以用,号分隔
     * @return boolean 设置成功返回true，设置失败返回false。
     * @exception no exception
     */
	public boolean setTo(String to){
		if(to == null)return false;
		
		try{
			mimeMsg.setRecipients(Message.RecipientType.TO,InternetAddress.parse(to));
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
		
	}

	/**
     * <p>设置抄送人</p>
     * @author WUZEWEN
     * @param copyto 抄送人，多个可以使用,号分隔
     * @return boolean 设置成功返回true，设置失败返回false。
     * @exception no exception
     */
	public boolean setCopyTo(String copyto){
		if(copyto == null)return false;
		try{
			mimeMsg.setRecipients(Message.RecipientType.CC,(Address[])InternetAddress.parse(copyto));
			return true;
		}
		catch(Exception e)
		{
			return false; 
		}
	}

	
	/**
     * <p>邮件的发送</p>
     * @author WUZEWEN
     * @return boolean 发送成功返回true，发送失败返回false。
	 * @throws Exception 
     * @exception exception
     */
	public boolean sendout() throws Exception {
		try {
			mimeMsg.setContent(mp);
			mimeMsg.saveChanges();
			log.info("正在发送邮件....");

			Session mailSession = Session.getInstance(props, null);
			Transport transport = mailSession.getTransport("smtp");
			transport.connect((String) props.get("mail.smtp.host"), username,
					password);
			transport.sendMessage(mimeMsg, mimeMsg
					.getRecipients(Message.RecipientType.TO));
			// transport.send(mimeMsg);
			// transport.

			log.info("发送邮件成功！");
			transport.close();

			return true;
		} catch (Exception e) {
			log.error("邮件发送失败！" + e.getMessage(), e );
			//log.info("debug", e );
			/// e.pri ntStackTrace() ;
			throw e;
		}
	}

	
	/**  
	* <p>设置代理服务器</p>
	* @author wuzewen on 2005-07-29
	* @param proxy 使用的邮件发送代理的ip
	* @param proxyPort 使用的邮件发送代理的端口
	*/  
	public void setProxyServer(String type, String proxy, String proxyPort) {  
		//设置代理服务器  
		//    System.getProperties().put("proxySet", "true");  
		//    System.getProperties().put("proxyHost", proxy);  
		//    System.getProperties().put("proxyPort", proxyPort);  
		if(type.equals("httpProxy")){
			//http proxy
			System.getProperties().put("httpProxySet",new Boolean(true) );
			System.getProperties().put("httpProxyHost",proxy);
			System.getProperties().put("httpProxyPort",proxyPort);	
		}else if(type.equals("socktProxy")){
			//sockt proxy   
			System.out.println ("proxy="+proxy+":"+proxyPort); 
			System.getProperties().put("socksProxySet",new Boolean(true) );
			System.getProperties().put("socksProxyHost",proxy);
			System.getProperties().put("socksProxyPort",proxyPort);
		}else {
			//sockt proxy   
			System.out.println ("proxy="+proxy+":"+proxyPort); 
			System.getProperties().put("socksProxySet",new Boolean(true) );
			System.getProperties().put("socksProxyHost",proxy);
			System.getProperties().put("socksProxyPort",proxyPort);
		}
	
	}  

	/**
	 * <p>发送邮件的Main函数。</p>
	 * @author WUZEWEN on 2005-05-25
	 * @param args 调用参数
	 * @return void
	 * @throws Exception 
	 * @throws no exception
	 */
	public static void main(String[] args) throws Exception {
	
//		String mailbody = "<meta http-equiv=Content-Type content=text/html; charset=gb2312>"+
//		"<div align=center><a href=http://www.csdn.net> csdn </a></div>";
		
		//获取配置信息
		Properties props=new Properties();
		try{
			props.load(new java.io.FileInputStream("properties_2005_javamail.properties")) ;
		}catch(FileNotFoundException ex){
			System.out.println ("没有找到属性文件！");
			System.exit(0);
		}catch(IOException ex){
			System.out.println ("没有找到属性文件！");
			return;
		}
		//邮件信息配置
		String prefix=props.getProperty("jmail.rootCategory");	//主类别
		if(prefix==null||prefix.trim().equals("")){
			prefix="A1";
		}
		prefix="jmail.conf."+prefix+".";
		System.out.println ("category:"+prefix);
		String subject = props.getProperty(prefix+"subject");	//主题
		String body    = props.getProperty(prefix+"body");		//文本内容
		String from    = props.getProperty(prefix+"from");		//发送邮箱
		String user    = props.getProperty(prefix+"user");		//发送邮箱用户
		String pass    = props.getProperty(prefix+"pass");		//发送邮箱口令
		String to      = props.getProperty(prefix+"to");		//接收邮箱集合，用,号分隔
		String copyTo  = props.getProperty(prefix+"copyTo");	//抄送邮箱集合，用,号分隔
		String fileAffix=props.getProperty(prefix+"fileAffix");//附件，多个使用,号分隔
		
		if(subject==null) subject="";
		if(body==null) body="";
		if(from==null) from="";
		if(user==null||user.trim().equals("")){
			user=from.substring(0,from.indexOf("@"));
		}
		if(pass==null||pass.trim().equals("")){
			pass="tom8798155";
		}
		System.out.println ("pass="+pass);
		if(to==null) to="";
		if(copyTo==null) copyTo="";
		if(fileAffix==null) fileAffix="";
		java.util.StringTokenizer st = new StringTokenizer(fileAffix,",");
		
		//网络配置信息
		String smtp     = props.getProperty(prefix+"smtp");		//邮件服务器的smtp
		String proxyType= props.getProperty(prefix+"proxyType");		//局域网的proxyType
		String proxyIP  = props.getProperty(prefix+"proxyIP");		//局域网的proxyIP
		String proxyPort= props.getProperty(prefix+"proxyPort");		//局域网的proxyPort
	    
		//设置代理服务器 
		MailSendBean themail = new MailSendBean(smtp);
		if(proxyType!=null&&!proxyType.trim().equals("")){
			themail.setProxyServer(proxyType,proxyIP,proxyPort) ;			
		}
		//themail.props = System.getProperties() ;
		//themail.setProxyServer("192.168.0.10","1080") ;
		//themail.setProxyServer("192.168.0.10","8000") ;
		themail.setNeedAuth(true);
		//themail.		
		
		if(themail.setSubject( subject ) == false) return;
		if(themail.setBody( body ) == false) return;
		if(themail.setFrom( from ) == false) return;
		if(themail.setTo( to ) == false) return;
		if(!copyTo.equals("")){
			if(themail.setCopyTo(copyTo) == false) return;
		}
		while(st.hasMoreElements()){
			String tmep = st.nextElement().toString() ;
			System.out.println (tmep);
			if(themail.addFileAffix(tmep) == false){
				System.out.println("添加附件失败！");
				return;
			}
		}
		//if(themail.addFileAffix("c:/myattachment.txt") == false) return;
		themail.setUserPass(user,pass);			
		if(themail.sendout() == false){
			return; 
		}
		
	}
	
}
