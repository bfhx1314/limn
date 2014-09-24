package com.limn.tool.common;

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
import java.util.Enumeration;
import java.util.HashSet;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;



public class FileUtil {
	
	/**
	 * 文件是否存在
	 * @param filePath 文件绝对路径
	 * @return
	 */
	public static boolean exists(String filePath){
		return new File(filePath).exists();
	}
	
//	public static void main(String[] args){
//		createFloder("C:\\AA\\AA");
//	}
	
	/**
	 * 获取文件父路径
	 * @param filePath 文件绝对路径
	 * @return
	 */
	public static String getParent(String filePath){
		return new File(filePath).getParent();
	}
	
	/**
	 * 获取文件名
	 * @param filePath 文件绝对路径
	 * @return
	 */
	public static String getName(String filePath){
		return new File(filePath).getName();
	}
	
	@SuppressWarnings("resource")
	public static long getSize(String filePath) throws IOException{
		File f = new File(filePath);
        long size=0;
        if (f.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(f);
            size= fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        return size;
	}
	
	/**
	 * 创建文件夹
	 * @param filePath 文件夹路径
	 */
	public static void createFloder(String filePath){
		new File(filePath).mkdirs();
	}
	
	/**
	 * 读取文件内容
	 * @param filePath 文件绝对路径（TXT、sql）
	 * @return
	 */
	public static String readFile(String filePath){
		StringBuilder sbr = new StringBuilder();
		String str ="";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while((str = br.readLine()) != null) {
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
	 * @param filePath 文件绝对路径（TXT、sql）
	 * @return
	 */
	public static String readFileExecuteSql(String filePath){
		StringBuilder sbr = new StringBuilder();
		String str ="";
		BufferedReader br;
		try {
			br = new BufferedReader(new FileReader(filePath));
			while((str = br.readLine()) != null) {
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
	 * @param filePath 文件路径，不存在会新建
	 * @param str 内容
	 */
	public static void writeFile(String filePath, String str){
		if (!str.isEmpty()) {
			if (!exists(filePath)){
				try {
					new File(filePath).createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			FileWriter fw;
			try {
				fw = new FileWriter(filePath,true);
		        fw.write(str,0,str.length());    
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
	 * @param sourceFile 源文件
	 * @param targetFile 目标文件
	 * @throws IOException
	 * @throws ParameterException 
	 */
	public static void copyFile(File sourceFile, File targetFile)
			throws IOException {
		if (!targetFile.exists()) {
             File dir = targetFile.getParentFile(); // new
             if (!dir.exists())
                     dir.mkdirs();
             targetFile.createNewFile();
		}
		if(!sourceFile.exists()){
//			throw new ParameterException(ParameterException.FILE_NOT_EXIST, sourceFile.getAbsolutePath() + " 文件不存在");
		}
		InputStream is = null;		
		OutputStream os = null;
		int len;
		byte []buff = new byte[1024];
		try {
			is = new FileInputStream(sourceFile);
			os = new FileOutputStream(targetFile);
			while((len = is.read(buff)) != -1){
				os.write(buff, 0, len);
			}
			os.flush();
		}finally{
			is.close();			
			os.close();		
		}
		
//		BufferedInputStream inBuff = null;
//		BufferedOutputStream outBuff = null;
//		try {
//			// 新建文件输入流并对它进行缓冲
//			inBuff = new BufferedInputStream(new FileInputStream(sourceFile));
//
//			// 新建文件输出流并对它进行缓冲
//			outBuff = new BufferedOutputStream(new FileOutputStream(targetFile));
//
//			// 缓冲数组
//			byte[] b = new byte[1024 * 5];
//			int len;
//			while ((len = inBuff.read(b)) != -1) {
//				outBuff.write(b, 0, len);
//			}
//			// 刷新此缓冲的输出流
//			outBuff.flush();
//		} finally {
//			// 关闭流
//			if (inBuff != null)
//				inBuff.close();
//			if (outBuff != null)
//				outBuff.close();
//		}
	}

	/**
	 * 复制文件夹
	 * @param sourceDir 源文件夹
	 * @param targetDir 目标文件夹
	 * @throws IOException
	 */
	public static void copyDirectiory(String sourceDir, String targetDir)
			throws IOException {
		// 新建目标目录
		(new File(targetDir)).mkdirs();
		// 获取源文件夹当前下的文件或目录
		File[] file = (new File(sourceDir)).listFiles();
		for (int i = 0; i < file.length; i++) {
			if (file[i].isFile()) {
				// 源文件
				File sourceFile = file[i];
				// 目标文件
				File targetFile = new File(
						new File(targetDir).getAbsolutePath() + File.separator
								+ file[i].getName());
//				try {
					copyFile(sourceFile, targetFile);
//				} catch (ParameterException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
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
	
	
	public static String findFileByType(String path, String type){
		File findFile = new File(path);
		for(File f:findFile.listFiles()){
			String fileName = f.getName();
			int i = fileName.lastIndexOf(".");
			if(fileName.substring(i+1).equalsIgnoreCase(type)){
				return f.getAbsolutePath();
			}
		}
		return null;
	}
	
	public static HashSet<String> findFilesByType(String path, String type){
		File findFile = new File(path);
		HashSet<String> set = new HashSet<String>();
		for(File f:findFile.listFiles()){
			String fileName = f.getName();
			int i = fileName.lastIndexOf(".");
			if(fileName.substring(i+1).equalsIgnoreCase(type)){
				set.add(fileName);
			}
		}
		return set;
	}
	
	public static HashSet<String> findFiles(String path,boolean isDict){
		File findFile = new File(path);
		HashSet<String> set = new HashSet<String>();
		for(File f:findFile.listFiles()){
			
			if(f.isFile()){
				set.add(f.getName());
			}else if(f.isDirectory() == isDict){
				set.add(f.getName());
			}
		}
		return set;
	}
	
	
	
	/**
	 * 对于文件内容
	 * 
	 * @param 文件路径
	 * @return 文件内容
	 * @throws IOException
	 */
	public static String getFileText(String path) throws IOException {

		File file = new File(path);

		if (!file.exists() || file.isDirectory())
		{
			file.getParentFile().mkdirs();
			file.createNewFile();
			setFileText(path,"0");
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
	 * @param path 文件路径
	 * @param text 内容
	 */
	public static void setFileText(String path, String text){
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

//	public static void main(String[] args){
//		String a = "aaaaaa.xtx";
//		int i = a.lastIndexOf(".");
//		System.out.println(findFileByType("D:\\Automation\\Test Case\\Business831","xls"));
//	}
	
	
}
