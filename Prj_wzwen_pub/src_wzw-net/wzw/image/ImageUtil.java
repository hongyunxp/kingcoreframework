/**
 * Copyright (C) 2002-2009 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 *
 * This software is the confidential and proprietary information of
 * WuZeven, Personal. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with WuZeven.
 */

package wzw.image;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.util.HashMap;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import com.gif4j.GifDecoder;
import com.gif4j.GifEncoder;
import com.gif4j.GifImage;
import com.gif4j.GifTransformer;
import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * <p>java类文件的说明...</p>
 * @author Zeven on Oct 28, 2009
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 */

public class ImageUtil {

	private final static Logger log = Logger.getLogger(ImageUtil.class);
	
	/**
	 * 给图片加水印，不改变大小	 
	 * @param imageUrl String(原始文件)
	 * @param newImageUrl String(水印后输文件)
	 * @param markContent 水印内容
	 * @param markContentColor Color 水印颜色
	 * @param qualNum 大小
	 * @throws Exception 
	 * 
	 */
	public void waterMark(String imageUrl,String newImageUrl,String markContent, 
			Color markContentColor, String fontType,int fontSize, int position) throws Exception
	{  
		FileOutputStream fos =null;
		try 
		{
			java.io.File file = new java.io.File(imageUrl);			
			Image imageOriginal = javax.imageio.ImageIO.read(file); //构造Image对象 
	        ImageIcon waterIcon = new ImageIcon(markContent); 
	        
			int width = imageOriginal.getWidth(null);
			int height = imageOriginal.getHeight(null);
	        
			BufferedImage bufImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
			
			//水印文件在源文件的右下角  
			Graphics2D g = bufImage.createGraphics();
			g.setColor(markContentColor);
			 if(newImageUrl.length()>0){   
		            Image waterImg = waterIcon.getImage();   
		            width = width-waterIcon.getIconWidth();   
		            height = height-waterIcon.getIconHeight();   
		            g.drawImage(imageOriginal, 0, 0, null);   
		            //水印图片的位置 与水印文字一起定位   
		            switch(position){   
		                case 0 ://居中   
		                    g.drawImage(waterImg, width / 2, height / 2, null);                    
		                    break;   
		                case 1 ://左上   
		                    g.drawImage(waterImg, 0, 0 , null);   
		                    break;   
		                case 2 ://右上   
		                    g.drawImage(waterImg, width , 0 , null);   
		                    break;   
		                case 3 ://左下   
		                    g.drawImage(waterImg, 0, height , null);   
		                    break;   
		                case 4 ://右下   
		                    g.drawImage(waterImg, width , height , null);   
		                    break;   
		                default ://居中   
		                    g.drawImage(waterImg, width / 2, height / 2, null);            
		                    break;   
		            }   
		        }   
		           
		        //添加文字   
		        if(markContent.length()>0){   
		            AttributedString ats = new AttributedString(markContent);   
		            Font f = new Font(fontType,Font.BOLD, fontSize);       
		            ats.addAttribute(TextAttribute.FONT, f, 0,markContent.length() );   
		            AttributedCharacterIterator iter = ats.getIterator();   
		            switch(position){   
		            case 0 ://居中   
		                g.drawString(iter,width / 2, height / 2);   
		                break;   
		            case 1 ://左上   
		                g.drawString(iter,0, 0);   
		                break;   
		            case 2 ://右上   
		                g.drawString(iter,width , 0);   
		                break;   
		            case 3 ://左下   
		                g.drawString(iter,0, height );   
		                break;   
		            case 4 ://右下   
		            	g.drawString(iter, Math.round(width / 1.8), Math.round(height));
		                //g.drawString(iter,width , height);   
		                break;   
		            default ://居中   
		                g.drawString(iter,width / 2, height / 2);                      
		                break;   
		        }   
		        }   
		           

			g.setBackground(Color.white);
			g.dispose();  

			fos = new FileOutputStream(newImageUrl);
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(fos);
            JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bufImage);
            //param.setQuality(1f, true);
            encoder.encode(bufImage, param);
 
			fos.flush();
		} catch (Exception e) {
			//e.printStackTrace();
			log.error("图片加水印失败！",e);
			throw e;
			
		}finally{
		   if(fos!=null){
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		   }
		}
	}


	/**
	 *  对图片缩略。
	 * @param srcImg
	 * @param destImg
	 * @param width
	 * @param height
	 * @param smooth
	 * @throws IOException 
	 */
	public static void getGifImage(String srcImg, String destImg, int width,   
            int height, boolean smooth) throws IOException {   
  
        try {
        	File fSrc = new File(srcImg);
        	File fDest = new File(destImg);
        	
        	Image src = javax.imageio.ImageIO.read(fSrc);
			int old_w = src.getWidth(null); //得到源图宽
			int old_h = src.getHeight(null);//得到源图长
			double ratio = 1.0; //缩小比率
			if (old_w > 0 && old_h > 0) {
				double tw = (double)old_w / (double)width;
				double th = (double)old_h / (double)height;
				ratio = Math.max(tw, th);
			}
			width = (int)(old_w / ratio);
        	height = (int)(old_h / ratio);
			
            GifImage gifImage = GifDecoder.decode(fSrc);   
            GifImage resizedGifImage2 = GifTransformer.resize(gifImage, width, height, smooth);   
            GifEncoder.encode(resizedGifImage2, fDest);   
        } catch (IOException e) {   
            //e.printStackTrace(); 
			log.error("图片缩略失败！",e);  
            throw e;
        }   
  
    }  
	
	public static void main(String[] args) throws Exception{
//		try{
//			String imageUrl = "d:\\U3.jpg";
//			String newImageUrl = "d:\\c1.jpg";
//			ImageProducer filterSource = null;
//			double value = 1.0;
//			value += 5.0;
//			ImageProducer source = Jimi.getImageProducer(imageUrl);			
//			filterSource = source;
//			Shrink filter = new Shrink(source, (int)value);
//			 
//			FilteredImageSource fis = new FilteredImageSource(filterSource, filter);
//			filterSource = fis;
//			Jimi.putImage(filterSource, newImageUrl);			
//		}catch(Exception e){
//			e.printStackTrace();
//		}
		
//		 转换并写文件,若为GIF则需要特殊转换   
         getGifImage("f:\\1.gif", "f:\\a.gif", 130, 100, true);   
	}

}
