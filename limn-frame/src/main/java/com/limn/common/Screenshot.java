package com.limn.common;

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;


/**
 * 屏幕截图
 * @author limn
 *
 */
public class Screenshot {

	private static String fileName; // 文件的前缀


	private static int serialNum = 0;



	private static String defaultImageFormat = "jpg";
	
	private static String imageFormat = defaultImageFormat; // 图像文件的格式

	private static Dimension d = Toolkit.getDefaultToolkit().getScreenSize();


	/**
	 * 自定义截图格式 
	 * @param format
	 */
	public static void setFormat(String format) {
		imageFormat = format;
	}

	/**
	 * 文件保存
	 * @param bitMapPath  文件保存路径 不需要带后缀名
	 */
	public static String snapShot(String bitMapPath, int x, int y, int witdh,	int height) {
		
		
		fileName = bitMapPath;
		
		try {
			// 拷贝屏幕到一个BufferedImage对象screenshot
			BufferedImage screenshot = new Robot().createScreenCapture(new Rectangle(y, x+50, witdh, height));


			// 根据文件前缀变量和文件格式变量，自动生成文件名
			String name = fileName + "." + imageFormat;
			File f = new File(name);
			// 父目录不存在就创建
			if(!f.getParentFile().exists()){
				f.getParentFile().mkdirs();
			}
//			System.out.print("Save File " + name);
			// 将screenshot对象写入图像文件
			ImageIO.write(screenshot, imageFormat, f);
//			System.out.print("..Finished!\n");
		} catch (Exception ex) {
			System.out.println(ex);
		}
		return bitMapPath + "." + imageFormat;
	}
}