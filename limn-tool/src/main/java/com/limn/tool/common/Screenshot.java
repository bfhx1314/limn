package com.limn.tool.common;

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

	private String fileName; // 文件的前缀


	static int serialNum = 0;

	private String imageFormat; // 图像文件的格式

	private String defaultImageFormat = "png";

	Dimension d = Toolkit.getDefaultToolkit().getScreenSize();


	/**
	 * 默认截图文件格式 png
	 */
	public Screenshot() {
		imageFormat = defaultImageFormat;
	}

	/**
	 * 自定义截图格式 
	 * @param format
	 */
	public Screenshot(String format) {
		imageFormat = format;
	}

	/**
	 * 文件保存
	 * @param bitMapPath  文件保存路径 不需要带后缀名
	 */
	public String snapShot(String bitMapPath) {
		fileName = bitMapPath;
		
		try {
			// 拷贝屏幕到一个BufferedImage对象screenshot
			BufferedImage screenshot = (new Robot())
					.createScreenCapture(new Rectangle(0, 0,
							(int) d.getWidth(), (int) d.getHeight()));


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