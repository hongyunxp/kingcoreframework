/**
 * Copyright (C) 2002-2007 WUZEWEN. All rights reserved.
 * WUZEWEN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package wzw.image;

/**
 * <p>MicroSoft 位图文件读取类。 
 * 		BitmapHandler/BitmapReader/p>
 * @version 1.0
 */


import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.awt.image.MemoryImageSource;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.ImageIcon;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

public class BitmapReader {
	
	/**
	 * <p>*.bmp convert to *.jpg.</p>
	 * @param inputFile 输入文件名称含全路径
	 * @param outputFile 输出文件名称含全路径
	 */
	//public static void reader(String file) {
	public static void toJPEGImage(String inputFile, String outputFile) {
		
		try {
			FileInputStream in = new FileInputStream(inputFile);
			Image TheImage = read(in);
			int wideth = TheImage.getWidth(null);
			int height = TheImage.getHeight(null);
//			wideth = wideth / 2;
//			height = height / 2;
			BufferedImage tag = new BufferedImage(wideth, height,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(TheImage, 0, 0, wideth, height, null);
			FileOutputStream out = new FileOutputStream( outputFile );
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
			encoder.encode(tag);
			out.close();
		}
		catch (Exception e) {
			System.out.println(e);
		}
	}
	
	private static int constructInt(byte[] in, int offset) {
		int ret = ( (int) in[offset + 3] & 0xff);
		ret = (ret << 8) | ( (int) in[offset + 2] & 0xff);
		ret = (ret << 8) | ( (int) in[offset + 1] & 0xff);
		ret = (ret << 8) | ( (int) in[offset + 0] & 0xff);
		return (ret);
	}
	
	private static int constructInt3(byte[] in, int offset) {
		int ret = 0xff;
		ret = (ret << 8) | ( (int) in[offset + 2] & 0xff);
		ret = (ret << 8) | ( (int) in[offset + 1] & 0xff);
		ret = (ret << 8) | ( (int) in[offset + 0] & 0xff);
		return (ret);
	}
	
//	private static long constructLong(byte[] in, int offset) {
//		long ret = ( (long) in[offset + 7] & 0xff);
//		ret |= (ret << 8) | ( (long) in[offset + 6] & 0xff);
//		ret |= (ret << 8) | ( (long) in[offset + 5] & 0xff);
//		ret |= (ret << 8) | ( (long) in[offset + 4] & 0xff);
//		ret |= (ret << 8) | ( (long) in[offset + 3] & 0xff);
//		ret |= (ret << 8) | ( (long) in[offset + 2] & 0xff);
//		ret |= (ret << 8) | ( (long) in[offset + 1] & 0xff);
//		ret |= (ret << 8) | ( (long) in[offset + 0] & 0xff);
//		return (ret);
//	}
	
//	private static double constructDouble(byte[] in, int offset) {
//		long ret = constructLong(in, offset);
//		return (Double.longBitsToDouble(ret));
//	}
	
	private static short constructShort(byte[] in, int offset) {
		short ret = (short) ( (short) in[offset + 1] & 0xff);
		ret = (short) ( (ret << 8) | (short) ( (short) in[offset + 0] & 0xff));
		return (ret);
	}
	
	static class BitmapHeader {
		public int iSize, ibiSize, iWidth, iHeight, iPlanes, iBitcount,
		iCompression, iSizeimage, iXpm, iYpm, iClrused, iClrimp;
		// 读取bmp文件头信息
		public void read(FileInputStream fs) throws IOException {
			final int bflen = 14;
			byte bf[] = new byte[bflen];
			fs.read(bf, 0, bflen);
			final int bilen = 40;
			byte bi[] = new byte[bilen];
			fs.read(bi, 0, bilen);
			iSize = constructInt(bf, 2);
			ibiSize = constructInt(bi, 2);
			iWidth = constructInt(bi, 4);
			iHeight = constructInt(bi, 8);
			iPlanes = constructShort(bi, 12);
			iBitcount = constructShort(bi, 14);
			iCompression = constructInt(bi, 16);
			iSizeimage = constructInt(bi, 20);
			iXpm = constructInt(bi, 24);
			iYpm = constructInt(bi, 28);
			iClrused = constructInt(bi, 32);
			iClrimp = constructInt(bi, 36);
		}
	}
	
	private static Image read(FileInputStream fs) {
		try {
			BitmapHeader bh = new BitmapHeader();
			bh.read(fs);
			if (bh.iBitcount == 24) {
				return (readImage24(fs, bh));
			}
			if (bh.iBitcount == 32) {
				return (readImage32(fs, bh));
			}
			fs.close();
		}
		catch (IOException e) {
			System.out.println(e);
		}
		return (null);
	}
	
	//24位
	protected static Image readImage24(FileInputStream fs, BitmapHeader bh) throws
	IOException {
		Image image;
		if (bh.iSizeimage == 0) {
			bh.iSizeimage = ( ( ( (bh.iWidth * bh.iBitcount) + 31) & ~31) >> 3);
			bh.iSizeimage *= bh.iHeight;
		}
		int npad = (bh.iSizeimage / bh.iHeight) - bh.iWidth * 3;
		int ndata[] = new int[bh.iHeight * bh.iWidth];
		byte brgb[] = new byte[ (bh.iWidth + npad) * 3 * bh.iHeight];
		fs.read(brgb, 0, (bh.iWidth + npad) * 3 * bh.iHeight);
		int nindex = 0;
		for (int j = 0; j < bh.iHeight; j++) {
			for (int i = 0; i < bh.iWidth; i++) {
				ndata[bh.iWidth * (bh.iHeight - j - 1) + i] = constructInt3(
						brgb, nindex);
				nindex += 3;
			}
			nindex += npad;
		}
		image = Toolkit.getDefaultToolkit().createImage
		(new MemoryImageSource(bh.iWidth, bh.iHeight,
				ndata, 0, bh.iWidth));
		fs.close();
		return (image);
	}
	
//	32位
	protected static Image readImage32(FileInputStream fs, BitmapHeader bh) throws
	IOException {
		Image image;
		///int xwidth = bh.iSizeimage / bh.iHeight;
		int ndata[] = new int[bh.iHeight * bh.iWidth];
		byte brgb[] = new byte[bh.iWidth * 4 * bh.iHeight];
		fs.read(brgb, 0, bh.iWidth * 4 * bh.iHeight);
		int nindex = 0;
		for (int j = 0; j < bh.iHeight; j++) {
			for (int i = 0; i < bh.iWidth; i++) {
				ndata[bh.iWidth * (bh.iHeight - j - 1) + i] = constructInt3(
						brgb, nindex);
				nindex += 4;
			}
		}
		image = Toolkit.getDefaultToolkit().createImage
		(new MemoryImageSource(bh.iWidth, bh.iHeight, ndata, 0, bh.iWidth));
		fs.close();
		return (image);
	}
	
	public static Image load(String sdir, String sfile) {
		return (load(sdir + sfile));
	}
	
	public static Image load(String sdir) {
		try {
			FileInputStream fs = new FileInputStream(sdir);
			return (read(fs));
		}
		catch (IOException ex) {
			return (null);
		}
	}
	
//	public ImageDialog getBmpImage(String filePath) throws IOException {
//	if (filePath.equals("")) {
//	System.out.println("输入bmp文件名");
//	return null;
//	}
//	else {
//	FileInputStream in = new FileInputStream(filePath);
//	Image TheImage = read(in);
//	ImageDialog container = new ImageDialog(new ImageIcon(TheImage));
//	return container;
//	}
//	}
	
	public ImageIcon getBmpImageIcon(String filePath) throws IOException {
		if (filePath.equals("")) {
			System.err.println("输入bmp文件名");
			return null;
		}
		else {
			FileInputStream in = new FileInputStream(filePath);
			Image TheImage = read(in);
			return new ImageIcon(TheImage);
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//Image im = BitmapHandler.load("E:/temp_E","tempBmp.bmp");
		
		BitmapReader.toJPEGImage("E:/temp_E/tempBmp.bmp", "E:/temp_E/tempBmp.jpg");
		System.out.println("------------ ok");
	}
	
}


