/**
 * Copyright (C) 2002-2008 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.image;

import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * <p>新的整张图片缩放线程。</p>
 * @author Zeven on 2008-5-15
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ImageScaleThread implements Runnable {

	/**
	 * 日志处理对象。
	 */
	protected static Logger log = Logger.getLogger( wzw.image.ImageScaleThread.class);

	public Thread thread ; 
	private String inputFile;
	private String outputFile;
	private int new_w;
	private int new_h;
	private boolean gp;
	
	public ImageScaleThread(String inputFile,String outputFile,int new_w,int new_h,boolean gp){
	    //create a new, second thread
	    this.thread = new Thread( this, "ImageScaleThread") ;
	    this.inputFile = inputFile;
	    this.outputFile = outputFile;
	    this.new_w = new_w;
	    this.new_h = new_h;
	    this.gp = gp;
	    
	    this.thread.start();
	    //System.out.println("ImageScaleThread start ...");
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run() 
	 */
	public void run() {
//		System.out.println("------------0");
		ImageScaleLoss imageScale = new ImageScaleLoss();
		try {
//			System.out.println("------------1");
//			System.out.println( this.outputFile==null );
			imageScale.createImage( this.inputFile, this.outputFile,
							this.new_w, this.new_h,  this.gp );
		} catch (IOException e) {
			log.debug("debug", e);
			/// e.pri ntStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
