package com.limn.tool.common;
 
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
 


import javax.imageio.ImageIO;
 
public class PictureChange {
 
    
    
    
    public void exeRun(File picFile, String targetPath) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(picFile);
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();
 
        // 灰度化
        int[][] gray = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int argb = bufferedImage.getRGB(x, y);
                int r = (argb >> 16) & 0xFF;
                int g = (argb >> 8) & 0xFF;
                int b = (argb >> 0) & 0xFF;
                int grayPixel = (int) ((b * 29 + g * 150 + r * 77 + 128) >> 8);                
                gray[x][y] = grayPixel;
            }
        }
 
        // 二值化
        int threshold = ostu(gray, w, h);
        BufferedImage binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
            	
            	//去掉图片周围的边框
              	if(y==0 || x==0 || y+1==h || x+1==w){
            		gray[x][y]|= 0x00FFFF;	
            	}
            	
                if (gray[x][y] > threshold) {  
                    gray[x][y] |= 0x00FFFF; 
                } else {
                    gray[x][y] &= 0xFF0000; 
                }
                
                binaryBufferedImage.setRGB(x, y, gray[x][y]);
            }
        }
 
        // 矩阵打印
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {


                if (isBlack(binaryBufferedImage.getRGB(x, y))) {
  
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }
        targetPath = targetPath.replace("\\", "/");
        
        
        FileOutputStream a =  new FileOutputStream(targetPath);
        ImageIO.write(binaryBufferedImage, "jpg", a);

        a.close();
        try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
 
    public static boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300) {
            return true;
        }
        return false;
    }
 
    public static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 300) {
            return true;
        }
        return false;
    }
 
    public static int isBlackOrWhite(int colorInt) {
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730) {
            return 1;
        }
        return 0;
    }
 
    public static int getColorBright(int colorInt) {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue();
    }
 
    public static int ostu(int[][] gray, int w, int h) {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int red = 0xFF & gray[x][y];
                histData[red]++;
            }
        }
 
        // Total number of pixels
        int total = w * h;
 
        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histData[t];
 
        float sumB = 0;
        int wB = 0;
        int wF = 0;
 
        float varMax = 0;
        int threshold = 0;
 
        for (int t = 0; t < 256; t++) {
            wB += histData[t]; // Weight Background
            if (wB == 0)
                continue;
 
            wF = total - wB; // Weight Foreground
            if (wF == 0)
                break;
 
            sumB += (float) (t * histData[t]);
 
            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground
 
            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);
 
            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }
 
        return threshold;
    }
}