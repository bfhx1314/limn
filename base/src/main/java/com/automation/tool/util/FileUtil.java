package com.automation.tool.util;

import com.automation.exception.MException;
import com.automation.exception.RunException;
import com.automation.tool.parameter.Parameters;
import com.google.common.io.Resources;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
     * 判断数据源是文件夹
     * @param filePath
     * @return
     */
    public static boolean isDirectory(String filePath){
        return new File(filePath).isDirectory();
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

    public static void deleteFloder(String filePath){
        new File(filePath).delete();
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

        try {
            File f = new File(filePath);
            if (!f.exists()) {
                f.createNewFile();
            }
            OutputStreamWriter write = new OutputStreamWriter(new FileOutputStream(f),"UTF-8");
            BufferedWriter writer=new BufferedWriter(write);
            //PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter(filePathAndName)));
            //PrintWriter writer = new PrintWriter(new FileWriter(filePathAndName));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            System.out.println("写文件内容操作出错");
            e.printStackTrace();
        }

/*		if (!str.isEmpty()) {
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
				e.printStackTrace();
			}
		}*/
	}

	/**
	 * 复制文件
	 * 
	 * @param sourceFilePaht
	 *            源文件
	 * @param targetFilePath
	 *            目标文件
	 * @throws java.io.IOException
	 */
	public static void copyFile(String sourceFilePaht, String targetFilePath) throws IOException {
        File sourceFile = new File(sourceFilePaht);
        File targetFile = new File(targetFilePath);
		copyFile(sourceFile,targetFile);

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
	 * @throws java.io.IOException
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
	 * @throws java.io.IOException
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
			return AbsolutelyPath + "\\" + path;
		}
	}

    public static void copySourceToFolder(String sourceFilePath, String filePath, Class<?> clasz) throws RunException {
        File file = new File(filePath);
//		URL resource = clasz.getClass().getResource(sourceFilePath);
        URL resource = clasz.getClassLoader().getResource(sourceFilePath);
        URLConnection urlConnection;
        try {
            urlConnection = resource.openConnection();
            copyJarResourcesRecursively(file, (JarURLConnection)urlConnection);
        } catch (Exception e1) {
            try {
                String reportResource = FileUtil.class.getResource("/report").getPath();
                FileUtil.copyDirectiory(reportResource, Parameters.RESULT_PRJ_TIME_TESTCASE_REPORT_PATH);
            } catch (Exception e) {
                throw new RunException(e);

            }
        }
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

}
