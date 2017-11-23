package monitor;

import java.io.File;

import com.limn.tool.regexp.RegExp;



/**
 * 检查最新补丁
 * @author limn
 *
 */
public class CheckPatch{

	private String patchPath = null;
	private File lastModified = null;
	
	private String lastVersion = null;
	/**
	 * 
	 * @param patchPath  补丁路径
	 */
	CheckPatch(String patchPath){
		this.patchPath = patchPath;
	}

	/**
	 *  获取最新的补丁文件
	 * @return file  补丁文件
	 */
	public File getLastVersionFile(){
		File updatePath = new File(patchPath);
		
		Long currentVersion;
		Long version = (long) 0;
		for (File uFile : updatePath.listFiles()) {
			
			if (uFile.isFile()&&RegExp.findCharacters(uFile.getName(), "^patch_.*zip$")) {
				currentVersion = uFile.lastModified();
//				String versionString = uFile.getName().split("-")[1];
//				currentVersion = Long.parseLong(versionString);
				if(version<currentVersion){
					lastModified = uFile;
					version = currentVersion;
					String[] tmp = RegExp.splitWord(uFile.getName(), "\\.zip");
					lastVersion = tmp[0];
					
				}
			}
		}
		return lastModified;
	}
	
	
	public String getLastVersion(){
		return lastVersion;
	}
	
	public Long getFileCount(){
		File updatePath = new File(patchPath);
		return updatePath.length();
	}
	
	public File updateLastFile(){
		File updatePath = new File(lastModified.getPath());
		return updatePath;
	}


}
