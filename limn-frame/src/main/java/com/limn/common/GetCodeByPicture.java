package com.limn.common;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * 通过图片获取验证码 首先将图片进行处理，黑白掉 在通过Tesseract-OCR 这个工具识别
 * 
 * @author limengnan
 * 
 */
public class GetCodeByPicture {

	public static void main(String[] args) {
		new GetCodeByPicture()
				.identification("http://172.16.10.35:8080/hoss-web/hoss/sys/v2/getVerCode.do");
	}
	
	/**
	 * 
	 * @param instr InputStream 
	 * @return 验证码
	 */
	public String identification(InputStream instr){
		File pic = null;
		try {
			pic = getInputStream(instr);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return run(pic);
		
	}
	
	/**
	 * 
	 * @param pic 图片文件地址
	 * @return
	 */
	public String identificationByPath(String picPath){
		File pic = new File(picPath);
		return run(pic);
	}
	

	/**
	 * 
	 * @param url 图片地址
	 * @return 验证码
	 */
	public String identification(String url) {
		File pic = null;
		try {
			pic = getIntnetPicture(url);
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return run(pic);
	}
	
	private String run(File pic){
		PictureChange pc = new PictureChange();
		String codePath = System.getProperty("user.dir") + "/code.jpg";
		String code = null;
		try {

			pc.exeRun(pic, codePath);

			code = recognizeText(new File(codePath));

		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		System.out.println(code);
		return code;
	}
	
	private File getInputStream(InputStream inStream) throws Exception{
		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		File imageFile = new File("OriginalCode.jpg");
		// 创建输出流
		FileOutputStream outStream = new FileOutputStream(imageFile);
		// 写入数据
		outStream.write(data);
		// 关闭输出流
		outStream.close();
		return imageFile;
	}
	
	
	
	private File getIntnetPicture(String urlPath) throws Throwable {
		// new一个URL对象
		URL url = new URL(urlPath);
		// 打开链接
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		// 设置请求方式为"GET"
		conn.setRequestMethod("GET");
		// 超时响应时间为5秒
		conn.setConnectTimeout(5 * 1000);
		// 通过输入流获取图片数据
		InputStream inStream = conn.getInputStream();
		// 得到图片的二进制数据，以二进制封装得到数据，具有通用性
		byte[] data = readInputStream(inStream);
		// new一个文件对象用来保存图片，默认保存当前工程根目录
		File imageFile = new File("BeautyGirl.jpg");
		// 创建输出流
		FileOutputStream outStream = new FileOutputStream(imageFile);
		// 写入数据
		outStream.write(data);
		// 关闭输出流
		outStream.close();
		return imageFile;
	}

	private byte[] readInputStream(InputStream inStream) throws Exception {
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		// 创建一个Buffer字符串
		byte[] buffer = new byte[1024];
		// 每次读取的字符串长度，如果为-1，代表全部读取完毕
		int len = 0;
		// 使用一个输入流从buffer里把数据读取出来
		while ((len = inStream.read(buffer)) != -1) {
			// 用输出流往buffer里写入数据，中间参数代表从哪个位置开始读，len代表读取的长度
			outStream.write(buffer, 0, len);
		}
		// 关闭输入流
		inStream.close();
		// 把outStream里的数据写入内存
		return outStream.toByteArray();
	}

//	private final String EOL = System.getProperty("line.separator");

	private String recognizeText(File imageFile) throws Exception {
		File tempImage = imageFile;
		File outputFile = new File(imageFile.getParentFile(), "output");
		StringBuffer strB = new StringBuffer();
		List<String> cmd = new ArrayList<String>();
		String OS = System.getProperties().getProperty("os.name");
		if (OS.endsWith("Linux")) {
			cmd.add("tesseract ");
		} else {
			cmd.add(System.getProperty("user.dir") + "/bin/Tesseract-OCR/tesseract");
		}
		cmd.add("");
		cmd.add(outputFile.getName());
		// cmd.add(LANG_OPTION);
		// cmd.add("chi_sim");
		// cmd.add("eng");

		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(imageFile.getParentFile());

		cmd.set(1, tempImage.getName());
		pb.command(cmd);
		pb.redirectErrorStream(true);

		Process process = pb.start();
		// tesseract.exe 1.jpg 1 -l chi_sim
		int w = process.waitFor();

		// 删除临时正在工作文件
		tempImage.delete();
		String formatCode = null;
		if (w == 0) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					new FileInputStream(outputFile.getAbsolutePath() + ".txt"),
					"UTF-8"));

			formatCode = in.readLine();
	
			in.close();
		}
		
		tempImage.delete();

		new File(outputFile.getAbsolutePath() + ".txt").delete();
		

		if(formatCode==null){
			return "";
		}
		formatCode = formatCode.replace("|", "l");
		if (formatCode.length() > 5 ){
			formatCode = formatCode.replace("Li", "u");
			formatCode = formatCode.replace("LI", "u");
			formatCode = formatCode.replace("Ll", "u");
			formatCode = formatCode.replace("i'i", "n");
			formatCode = formatCode.replace("l'", "r");
			formatCode = formatCode.replace("l'I", "h");
			formatCode = formatCode.replace(" ", "");
		}
		return formatCode;
	}
}
