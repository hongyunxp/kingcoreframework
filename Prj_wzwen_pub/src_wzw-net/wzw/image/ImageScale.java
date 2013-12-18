package wzw.image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;

/**
 * <p>缩略图类，
 *	本java类能将jpg图片文件，进行等比或非等比的大小转换。
 * 	具体使用方法：先构建实例，再调用createImage方法。
 * 	 > 能处理有透明背景层的gif图片。
 *   > 所有图片质量较高。
 * 
 * </p>
 * @author	WUZEWEN on 2005-09-15 
 * @version	1.0
 * @see		Object#equals(java.lang.Object)
 * @see		Object#hashCode()
 * @see		HashMap
 * @since	JDK5
 **/

public class ImageScale {
	
	/**
	 * 日志处理对象。
	 */
	protected static Logger log = Logger.getLogger( wzw.image.ImageScale.class);
	
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

    /** *//**
     * Start: Use Lanczos filter to replace the original algorithm for image
     * scaling. Lanczos improves quality of the scaled image modify by :blade
     */

    public static void main(String[] args) {
        ImageScale is = new ImageScale();
        try {
//            is.saveImageAsJpg("d:/008.jpg", "d:/008bre.jpg", 1024,
//                    768);

            is.saveImageAsJpg("E:/My Documents_200608/Tmp_work/imageScale/e182b2a2b79fa5f7336ddbc41c9c5484.jpg",
            		          "E:/My Documents_200608/Tmp_work/imageScale/e182b2a2b79fa5f7336ddbc41c9c5484_200_bad.jpg", 
            		          200, 200);
            is.saveImageAsJpg("E:/My Documents_200608/Tmp_work/imageScale/42699ecd616adcee2c9cb5e5cb4a440d--小图缩放测试2.gif",
  		          "E:/My Documents_200608/Tmp_work/imageScale/42699ecd616adcee2c9cb5e5cb4a440d--小图缩放测试2_200_bad.jpg", 
  		          200, 200);
            
            
        } catch (Exception e) {
            // TODO Auto-generated catch block
			log.debug("debug", e);
            /// e.pri ntStackTrace();
        }
    }

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
	
	
    private boolean createImage(String fromFileStr, String saveToFileStr, int formatWideth, int formatHeight, boolean gp) 
    		throws IOException {
		// TODO Auto-generated method stub
		try {
			this.saveImageAsJpg(fromFileStr, saveToFileStr, formatWideth, formatHeight);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			log.debug("debug", e);
			/// e.pri ntStackTrace();
			throw new IOException(e.getMessage());
		}
		
		return true;
	}

    
	// fromFileStr原图片地址,saveToFileStr生成缩略图地址,formatWideth生成图片宽度,formatHeight高度
    public void saveImageAsJpg(String fromFileStr, String saveToFileStr,
            int formatWideth, int formatHeight) throws Exception {
        BufferedImage srcImage;
        File saveFile = new File(saveToFileStr);
        File fromFile = new File(fromFileStr);
        srcImage = javax.imageio.ImageIO.read(fromFile); // construct image
        int imageWideth = srcImage.getWidth(null);
        int imageHeight = srcImage.getHeight(null);
        int changeToWideth = 0;
        int changeToHeight = 0;
        if (imageWideth > 0 && imageHeight > 0) {
            // flag=true;
            if (imageWideth / imageHeight >= formatWideth / formatHeight) {
                if (imageWideth > formatWideth) {
                    changeToWideth = formatWideth;
                    changeToHeight = (imageHeight * formatWideth) / imageWideth;
                } else {
                    changeToWideth = imageWideth;
                    changeToHeight = imageHeight;
                }
            } else {
                if (imageHeight > formatHeight) {
                    changeToHeight = formatHeight;
                    changeToWideth = (imageWideth * formatHeight) / imageHeight;
                } else {
                    changeToWideth = imageWideth;
                    changeToHeight = imageHeight;
                }
            }
        }

        srcImage = imageZoomOut(srcImage, changeToWideth, changeToHeight);
        ImageIO.write(srcImage, "JPEG", saveFile);
    }

    public BufferedImage imageZoomOut(BufferedImage srcBufferImage, int w, int h) {
        width = srcBufferImage.getWidth();
        height = srcBufferImage.getHeight();
        scaleWidth = w;

        if (DetermineResultSize(w, h) == 1) {
            return srcBufferImage;
        }
        CalContrib();
        BufferedImage pbOut = HorizontalFiltering(srcBufferImage, w);
        BufferedImage pbFinalOut = VerticalFiltering(pbOut, h);
        return pbFinalOut;
    }

    /** *//**
     * 决定图像尺寸
     */
    private int DetermineResultSize(int w, int h) {
        double scaleH, scaleV;
        scaleH = (double) w / (double) width;
        scaleV = (double) h / (double) height;
        // 需要判断一下scaleH，scaleV，不做放大操作
        if (scaleH >= 1.0 && scaleV >= 1.0) {
            return 1;
        }
        return 0;

    } // end of DetermineResultSize()

    private double Lanczos(int i, int inWidth, int outWidth, double Support) {
        double x;

        x = (double) i * (double) outWidth / (double) inWidth;

        return Math.sin(x * PI) / (x * PI) * Math.sin(x * PI / Support)
                / (x * PI / Support);

    }

    private void CalContrib() {
        nHalfDots = (int) ((double) width * support / (double) scaleWidth);
        nDots = nHalfDots * 2 + 1;
        try {
            contrib = new double[nDots];
            normContrib = new double[nDots];
            tmpContrib = new double[nDots];
        } catch (Exception e) {
            System.out.println("init   contrib,normContrib,tmpContrib" + e);
        }

        int center = nHalfDots;
        contrib[center] = 1.0;

        double weight = 0.0;
        int i = 0;
        for (i = 1; i <= center; i++) {
            contrib[center + i] = Lanczos(i, width, scaleWidth, support);
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
    } // end of CalContrib()

    // 处理边缘
    private void CalTempContrib(int start, int stop) {
        double weight = 0;

        int i = 0;
        for (i = start; i <= stop; i++) {
            weight += contrib[i];
        }

        for (i = start; i <= stop; i++) {
            tmpContrib[i] = contrib[i] / weight;
        }

    } // end of CalTempContrib()

    private int GetRedValue(int rgbValue) {
        int temp = rgbValue & 0x00ff0000;
        return temp >> 16;
    }

    private int GetGreenValue(int rgbValue) {
        int temp = rgbValue & 0x0000ff00;
        return temp >> 8;
    }

    private int GetBlueValue(int rgbValue) {
        return rgbValue & 0x000000ff;
    }

    private int ComRGB(int redValue, int greenValue, int blueValue) {

        return (redValue << 16) + (greenValue << 8) + blueValue;
    }

    // 行水平滤波
    private int HorizontalFilter(BufferedImage bufImg, int startX, int stopX,
            int start, int stop, int y, double[] pContrib) {
        double valueRed = 0.0;
        double valueGreen = 0.0;
        double valueBlue = 0.0;
        int valueRGB = 0;
        int i, j;

        for (i = startX, j = start; i <= stopX; i++, j++) {
            valueRGB = bufImg.getRGB(i, y);

            valueRed += GetRedValue(valueRGB) * pContrib[j];
            valueGreen += GetGreenValue(valueRGB) * pContrib[j];
            valueBlue += GetBlueValue(valueRGB) * pContrib[j];
        }

        valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                Clip((int) valueBlue));
        return valueRGB;

    } // end of HorizontalFilter()

    // 图片水平滤波
    private BufferedImage HorizontalFiltering(BufferedImage bufImage, int iOutW) {
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
                CalTempContrib(start, stop);
                for (y = 0; y < dwInH; y++) {
                    value = HorizontalFilter(bufImage, startX, stopX, start,
                            stop, y, tmpContrib);
                    pbOut.setRGB(x, y, value);
                }
            } else {
                for (y = 0; y < dwInH; y++) {
                    value = HorizontalFilter(bufImage, startX, stopX, start,
                            stop, y, normContrib);
                    pbOut.setRGB(x, y, value);
                }
            }
        }

        return pbOut;

    } // end of HorizontalFiltering()

    private int VerticalFilter(BufferedImage pbInImage, int startY, int stopY,
            int start, int stop, int x, double[] pContrib) {
        double valueRed = 0.0;
        double valueGreen = 0.0;
        double valueBlue = 0.0;
        int valueRGB = 0;
        int i, j;

        for (i = startY, j = start; i <= stopY; i++, j++) {
            valueRGB = pbInImage.getRGB(x, i);

            valueRed += GetRedValue(valueRGB) * pContrib[j];
            valueGreen += GetGreenValue(valueRGB) * pContrib[j];
            valueBlue += GetBlueValue(valueRGB) * pContrib[j];
            // System.out.println(valueRed+"->"+Clip((int)valueRed)+"<-");
            //   
            // System.out.println(valueGreen+"->"+Clip((int)valueGreen)+"<-");
            // System.out.println(valueBlue+"->"+Clip((int)valueBlue)+"<-"+"-->");
        }

        valueRGB = ComRGB(Clip((int) valueRed), Clip((int) valueGreen),
                Clip((int) valueBlue));
        // System.out.println(valueRGB);
        return valueRGB;

    } // end of VerticalFilter()

    private BufferedImage VerticalFiltering(BufferedImage pbImage, int iOutH) {
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
                CalTempContrib(start, stop);
                for (int x = 0; x < iW; x++) {
                    value = VerticalFilter(pbImage, startY, stopY, start, stop,
                            x, tmpContrib);
                    pbOut.setRGB(x, y, value);
                }
            } else {
                for (int x = 0; x < iW; x++) {
                    value = VerticalFilter(pbImage, startY, stopY, start, stop,
                            x, normContrib);
                    pbOut.setRGB(x, y, value);
                }
            }

        }

        return pbOut;

    } // end of VerticalFiltering()

    int Clip(int x) {
        if (x < 0)
            return 0;
        if (x > 255)
            return 255;
        return x;
    }
}