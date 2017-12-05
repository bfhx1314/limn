package com.limn.tool.common;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.JarURLConnection;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.limn.tool.parameter.Parameter;
import com.limn.tool.regexp.RegExp;

public class FileUtil {

	/**
	 * 文件是否存在
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @return
	 */
	public static boolean exists(String filePath) {
		return new File(filePath).exists();
	}

	/**
	 * 创建文件
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @return
	 */
	public static void createFile(String filePath) {
		try {
			new File(filePath).createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// public static void main(String[] args){
	// createFloder("C:\\aa\\aaa");
	// createFile("C:\\variable.properties");
	// }
	/**
	 * 清空文件内容
	 * 
	 * @param filePath
	 *            文件绝对路径
	 */
	public static void setEmpty(String filePath) {
		File file = new File(filePath);
		FileWriter fw;
		try {
			fw = new FileWriter(file);
			fw.write("");
			fw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件父路径
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @return
	 */
	public static String getParent(String filePath) {
		return new File(filePath).getParent();
	}

	/**
	 * 获取文件名
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @return
	 */
	public static String getName(String filePath) {
		return new File(filePath).getName();
	}

	/**
	 * 获取文件后缀名
	 * @param filePath
	 * @return
	 */
	public static String getFileType(String filePath){
		String fileName = getName(filePath);
		String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
		return prefix;
	}
	
	/**
	 * 获取文件后缀名
	 * @param file
	 * @return
	 */
	public static String getFileType(File file){
		String fileName = file.getName();
		String prefix = fileName.substring(fileName.lastIndexOf(".")+1);
		return prefix;
	}
	
	
	@SuppressWarnings("resource")
	public static long getSize(String filePath) throws IOException {
		File f = new File(filePath);
		long size = 0;
		if (f.exists()) {
			FileInputStream fis = null;
			fis = new FileInputStream(f);
			size = fis.available();
		} else {
			f.createNewFile();
			System.out.println("文件不存在");
		}
		return size;
	}

	/**
	 * 创建文件夹
	 * 
	 * @param filePath
	 *            文件夹路径
	 */
	public static void createFloder(String filePath) {
		new File(filePath).mkdirs();
	}

	/**
	 * 读取文件内容
	 * 
	 * @param filePath
	 *            文件绝对路径（TXT、sql）
	 * @return
	 */
	public static String readFile(String filePath) {
		StringBuilder sbr = new StringBuilder();
		String str = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((str = br.readLine()) != null) {
				sbr.append(str + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sbr.toString();
	}

	/**
	 * 执行每一行sql
	 * 
	 * @param filePath
	 *            文件绝对路径（TXT、sql）
	 * @return
	 */
	public static String readFileExecuteSql(String filePath) {
		StringBuilder sbr = new StringBuilder();
		String str = "";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while ((str = br.readLine()) != null) {
				sbr.append(str + "\n");
			}
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sbr.toString();
	}

	/**
	 * 写文件
	 * 
	 * @param filePath
	 *            文件路径，不存在会新建
	 * @param str
	 *            内容
	 */
	public static void writeFile(String filePath, String str) {
		if (!str.isEmpty()) {
			if (!exists(filePath)) {
				try {
					new File(filePath).createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter fw;
			try {
				fw = new FileWriter(filePath, true);
				fw.write(str, 0, str.length());
				fw.flush();
				fw.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFile
	 *            源文件
	 * @param targetFile
	 *            目标文件
	 * @throws IOException
	 */
	public static void copyFile(File sourceFile, File targetFile) throws IOException {
		if (!targetFile.exists()) {
			File dir = targetFile.getParentFile(); // new
			if (!dir.exists())
				dir.mkdirs();
			targetFile.createNewFile();
		}
		if (!sourceFile.exists()) {
			// throw new ParameterException(ParameterException.FILE_NOT_EXIST,
			// sourceFile.getAbsolutePath() + " 文件不存在");
		}
		InputStream is = null;
		OutputStream os = null;
		int len;
		byte[] buff = new byte[1024];
		try {
			is = new FileInputStream(sourceFile);
			os = new FileOutputStream(targetFile);
			while ((len = is.read(buff)) != -1) {
				os.write(buff, 0, len);
			}
			os.flush();
		} finally {
			is.close();
			os.close();
		}

		// BufferedInputStream inBuff = null;
		// BufferedOutputStream outBuff = null;
		// try {
		// // 新建文件输入流并对它进行缓冲
		// inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
		//
		// // 新建文件输出流并对它进行缓冲
		// outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
		//
		// // 缓冲数组
		// byte[] b = new byte[1024 * 5];
		// int len;
		// while ((len = inBuff.read(b)) != -1) {
		// outBuff.write(b, 0, len);
		// }
		// // 刷新此缓冲的输出流
		// outBuff.flush();
		// } finally {
		// // 关闭流
		// if (inBuff != null)
		// inBuff.close();
		// if (outBuff != null)
		// outBuff.close();
		// }
	}

	/**
	 * 复制文件夹
	 * 
	 * @param sourceDir
	 *            源文件夹
	 * @param targetDir
	 *            目标文件夹
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir) throws IOException {
		if (!exists(targetDir)) {
			// 新建目标目录
			(new File(targetDir)).mkdirs();
		}
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(new File(targetDir).getAbsolutePath() + File.separator + file[i].getName());
				// try {
				copyFile(sourceFile, targetFile);
				// } catch (ParameterException e) {
				// // TODO Auto-generated catch block
				// e.printStackTrace();
				// }
			}
			if (file[i].isDirectory()) {
				// 准备复制的源文件夹
				String dir1 = sourceDir + "/" + file[i].getName();
				// 准备复制的目标文件夹
				String dir2 = targetDir + "/" + file[i].getName();
				copyDirectiory(dir1, dir2);
			}
		}
	}

	public static void unZip(String path) throws IOException {

		String parentPath = new File(path).getParent() + "/";
		ZipFile zipFile = new ZipFile(path);

		for (Enumeration<?> entries = zipFile.getEntries(); entries.hasMoreElements();) {
			ZipEntry entry = (ZipEntry) entries.nextElement();
			String zipEntryName = entry.getName();
			String outPath = (parentPath + zipEntryName).replaceAll("\\*", "/");
			File file = new File(outPath.substring(0, outPath.lastIndexOf('/')));
			if (!file.exists()) {
				file.mkdirs();
			}
			if (new File(outPath).isDirectory()) {
				continue;
			}
			InputStream in = zipFile.getInputStream(entry);
			// 输出文件路径信息
			System.out.println(outPath);

			OutputStream out = new FileOutputStream(outPath);
			byte[] buf1 = new byte[1024];
			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}

	}

	public static String findFileByType(String path, String type) {
		File findFile = new File(path);
		for (File f : findFile.listFiles()) {
			String fileName = f.getName();
			int i = fileName.lastIndexOf(".");
			if (fileName.substring(i + 1).equalsIgnoreCase(type)) {
				return f.getAbsolutePath();
			}
		}
		return null;
	}

	public static HashSet<String> findFilesByType(String path, String type) {
		File findFile = new File(path);
		HashSet<String> set = new HashSet<String>();
		for (File f : findFile.listFiles()) {
			String fileName = f.getName();
			int i = fileName.lastIndexOf(".");
			if (fileName.substring(i + 1).equalsIgnoreCase(type)) {
				set.add(fileName);
			}
		}
		return set;
	}

	public static HashSet<String> findFiles(String path, boolean isDict) {
		File findFile = new File(path);
		HashSet<String> set = new HashSet<String>();
		for (File f : findFile.listFiles()) {

			if (f.isFile()) {
				set.add(f.getName());
			} else if (f.isDirectory() == isDict && !RegExp.findCharacters(f.getName(), "^\\.")) {
				set.add(f.getName());
			}
		}
		return set;
	}

	/**
	 * 对于文件内容
	 * 
	 * @param path 文件路径
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String getFileText(String path) throws IOException {

		File file = new File(path);

		if (!file.exists() || file.isDirectory()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
			setFileText(path, "0");
		}

		FileInputStream fis = new FileInputStream(file);
		byte[] buf = new byte[1024];
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int len;
		while ((len = fis.read(buf)) != -1) {
			baos.write(buf, 0, len);
		}
		// System.out.println(baos.toString());
		fis.close();
		return baos.toString();
	}

	/**
	 * 写入文件内容
	 * 
	 * @param path
	 *            文件路径
	 * @param text
	 *            内容
	 */
	public static void setFileText(String path, String text) {
		FileOutputStream out = null;
		try {
			out = new FileOutputStream(new File(path));
			out.write(text.getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	public static void copyFile(File file, String filePath){
		if(file.isDirectory()){
			for(File childFile : file.listFiles()){
				copyFile(childFile,filePath + "/" + file.getName());
			}
		}else{
			try {
				createFloder(filePath);
				saveToFile(filePath + "/" + file.getName(), new FileInputStream(file));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * 返回文件绝对路径
	 * 
	 * @param AbsolutelyPath
	 * @param path
	 * @return
	 */
	public static String getFileAbsolutelyPath(String AbsolutelyPath, String path) {
		if (RegExp.findCharacters(path, "[/|\\\\]")) {
			return path;
		} else {
			return AbsolutelyPath + "/" + path;
		}
	}

	public static void copySourceToFolder(String sourceFilePath, String filePath, Class<?> clasz) {
		
		
		File file = new File(filePath);
		
//		URL resource = clasz.getClass().getResource(sourceFilePath);
		URL resource = clasz.getClassLoader().getResource(sourceFilePath);
		try {
			if(resource.openConnection() instanceof JarURLConnection){
				JarURLConnection urlConnection = (JarURLConnection) resource.openConnection();
				copyJarResourcesRecursively(file, urlConnection);
			}else{
				File files = new File(resource.toURI());
				for(File fileChild : files.listFiles()){
					copyFile(fileChild,filePath);
				}
			}
		} catch (IOException e2) {
			
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		
//		
//		if (!file.exists()) {
//			// 不存在就将jar包里的ParameterValues.xml复制到指定路径下
//			File f = new File(Parameter.DEFAULT_TEMP_PATH);
//			f.mkdirs();
//
//			InputStream is = clasz.getClassLoader().getResourceAsStream(sourceFilePath);
//
//			try {
//				saveToFile(filePath, is);
//			} catch (IOException e) {
//				Print.log("保存文件异常:" + e.getMessage(), 2);
//			}
//		}


	}

	/**
	 * 转换String
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static String inputStream2String(InputStream is) throws IOException {
		StringBuilder sb1 = new StringBuilder();
		byte[] bytes = new byte[4096];
		int size = 0;

		try {
			while ((size = is.read(bytes)) > 0) {
				String str = new String(bytes, 0, size, "UTF-8");
				sb1.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb1.toString();
	}

	/**
	 * 保存文件
	 * 
	 * @param fileName
	 * @param in
	 * @throws IOException
	 */
	public static void saveToFile(String fileName, InputStream in) throws IOException {
		FileOutputStream fos = null;
		BufferedInputStream bis = null;
		// HttpURLConnection httpUrl = null;
		// URL url = null;
		int BUFFER_SIZE = 1024;
		byte[] buf = new byte[BUFFER_SIZE];
		int size = 0;
		//
		// //建立链接
		// url = new URL(destUrl);
		// httpUrl = (HttpURLConnection) url.openConnection();
		// //连接指定的资源
		// httpUrl.connect();
		// //获取网络输入流
		bis = new BufferedInputStream(in);
		// //建立文件
		fos = new FileOutputStream(fileName);
		//
		// //保存文件
		while ((size = bis.read(buf)) != -1)
			fos.write(buf, 0, size);
		//
		fos.close();
		bis.close();
		// httpUrl.disconnect();
	}

	private static void copyJarResourcesRecursively(File destination, JarURLConnection jarConnection) throws IOException {
		JarFile jarFile = jarConnection.getJarFile();
		Enumeration<JarEntry> entries = jarFile.entries();
		while (entries.hasMoreElements()) { // 遍历jar内容逐个copy
			JarEntry entry = entries.nextElement();
			if (entry.getName().startsWith(jarConnection.getEntryName())) {
				String fileName = StringUtils.removeStart(entry.getName(), jarConnection.getEntryName());
				File destFile = new File(destination, fileName);
				if (!entry.isDirectory()) {
					InputStream entryInputStream = jarFile.getInputStream(entry);
					FileUtils.copyInputStreamToFile(entryInputStream, destFile);
				} else {
					FileUtils.forceMkdir(destFile);
				}
			}
		}
	}

	public static String getNewFilePath(String filePath){
		String flag = "_1";
		String fileType = FileUtil.getFileType(filePath);
		while(new File(filePath).exists()){

			ArrayList<String> res = RegExp.matcherCharacters(filePath , "_\\d{1,}\\." + fileType + "$");
			if(res.size() == 1){
				ArrayList<String> resInt = RegExp.matcherCharacters(res.get(0),"\\d{1,}");
				int num = Integer.valueOf(resInt.get(0)) + 1;
				flag = "_" + num + "." + fileType;

				filePath = filePath.replace(res.get(0),flag);
			}else{
				String name = new File(filePath).getName();
				filePath = new File(filePath).getParent() + "\\" + name.substring(0,name.lastIndexOf(".")) + flag + "." + fileType;
			}



		}
		return  filePath;

	}


	public static void main(String[] args){

		String a = getNewFilePath("D:\\workspace\\limn\\ResultsFolder\\趣头条\\20171201_234645\\BitMap\\0\\3\\0_M启动.png");
		System.out.println(a);
	}


}
