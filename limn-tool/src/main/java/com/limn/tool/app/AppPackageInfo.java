package com.limn.tool.app;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import com.dd.plist.NSArray;
import com.dd.plist.NSDictionary;
import com.dd.plist.NSString;
import com.dd.plist.PropertyListParser;
import com.limn.tool.common.FileUtil;
import com.limn.tool.regexp.RegExp;

public class AppPackageInfo {
	
	
	
	private File zipFile = null;
	private File ipa = null;

	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public IOSPackageInfo resolveByIOS(String filePath) {
		return resolveByIOS(new File(filePath));
	}
	
	/**
	 * 
	 * @param filePath
	 * @return
	 */
	public IOSPackageInfo resolveByIOS(File filePath) {
		IOSPackageInfo iif = null;
		if (filePath.exists()) {
			if (FileUtil.getFileType(filePath).equalsIgnoreCase("ipa")) {
				try {
					iif = getIpaInfoMap(filePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return iif;
	}
	
	
	
	
	public ApkInfo resolveByAndroid(File filePath) {
		
		ApkInfo iif = null;
		if (filePath.exists()) {
			if (FileUtil.getFileType(filePath).equalsIgnoreCase("apk")) {
				try {
					iif = getApkInfoMap(filePath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		return iif;
	}
	
	
	


	private File getZipInfo(String regexp) throws Exception {
		// 定义输入输出流对象
		InputStream input = null;
		OutputStream output = null;
		File result = null;
		File unzipFile = null;
		ZipFile zipFile = null;
		try {
			// 创建zip文件对象
			
			zipFile = new ZipFile(this.zipFile);
			// 创建本zip文件解压目录
			String name = this.zipFile.getName().substring(0, this.zipFile.getName().lastIndexOf("."));
			unzipFile = new File(this.zipFile.getParent() + "/" + name);
			if (unzipFile.exists()) {
				unzipFile.delete();
			}
			unzipFile.mkdir();
			// 得到zip文件条目枚举对象
			Enumeration<ZipEntry> zipEnum = zipFile.getEntries();
			// 定义对象
			ZipEntry entry = null;
			String entryName = null;
			String names[] = null;
			int length;
			// 循环读取条目
			while (zipEnum.hasMoreElements()) {
				// 得到当前条目
				entry = zipEnum.nextElement();
				entryName = new String(entry.getName());
				// 用/分隔条目名称
				names = entryName.split("\\/");
				length = names.length;
				for (int v = 0; v < length; v++) {
					
					if (RegExp.findCharacters(entryName, regexp)) { // 为Info.plist文件,则输出到文件"" + 
						System.out.println(entryName);
						input = zipFile.getInputStream(entry);
						result = new File(unzipFile.getAbsolutePath() + "/" + new File(entryName).getName());
						output = new FileOutputStream(result);
						byte[] buffer = new byte[1024 * 8];
						int readLen = 0;
						while ((readLen = input.read(buffer, 0, 1024 * 8)) != -1) {
							output.write(buffer, 0, readLen);
						}
						break;
					}
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		} finally {
			if (input != null)
				input.close();
			if (output != null) {
				output.flush();
				output.close();
			}
			// 必须关流，否则文件无法删除
			if (zipFile != null) {
				zipFile.close();
			}
		}
		// 如果有必要删除多余的文件
		
		return result;
	}

	/**
	 * IPA文件的拷贝，把一个IPA文件复制为Zip文件,同时返回Info.plist文件 参数 oldfile 为 IPA文件
	 */
	private File getIpaInfo(File oldfile) throws IOException {
		try {
			int byteread = 0;
			String filename = oldfile.getAbsolutePath().replaceAll(".ipa", ".zip");
			File newfile = new File(filename);
			if (oldfile.exists()) {
				// 创建一个Zip文件
				InputStream inStream = new FileInputStream(oldfile);
				FileOutputStream fs = new FileOutputStream(newfile);
				byte[] buffer = new byte[1444];
				while ((byteread = inStream.read(buffer)) != -1) {
					fs.write(buffer, 0, byteread);
				}
				if (inStream != null) {
					inStream.close();
				}
				if (fs != null) {
					fs.close();
				}
				// 解析Zip文件
				zipFile = newfile;
				return getZipInfo(".app/Info.plist$");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	private ApkInfo getApkInfoMap(File apk) throws Exception {

		ApkInfo aif = new ApkUtil().getApkInfo(apk.getAbsolutePath());
		return aif;
	}
	
	
	

	/**
	 * 通过IPA文件获取Info信息 这个方法可以重构，原因是指获取了部分重要信息，如果想要获取全部，那么应该返回一个Map
	 * <String,Object> 对于plist文件中的数组信息应该序列化存储在Map中，那么只需要第三发jar提供的NSArray可以做到！
	 */
	private IOSPackageInfo getIpaInfoMap(File ipa) throws Exception {

		IOSPackageInfo iif = new IOSPackageInfo();
		File file = getIpaInfo(ipa);
		// 第三方jar包提供
		NSDictionary rootDict = (NSDictionary) PropertyListParser.parse(file);
		// 应用包名
		NSString parameters = (NSString) rootDict.objectForKey("CFBundleIdentifier");
		iif.setCFBundleIdentifier(parameters.toString());
		// 应用名称
		parameters = (NSString) rootDict.objectForKey("CFBundleName");
		iif.setCFBundleName(parameters.toString());
		// 应用版本
		parameters = (NSString) rootDict.objectForKey("CFBundleVersion");
		iif.setCFBundleVersion(parameters.toString());
		// 应用展示的名称
		parameters = (NSString) rootDict.objectForKey("CFBundleDisplayName");
		iif.setCFBundleDisplayName(parameters.toString());
		
		NSDictionary dic = (NSDictionary) rootDict.objectForKey("CFBundleIcons");
		NSDictionary dicPri = (NSDictionary) dic.get("CFBundlePrimaryIcon");
		NSArray array = (NSArray) dicPri.get("CFBundleIconFiles");
		
		parameters = (NSString)array.getArray()[array.count()-1];
		File icon = getZipInfo(parameters.toString());
		iif.setIcon(icon);
		iif.setAppPackage(ipa);
		// 如果有必要，应该删除解压的结果文件
		file.delete();
		file.getParentFile().delete();
		
		//删除
		if (this.zipFile.exists()) {
			this.zipFile.delete();
		}

		return iif;
	}

	public static void main(String[] args) throws Exception {

		File file = new File("/Users/limengnan/Documents/iospackage/Partner-Swift_ios25.ipa");
		AppPackageInfo api = new AppPackageInfo();
		IOSPackageInfo map = api.getIpaInfoMap(file);
		map.getCFBundleDisplayName();
		map.getCFBundleIdentifier();
		map.getCFBundleName();
		map.getCFBundleVersion();

	}
	
		
	
	
}
