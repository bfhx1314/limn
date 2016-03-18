package com.limn.tool.common;


import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;

import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNInfo;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNStatusType;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

public class ExportForSVN{

	//URL
	public SVNURL URL = null;
	public String branchURL = null;
	public String userName = null;
	public String passWord = null;
	public String exportPath = null;
	public SVNRevision startingRevision;
	public SVNRevision endingRevision;
	private ISVNAuthenticationManager authManager;
	public SVNDiffClient diffClient;
	public static HashSet<SVNDiffStatus> changes =  new HashSet<SVNDiffStatus>();
	
	//����������¼ÿ�ε��� ���svn�汾�� �´ε��� �������Ϊ���
	private String versionFile = "revision.txt";
	
	public void execute(){
		System.out.println("SVN��ַ: " + branchURL);
		initSVN();
	}

	public void setExportPath(String exportPath){
		this.exportPath = exportPath + "/";
	}
	
	public void setBranchURL(String branchURL){
		this.branchURL = branchURL;
	}
	
	public void setUsername(String userName){
		this.userName = userName;
	}
	
	public void setPassWord(String passWord){
		this.passWord = passWord;
	}
	
	public void initSVN(){
		
		versionFile = exportPath + "/" + versionFile;
		
		DAVRepositoryFactory.setup();
		try {
			URL = SVNURL.parseURIEncoded(branchURL);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		this.startingRevision = getCurrentRevision();
		this.endingRevision = getLastRevision();
		this.authManager = SVNWCUtil.createDefaultAuthenticationManager(this.userName, this.passWord);
		System.out.println("��ǰSVN�汾��: " + startingRevision);
		System.out.println("����SVN�汾��: " + endingRevision);
		try {
			start();
		} catch (SVNException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	private void start() throws SVNException{
		ImplISVNDiffStatusHandler handler = new ImplISVNDiffStatusHandler();
		diffClient = new SVNDiffClient(authManager, null);

		diffClient.doDiffStatus(this.URL, this.startingRevision,this.URL, this.endingRevision,  SVNDepth.INFINITY, false, handler );

		SVNUpdateClient updateClient = new SVNUpdateClient(authManager,	SVNWCUtil.createDefaultOptions(true));

		Iterator<SVNDiffStatus> it = changes.iterator();
		while(it.hasNext()){
			SVNDiffStatus change = it.next();
			System.out.println(exportPath+ change.getPath());
			File destination = new File( exportPath + change.getPath());
			updateClient.doExport(change.getURL(), destination,	this.endingRevision, this.endingRevision, null, true,SVNDepth.INFINITY);
		}
		
		setFileText(versionFile,endingRevision.toString());
		
	}
	
	/**
	 * ��ȡ���°汾��
	 * @return SVNRevision
	 */
	private SVNRevision getLastRevision(){
		SVNWCClient wcClient = new SVNWCClient(authManager,null);
		SVNInfo info = null;
		try {
			info = wcClient.doInfo(URL, SVNRevision.HEAD, SVNRevision.HEAD);
		} catch (SVNException e) {
			e.printStackTrace();
		}
		return info.getCommittedRevision();
	}
	
	private SVNRevision getCurrentRevision(){
		String revision = null;

		try {
			revision = getFileText(versionFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return SVNRevision.create(Long.parseLong(revision));
		
	}

	/**
	 * �����ļ�����
	 * 
	 * @param �ļ�·��
	 * @return �ļ�����
	 * @throws IOException
	 */
	private String getFileText(String path) throws IOException {

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
	 * д���ļ�����
	 * @param path �ļ�·��
	 * @param text ����
	 */
	private void setFileText(String path, String text){
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

}

class ImplISVNDiffStatusHandler implements ISVNDiffStatusHandler {

	public void handleDiffStatus(SVNDiffStatus status) throws SVNException {
		if (status.getModificationType() == SVNStatusType.STATUS_ADDED || status.getModificationType() == SVNStatusType.STATUS_MODIFIED){
			ExportForSVN.changes.add(status);
		}

	}

}
