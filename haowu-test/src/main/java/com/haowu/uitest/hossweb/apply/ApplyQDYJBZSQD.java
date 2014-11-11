package com.haowu.uitest.hossweb.apply;

import java.util.LinkedHashMap;

public class ApplyQDYJBZSQD {
	
	private LinkedHashMap<String,String> data = new LinkedHashMap<String, String>();

	
	public void setSociology(String[] pers){
		int num = 0;
		for(String per:pers){
			data.put("projectTypes["+num+"].items[0].scales[0].scale", per);
			num++;
		}
		
	}
	
	public void setPartner(String[] pers){
		int num = 0;
		for(String per:pers){
			data.put("projectTypes["+num+"].items[1].scales[0].scale", per);
			num++;
		}
	}
	
	public void setIntermediaryBase(String[] Range,String[] pers){
		int num = 0;
		for(String per:pers){
			data.put("projectTypes["+num+"].items[2].scales[0].bigThreshold", Range[num]);
			data.put("projectTypes["+num+"].items[2].scales[0].scale", per);
			num++;
		}
	}
	
	public void setIntermediaryOther1(String[] Range,String[] pers){
		int num = 0;
		for(String per:pers){
//			data.put("projectTypes["+num+"].items[2].scales[1].smallThreshold", Range[num][0]);
			data.put("projectTypes["+num+"].items[2].scales[1].bigThreshold", Range[num]);
			data.put("projectTypes["+num+"].items[2].scales[1].scale", per);
			num++;
		}
	}
	
	public void setIntermediaryOther2(String[] pers){
		int num = 0;
		for(String per:pers){
//			data.put("projectTypes["+num+"].items[2].scales[2].smallThreshold", Range[num]);
			data.put("projectTypes["+num+"].items[2].scales[2].scale", per);
			num++;
		}
	}
	
	public LinkedHashMap<String,String> getData(){
		return data;
	}
	
}
