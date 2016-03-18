package com.automation.tool.util;

import com.automation.driver.Driver;
import com.automation.report.Log;
import com.automation.tool.parameter.Parameters;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Screenshot {

	private String fileName; // 文件的前缀

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


    public void snapShot(Log log){
        String screenShotFolderPath = Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH + "\\screenshot";
        if (!FileUtil.exists(screenShotFolderPath)){
            FileUtil.createFloder(screenShotFolderPath);
        }
        String shotPicName = DateFormat.getDate("yyyy-MM-dd_HH-mm-ss");
        String shotPath = screenShotFolderPath + "\\" + shotPicName;
        try {
            File sourceFile = ((TakesScreenshot) Driver.getWebDriver()).getScreenshotAs(OutputType.FILE);
            FileUtil.copyFile(sourceFile, new File(shotPath+"."+defaultImageFormat));
        } catch (IOException e) {
            Print.log(e.getMessage());
        } catch (Exception e1){
            e1.printStackTrace();
        }
        log.setScreenShotPic(shotPicName + ".png");
    }

    public void snapShot(){
        Log log = Print.getLogs();
        String screenShotFolderPath = Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH + "\\screenshot";
        if (!FileUtil.exists(screenShotFolderPath)){
            FileUtil.createFloder(screenShotFolderPath);
        }
        String shotPicName = DateFormat.getDate("yyyy-MM-dd_HH-mm-ss") + "_wait";
        String shotPath = screenShotFolderPath + "\\" + shotPicName;
        String picPath = shotPath+"."+defaultImageFormat;
        try {
            File sourceFile = ((TakesScreenshot) Driver.getWebDriver()).getScreenshotAs(OutputType.FILE);
            FileUtil.copyFile(sourceFile, new File(picPath));
            // 把每张截图放到log中
            if(log!=null) {
				log.addScreenShotPicForSecond(shotPicName + ".png");
			}
        } catch (IOException e) {
            Print.log(e.getMessage());
        } catch (Exception e1){
            e1.printStackTrace();
        }
    }
}