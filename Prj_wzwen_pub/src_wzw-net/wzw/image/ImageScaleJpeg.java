/**
 * Copyright (C) 2002-2005 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.image;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.log4j.Logger;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;


/**
 * <p>缩略图类，
 *	本java类能将jpg图片文件，进行等比或非等比的大小转换。
 * 	具体使用方法：先构建实例，再调用createImage方法。</p>
 * @author	WUZEWEN on 2005-09-15 
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 **/

public class ImageScaleJpeg{
	
	private int width;
	private int height;
	private int scaleWidth;
	double support = (double) 3.0;
	double PI = (double) 3.14159265358978;
	double[] contrib;
	double[] normContrib;
	double[] tmpContrib;
	int startContrib, stopContrib;
	int nDots;
	int nHalfDots;
	boolean proportion=true; //是否等比缩放标记(默认为等比缩放)
	
	/**
	 * 日志处理对象。
	 */
	private static Logger log = Logger.getLogger( ImageScaleJpeg.class );
	
	/**
	 * Start:
	 * Use lanczos filter to replace the original algorithm for image scaling. lanczos improves quality of the scaled image
	 * modify by :blade
	 * */
	public BufferedImage imageZoomOut(BufferedImage srcBufferImage,int w, int h) {


		//StopWatch sw = new StopWatch();		// 性能观测器
		//sw.start();
		
		width = srcBufferImage.getWidth();
		height = srcBufferImage.getHeight();
		scaleWidth = w;
		
		//sw.stop();
		//log.debug("------------------imageZoomOut 1--" + sw.getTime() );
		
		//sw.reset();
		//sw.start();
		if (determineResultSize(w, h) == 1) {
			return srcBufferImage;
		}
		
		//sw.stop();
		//log.debug("------------------imageZoomOut 2--" + sw.getTime() );

		//sw.reset();
		//sw.start();
		calContrib();
		//sw.stop();
		//System.out.println("------------------imageZoomOut 3-TTTTTTTT-" + sw.getTime() );
		
		//sw.reset();
		//sw.start();		
		BufferedImage pbOut = horizontalFiltering(srcBufferImage, w);		
		//sw.stop();
		//System.out.println("------------------imageZoomOut 4-TTTTTTTT-" + sw.getTime() );

		//sw.reset();
		//sw.start();
		BufferedImage pbFinalOut = verticalFiltering(pbOut, h);
		//sw.stop();
		//System.out.println("------------------imageZoomOut 5--" + sw.getTime() );
		
		return pbFinalOut;
	}
	
	/**
	 * 决定图像尺寸
	 * */
	private int determineResultSize(int w, int h) {
		double scaleH, scaleV;
		scaleH = (double) w / (double) width;
		scaleV = (double) h / (double) height;
		//需要判断一下scaleH，scaleV，不做放大操作
		if (scaleH >= 1.0 && scaleV >= 1.0) {
			return 1;
		}
		return 0;
		
	} // end of determineResultSize()
	
	private double lanczos(int i, int inWidth, int outWidth, double Support) {
		double x;
		
		x = (double) i * (double) outWidth / (double) inWidth;
		
		return Math.sin(x * PI) / (x * PI) * Math.sin(x * PI / Support)
		/ (x * PI / Support);
		
	} // end of lanczos()
	
	//
	//   Assumption: same horizontal and vertical scaling factor
	//
	private void calContrib() {
		nHalfDots = (int) ((double) width * support / (double) scaleWidth);
		nDots = nHalfDots * 2 + 1;
		try {
			contrib = new double[nDots];
			normContrib = new double[nDots];
			tmpContrib = new double[nDots];
		} catch (Exception e) {
			System.out.println("init contrib,normContrib,tmpContrib" + e);
		}
		
		int center = nHalfDots;
		contrib[center] = 1.0;
		
		double weight = 0.0;
		int i = 0;
		for (i = 1; i <= center; i++) {
			contrib[center + i] = lanczos(i, width, scaleWidth, support);
			weight += contrib[center + i];
		}
		
		for (i = center - 1; i >= 0; i--) {
			contrib[i] = contrib[center * 2 - i];
		}
		
		weight = weight * 2 + 1.0;
		
		for (i = 0; i <= center; i++) {
			normContrib[i] = contrib[i] / weight;
		}
		
		for (i = center + 1; i < nDots; i++) {
			normContrib[i] = normContrib[center * 2 - i];
		}
	} // end of calContrib()
	
	//处理边缘
	private void calTempContrib(int start, int stop) {
		double weight = 0;
		
		int i = 0;
		for (i = start; i <= stop; i++) {
			weight += contrib[i];
		}
		
		for (i = start; i <= stop; i++) {
			tmpContrib[i] = contrib[i] / weight;
		}
		
	} // end of calTempContrib()
	
	private int getRedValue(int rgbValue) {
		int temp = rgbValue & 0x00ff0000;
		return temp >> 16;
	}
	
	private int getGreenValue(int rgbValue) {
		int temp = rgbValue & 0x0000ff00;
		return temp >> 8;
	}
	
	private int getBlueValue(int rgbValue) {
		return rgbValue & 0x000000ff;
	}
	
	private int comRGB(int redValue, int greenValue, int blueValue) {
		
		return (redValue << 16) + (greenValue << 8) + blueValue;
	}
	
	//行水平滤波
	private int horizontalFilter(BufferedImage bufImg, int startX, int stopX,
			int start, int stop, int y, double[] pContrib) {
		double valueRed = 0.0;
		double valueGreen = 0.0;
		double valueBlue = 0.0;
		int valueRGB = 0;
		int i, j;
		
		for (i = startX, j = start; i <= stopX; i++, j++) {
			valueRGB = bufImg.getRGB(i, y);
			
			valueRed += getRedValue(valueRGB) * pContrib[j];
			valueGreen += getGreenValue(valueRGB) * pContrib[j];
			valueBlue += getBlueValue(valueRGB) * pContrib[j];
		}
		
		valueRGB = comRGB(clip((int) valueRed), clip((int) valueGreen),
				clip((int) valueBlue));
		return valueRGB;
		
	} // end of horizontalFilter()
	
	//图片水平滤波
	private BufferedImage horizontalFiltering(BufferedImage bufImage, int iOutW) {
		int dwInW = bufImage.getWidth();
		int dwInH = bufImage.getHeight();
		int value = 0;
		BufferedImage pbOut = new BufferedImage(iOutW, dwInH,
				BufferedImage.TYPE_INT_RGB);
		
		for (int x = 0; x < iOutW; x++) {
			
			int startX;
			int start;
			int X = (int) (((double) x) * ((double) dwInW) / ((double) iOutW) + 0.5);
			int y = 0;
			
			startX = X - nHalfDots;
			if (startX < 0) {
				startX = 0;
				start = nHalfDots - X;
			} else {
				start = 0;
			}
			
			int stop;
			int stopX = X + nHalfDots;
			if (stopX > (dwInW - 1)) {
				stopX = dwInW - 1;
				stop = nHalfDots + (dwInW - 1 - X);
			} else {
				stop = nHalfDots * 2;
			}
			
			if (start > 0 || stop < nDots - 1) {
				calTempContrib(start, stop);
				for (y = 0; y < dwInH; y++) {
					value = horizontalFilter(bufImage, startX, stopX, start,
							stop, y, tmpContrib);
					pbOut.setRGB(x, y, value);
				}
			} else {
				for (y = 0; y < dwInH; y++) {
					value = horizontalFilter(bufImage, startX, stopX, start,
							stop, y, normContrib);
					pbOut.setRGB(x, y, value);
				}
			}
		}
		
		return pbOut;
		
	} // end of horizontalFiltering()
	
	private int verticalFilter(BufferedImage pbInImage, int startY, int stopY,
			int start, int stop, int x, double[] pContrib) {
		double valueRed = 0.0;
		double valueGreen = 0.0;
		double valueBlue = 0.0;
		int valueRGB = 0;
		int i, j;
		
		for (i = startY, j = start; i <= stopY; i++, j++) {
			valueRGB = pbInImage.getRGB(x, i);
			
			valueRed += getRedValue(valueRGB) * pContrib[j];
			valueGreen += getGreenValue(valueRGB) * pContrib[j];
			valueBlue += getBlueValue(valueRGB) * pContrib[j];
			//                  System.out.println(valueRed+"->"+clip((int)valueRed)+"<-");
			//
			//                  System.out.println(valueGreen+"->"+clip((int)valueGreen)+"<-");
			//                  System.out.println(valueBlue+"->"+clip((int)valueBlue)+"<-"+"-->");
		}
		
		valueRGB = comRGB(clip((int) valueRed), clip((int) valueGreen),
				clip((int) valueBlue));
		//           System.out.println(valueRGB);
		return valueRGB;
		
	} // end of verticalFilter()
	
	private BufferedImage verticalFiltering(BufferedImage pbImage, int iOutH) {
		int iW = pbImage.getWidth();
		int iH = pbImage.getHeight();
		int value = 0;
		BufferedImage pbOut = new BufferedImage(iW, iOutH,
				BufferedImage.TYPE_INT_RGB);
		
		for (int y = 0; y < iOutH; y++) {
			
			int startY;
			int start;
			int Y = (int) (((double) y) * ((double) iH) / ((double) iOutH) + 0.5);
			
			startY = Y - nHalfDots;
			if (startY < 0) {
				startY = 0;
				start = nHalfDots - Y;
			} else {
				start = 0;
			}
			
			int stop;
			int stopY = Y + nHalfDots;
			if (stopY > (int) (iH - 1)) {
				stopY = iH - 1;
				stop = nHalfDots + (iH - 1 - Y);
			} else {
				stop = nHalfDots * 2;
			}
			
			if (start > 0 || stop < nDots - 1) {
				calTempContrib(start, stop);
				for (int x = 0; x < iW; x++) {
					value = verticalFilter(pbImage, startY, stopY, start, stop,
							x, tmpContrib);
					pbOut.setRGB(x, y, value);
				}
			} else {
				for (int x = 0; x < iW; x++) {
					value = verticalFilter(pbImage, startY, stopY, start, stop,
							x, normContrib);
					pbOut.setRGB(x, y, value);
				}
			}
			
		}
		
		return pbOut;
		
	} // end of verticalFiltering()
	
	int clip(int x) {
		if (x < 0)
			return 0;
		if (x > 255)
			return 255;
		return x;
	}
	
	/**
	 * <p>按照指定图片生成指定大小规格的新图片。在jdk1.4下面，支持gif图片；在jdk5下面，支持gif,bmp,png等图片格式。</p>
	 * @param inputDir 参考图片路径，eg: "D:/temp/", "/usr/temp/"
	 * @param outputDir 输出图片路径，eg: "D:/temp/", "/usr/temp/"
	 * @param inputFileName 参考图片名称
	 * @param outputFileName 输出图片名称
	 * @param w 新图片宽度像素值
	 * @param h 新图片高度像素值
	 * @param gp 是否需要等比缩放，默认true
	 * @return
	 * @throws Exception
	 */
	public boolean createImage(String inputDir,String outputDir,String inputFileName,String outputFileName,int new_w,int new_h,boolean gp) throws IOException{

		//inputDir = org.
		//System.out.println( inputDir + "\n" + outputDir + "\n" + inputFileName + "\n" + outputFileName);
		return createImage( inputDir+inputFileName, outputDir+outputFileName, new_w, new_h, gp);
	}

	/**
	 * 
	 * <p>按照指定图片生成指定大小规格的新图片。在jdk1.4下面，支持gif图片；在jdk5下面，支持gif,bmp,png等图片格式。</p>
	 * @param inputFile 参考图片路径加名称，eg: "D:/temp/abc.jpg", "/usr/temp/abc.gif"
	 * @param outputFile 输出图片路径加名称，eg: "D:/temp/efg.jpg", "/usr/temp/efg.gif"
	 * @param w 新图片宽度像素值
	 * @param h 新图片高度像素值
	 * @param gp 是否需要等比缩放，默认true
	 * @return 成功返回true，失败返回false。
	 * @throws IOException
	 */
	public boolean createImage(String inputFile,String outputFile,int new_w,int new_h,boolean gp) throws IOException{
		

		// for test
		//inputFile="/www/vangvsv/images/upload/shop_img/20080516/1210919370543.jpg";
		//outputFile="/www/a.jpg"; 
//		new_w = 100;
//		new_h = 100 ;
//		gp = true;
		
		log.debug("------------------------ 开始创建图片"+ outputFile);
		
		//StopWatch sw = new StopWatch();
		//sw.start();
		
		File _file = new File( inputFile ); 
		if( !_file.exists() ) {
			throw new IOException("需要缩略操作的源文件["+ _file.getAbsolutePath()+"]不存在！");
		}

		//sw.stop();
		//System.out.println( "----------- 1:"+ sw.getTime() );
		//sw.reset();
		//sw.start();
		
		//System.out.println(  _file==null);                      //读入文件
		Image src = javax.imageio.ImageIO.read(_file);		//构造Image对象
		// 本来是在jdk1.4下对bmp图片进一步处理，这里取消，建议先生成jpg的大图，在生成jpg的缩略图，而不是产生 bmp大图加上jpg的缩略图。
//		if(src==null) {
//			src=BitmapReader.load( inputDir, inputFileName );
//		}
		//sw.stop();
		//System.out.println( "----------- 2:"+ sw.getTime() );
		//sw.reset();
		//sw.start();
		if(src==null) {
			throw new IOException("创建图片对象失败，请检查图片文件是否符合规格！");
		}
		
		int width =src.getWidth(null);
		int height=src.getHeight(null);  
		this.proportion = gp ;

		//sw.stop();
		//System.out.println( "----------- 3:"+ sw.getTime() );
		//sw.reset();
		//sw.start();
		if (this.proportion==true) //判断是否是等比缩放.
		{
//			为等比缩放计算输出的图片宽度及高度
			double rate1=((double)width)/(double)new_w ;
			double rate2=((double)height)/(double)new_h ;
			double rate=rate1>rate2?rate1:rate2;
			new_w=(int)(((double)width)/rate);
			new_h=(int)(((double)height)/rate);
		}

		//sw.stop();
		//System.out.println( "----------- 4:"+ sw.getTime() );
		
		//sw.reset();
		//sw.start();
		BufferedImage tag = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
		//tag.getGraphics().drawImage(src,0,0,80,57,null);       //绘制缩小后的图
		//sw.stop();
		//System.out.println( "----------- 5:"+ sw.getTime() );
		
		//sw.reset();
		//sw.start();	
		tag.getGraphics().drawImage(src,0,0,width,height,null);
		//sw.stop();
		//System.out.println( "----------- 6:"+ sw.getTime() );

		//sw.reset();
		//sw.start();
		//// ImageScale is = new ImageScale(); //Zeven
		tag= this.imageZoomOut(tag, new_w, new_h);
//		sw.stop();
//		System.out.println( "----------- 7:"+ sw.getTime() );

//		sw.reset();
//		sw.start();
		System.out.println( outputFile==null );
		FileOutputStream out=new FileOutputStream( outputFile );	//输出到文件流
		JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);       
//		sw.stop();
//		System.out.println( "----------- 8:"+ sw.getTime() );

//		sw.reset();
//		sw.start();
		encoder.encode(tag);                                               //近JPEG编码
		//System.out.print(width+"*"+height);                              
		out.close();
//		sw.stop();
//		System.out.println( "----------- 9:"+ sw.getTime() );

		log.debug("------------------------ 创建图片完成");
		_file = null;
		src = null;
		tag = null;
		out = null;
		encoder = null;
		log.debug("------------------------ 清理垃圾完成");
		
		return true;
		
	}
	
	/**
	 * End:
	 * Use lanczos filter to replace the original algorithm for image scaling. lanczos improves quality of the scaled image
	 * modify by :blade
	 * @throws InterruptedException 
	 * */
	 
	public static void main(String [] args) throws IOException, InterruptedException
	{

		if( true ){		// 使用客户端测试
//			String arg0 = args[0];
//			String arg1 = args[1];
			String arg0 = "E:/My Documents_200608/Tmp_work/e182b2a2b79fa5f7336ddbc41c9c5484.jpg";
			String arg1 = "E:/My Documents_200608/Tmp_work/e182b2a2b79fa5f7336ddbc41c9c5484_200_good.jpg";
			System.out.println("file="+arg0 +"\npath="+arg1);
			ImageScaleJpeg is = new ImageScaleJpeg();
			System.out.println("-- begin createImage.");
			is.createImage(arg0,arg1, 200,200, true); 
			System.out.println("--createImage ok");
			
			//"D:\bea\jdk142_08\bin\java" wzw.image.ImageScale "E:/My Documents_200608/temp/tempBmp.jpg" D:/tempBmp_100.jpg
			//"D:\Program Files\Java\jdk1.5.0_12\bin\java" wzw.image.ImageScale "E:/My Documents_200608/temp/tempBmp.jpg" D:/tempBmp_100.jpg
			
	//		is.createImage("E:/My Documents_200608/temp/",
	//				"E:/My Documents_200608/temp/",
	//				"Zewin.bmp",
	//				"Zewin_abc.bmp", 
	//				80,58,
	//				true);
		}
		
		System.out.println("--ok");
	}
}

