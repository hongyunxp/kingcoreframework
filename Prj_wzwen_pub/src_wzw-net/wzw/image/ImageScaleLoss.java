/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.image;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

import com.gif4j.GifEncoder;

/**
 * <p>
 * 	缩略图类， 本java类能将jpg图片文件，进行等比或非等比的大小转换。 
 *  具体使用方法：先构建实例，再调用createImage方法。
 * 		能处理有透明层背景的gif图片，但是所有图片质量不高，有些模糊。
 * 		2009-07-22 wzw : 增加图片缩放时是否需要补白的功能
 * 
 * </p>
 * 
 * @author WUZEWEN on 2005-09-15
 * @version 1.0
 * @see Object#equals(java.lang.Object)
 * @see Object#hashCode()
 * @see HashMap
 * @since JDK5
 */

public class ImageScaleLoss { 
 


	/**
	 * 日志处理对象。
	 */
	private final static Logger log = Logger.getLogger(ImageScaleLoss.class);
 
	/**
	 * <p>
	 * 按照指定图片生成指定大小规格的新图片。在jdk1.4下面，支持gif图片；在jdk5下面，支持gif,bmp,png等图片格式。
	 * </p>
	 * 
	 * @param inputDir
	 *            参考图片路径，eg: "D:/temp/", "/usr/temp/"
	 * @param outputDir
	 *            输出图片路径，eg: "D:/temp/", "/usr/temp/"
	 * @param inputFileName
	 *            参考图片名称
	 * @param outputFileName
	 *            输出图片名称
	 * @param w
	 *            新图片宽度像素值
	 * @param h
	 *            新图片高度像素值
	 * @param gp
	 *            是否需要等比缩放，默认true
	 * @return
	 * @throws Exception
	 */
	public boolean createImage(String inputDir, String outputDir,
			String inputFileName, String outputFileName, int new_w, int new_h,
			boolean gp) throws IOException {

		// inputDir = org.
		// System.out.println( inputDir + "\n" + outputDir + "\n" +
		// inputFileName + "\n" + outputFileName);
		return createImage(inputDir + (inputDir.endsWith("/")?"":File.separator) + inputFileName,
				           outputDir+ (outputDir.endsWith("/")?"":File.separator)  + outputFileName, 
				           new_w, new_h, gp);
	}

	public boolean createImage(String inputFile, String outputFile, int new_w,
			int new_h, boolean gp) throws IOException {
		return createImage(inputFile, outputFile, new_w,
				new_h, gp, true);
	}
	
	/**
	 * 
	 * <p>
	 * 按照指定图片生成指定大小规格的新图片。在jdk1.4下面，支持gif图片；在jdk5下面，支持gif,bmp,png等图片格式。
	 * </p>
	 * 
	 * @param inputFile
	 *            参考图片路径加名称，eg: "D:/temp/abc.jpg", "/usr/temp/abc.gif"
	 * @param outputFile
	 *            输出图片路径加名称，eg: "D:/temp/efg.jpg", "/usr/temp/efg.gif"
	 * @param w
	 *            新图片宽度像素值
	 * @param h
	 *            新图片高度像素值
	 * @param gp
	 *            是否需要等比缩放，默认true
	 * @param needFilled
	 *            是否需要补白
	 * @return 成功返回true，失败返回false。 
	 * @throws IOException
	 */
	public boolean createImage(String inputFile, String outputFile, int new_w,
			int new_h, boolean gp, boolean needFilled) throws IOException {

		// for test
		// inputFile="/www/vangvsv/images/upload/shop_img/20080516/1210919370543.jpg";
		// outputFile="/www/a.jpg";
		// new_w = 100;
		// new_h = 100 ;
		// gp = true;

		log.debug("------------------------ 开始创建图片" + outputFile);

		// StopWatch sw = new StopWatch();
		// sw.start();

		int req_w = new_w;
		int req_h = new_h; // 要求生成的高度、宽度
		File _file = new File(inputFile);
		if (!_file.exists()) {
			throw new IOException("需要缩略操作的源文件[" + _file.getAbsolutePath()
					+ "]不存在！");
		}

		// sw.stop();
		// System.out.println( "----------- 1:"+ sw.getTime() );
		// sw.reset();
		// sw.start();

		// System.out.println( _file==null); //读入文件
		BufferedImage src = ImageIO.read(_file); // 构造Image对象
		// 本来是在jdk1.4下对bmp图片进一步处理，这里取消，建议先生成jpg的大图，在生成jpg的缩略图，而不是产生
		// bmp大图加上jpg的缩略图。
		// if(src==null) {
		// src=BitmapReader.load( inputDir, inputFileName );
		// }
		// sw.stop();
		// System.out.println( "----------- 2:"+ sw.getTime() );
		// sw.reset();
		// sw.start();
		if (src == null) {
			throw new IOException("创建图片对象失败，请检查图片文件是否符合规格！");
		}

		int width = src.getWidth(null);
		int height = src.getHeight(null);

		boolean proportion = gp;
		//首先判断原图高宽是否比缩放后的高宽小,如果小于
		//就直接输出原图
		if (new_w >= width && new_h >= height) {
			proportion = false;
			new_w = width;
			new_h = height;
		}
		// sw.stop();
		// System.out.println( "----------- 3:"+ sw.getTime() );
		// sw.reset();
		// sw.start();
		if (proportion == true) // 判断是否是等比缩放.
		{
			// 为等比缩放计算输出的图片宽度及高度
			double rate1 = ((double) width) / (double) new_w;
			double rate2 = ((double) height) / (double) new_h;
			double rate = rate1 > rate2 ? rate1 : rate2;
			new_w = (int) (((double) width) / rate);
			new_h = (int) (((double) height) / rate);
		}

		// sw.stop();
		// System.out.println( "----------- 4:"+ sw.getTime() );

		// sw.reset();
		// sw.start();

		FileOutputStream out = new FileOutputStream(outputFile); // 输出到文件流
		
		BufferedImage tag = null;
		if(needFilled){  // 是否需要补空白
			//BufferedImage tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_4BYTE_ABGR);
			tag = new BufferedImage(req_w, req_h, BufferedImage.TYPE_4BYTE_ABGR);
			// BufferedImage tag = new
			// BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
			// tag.getGraphics().drawImage(src,0,0,80,57,null); //绘制缩小后的图
			// sw.stop();
			// System.out.println( "----------- 5:"+ sw.getTime() );

			// sw.reset();
			// sw.start();

			//tag.getGraphics().drawImage(
			//		src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null); // 绘制缩小后的图
			tag.getGraphics().drawImage(
					src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 
							(req_w-new_w)/2, (req_h-new_h)/2, null); // 绘制缩小后的图
			
			tag.getGraphics().setColor(Color.white);
			if(req_w > new_w){
				tag.getGraphics().fillRect(0, 0,               (req_w-new_w)/2, req_h );
				tag.getGraphics().fillRect((req_w+new_w)/2, 0, (req_w-new_w)/2, req_h );
			}
			if(req_h > new_h){
				tag.getGraphics().fillRect(0, 0,                req_w, (req_h-new_h)/2 );
				tag.getGraphics().fillRect(0, (req_h+new_h)/2,  req_w, (req_h-new_h)/2 );
				
			}
			
		}else{	// 不需要补白
			tag = new BufferedImage(new_w, new_h, BufferedImage.TYPE_4BYTE_ABGR);

			tag.getGraphics().drawImage(
						src.getScaledInstance(new_w, new_h, Image.SCALE_SMOOTH), 0, 0, null); // 绘制缩小后的图
			
		}
		
		// sw.reset();
		// sw.start();
		// // ImageScale is = new ImageScale(); //Zeven
		// tag= this.imageZoomOut(tag, new_w, new_h);
		// sw.stop();
		// System.out.println( "----------- 7:"+ sw.getTime() );

		// sw.reset();
		// sw.start();
		// System.out.println(outputFile == null);

		// JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
		/// encoder.encode( bi, jep );
		GifEncoder.encode(tag, out);
		ImageIO.write(tag, "gif", out);
		// sw.stop();
		// System.out.println( "----------- 8:"+ sw.getTime() );

		// sw.reset();
		// sw.start();
		// encoder.encode(tag); //近JPEG编码
		// System.out.print(width+"*"+height);
		out.close();
		// sw.stop();
		// System.out.println( "----------- 9:"+ sw.getTime() );

		log.debug("------------------------ 创建图片完成");
		_file = null;
		src = null;
		tag = null;
		out = null;
		// encoder = null;
		log.debug("------------------------ 清理垃圾完成");

		return true;

	}

	/**
	 * End: Use lanczos filter to replace the original algorithm for image
	 * scaling. lanczos improves quality of the scaled image modify by :blade
	 * 
	 * @throws InterruptedException
	 */

	public static void main(String[] args) throws IOException,
			InterruptedException {
		if(true){
			ImageScaleLoss is = new ImageScaleLoss();
			is.createImage("E:/Tmp_E/20090816104908633ZDTPQ1.jpg", "E:/Tmp_E/20090816104908633ZDTPQ1_100.jpg", 100, 100, true);
			is.createImage("E:/Tmp_E/20090816104908633ZDTPQ1.jpg", "E:/Tmp_E/20090816104908633ZDTPQ1_300.jpg", 300, 300, true);
			return;
		}
		
		if (false) { // 使用多线程测试

			ImageScaleThread t1 = new ImageScaleThread(
					  "E:/My Documents_200608/Tmp_work/imageScale/42699ecd616adcee2c9cb5e5cb4a440d--小图缩放测试2.gif",
	  		          "E:/My Documents_200608/Tmp_work/imageScale/42699ecd616adcee2c9cb5e5cb4a440d--小图缩放测试2_200_bad2.jpg",
					200, 200, true);

			if (true) {
				return;
			}

			ImageScaleThread t2 = new ImageScaleThread(
					"E:/My Documents_200608/tmp/42699ecd616adcee2c9cb5e5cb4a440d--小图缩放测试2.jpg",
					"E:/My Documents_200608/tmp/42699ecd616adcee2c9cb5e5cb4a440d--小图缩放测试_200.jpg",
					200, 200, true);
			// System.out.println("-------------------- end ...0 ");
			t1.thread.join();
			// System.out.println("-------------------- end ...1 ");
			// System.out.println("-------------------- end ...2 ");
			// System.out.println("-------------------- end ...3 ");
			// System.out.println("-------------------- end ...4 ");
			t2.thread.join();
			// System.out.println("-------------------- end ...5 ");
			// System.out.println("-------------------- end ...6 ");
			// System.out.println("-------------------- end ...7 ");
			return;

		}

		if (false) { // 使用客户端测试
			long bt = System.currentTimeMillis();
			ImageScaleLoss is = new ImageScaleLoss();
			
			for (int i = 0; i < 10; i++) {
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
							"商城商家快速注册界面2.jpg", "商城商家快速注册界面2_250.jpg",
						250, 250, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"商城商家快速注册界面2.jpg", "商城商家快速注册界面2_200.jpg",
					200, 200, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"商城商家快速注册界面2.jpg", "商城商家快速注册界面2_150.jpg",
					150, 150, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"商城商家快速注册界面2.jpg", "商城商家快速注册界面2_100.jpg",
					100, 100, true);
				
			}
			long end = System.currentTimeMillis();
			System.out.println((end-bt)/1000.0+"秒");

		}
		if (true) { // 使用客户端测试			long bt = System.currentTimeMillis();
			ImageScaleLoss is = new ImageScaleLoss();
			
			for (int i = 0; i < 1; i++) {
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
							"商城商家快速注册界面2.jpg", "商城商家快速注册界面2_250.jpg",
						250, 250, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"商城商家快速注册界面2_250.jpg", "商城商家快速注册界面2_200.jpg",
					200, 200, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"商城商家快速注册界面2_200.jpg", "商城商家快速注册界面2_150.jpg",
					150, 150, true);
				is.createImage("E:/My Documents_200608/Tmp_Work_Doc/", "E:/My Documents_200608/Tmp_Work_Doc/", 
						"商城商家快速注册界面2_150.jpg", "商城商家快速注册界面2_100.jpg",
					100, 100, true);
				
				
			}
			long end = System.currentTimeMillis();
			System.out.println((end-bt)/1000.0+"秒");
			//return ;
		}

		// System.out.println("--ok");
	}

}
