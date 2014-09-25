package com.limn.control;

import java.io.File;
import java.util.HashSet;
import java.util.Iterator;

import javax.swing.JComboBox;

import com.limn.tool.common.FileUtil;



public class FileComboBox extends JComboBox<Object> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private HashSet<String> set = new HashSet<String>();
	
	public FileComboBox(String path,String type){
		if(new File(path).exists()){
			set = FileUtil.findFilesByType(path, type);
			this.setEditable(true);
			init();
		}
	}
	
	public FileComboBox(String path,boolean isDict){
		if(new File(path).exists()){
			set = FileUtil.findFiles(path, isDict);
			this.setEditable(true);
			init();
		}
	}
	
	private void init(){
		Iterator<String> it = set.iterator();
		while(it.hasNext()){
			this.addItem(it.next());
		}
	}

}
