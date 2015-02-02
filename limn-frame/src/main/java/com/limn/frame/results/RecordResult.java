package com.limn.frame.results;

import java.util.HashSet;

public class RecordResult implements DataResults {
	
	HashSet<DataResults> dataResults = new HashSet<DataResults>();
	
	
	public RecordResult(){
		dataResults.add(new XMLData());
	}
	
	public void addRecordData(DataResults DR){
		dataResults.add(DR);
	}

	@Override
	public void init() {
		for(DataResults dr : dataResults){
			dr.init();
		}	
	}

	@Override
	public void addSheet(int index) {
		for(DataResults dr : dataResults){
			dr.addSheet(index);
		}
	}

	@Override
	public void addModule(String moduleName) {
		for(DataResults dr : dataResults){
			dr.addModule(moduleName);
		}
	}

	@Override
	public void addCase(String caseNo) {
		for(DataResults dr : dataResults){
			dr.addCase(caseNo);
		}
	}

	@Override
	public void addStep(String stepName, String result) {
		for(DataResults dr : dataResults){
			dr.addStep(stepName,result);
		}
	}

	@Override
	public void addActualResults(String[] results) {
		for(DataResults dr : dataResults){
			dr.addActualResults(results);
		}
	}

	@Override
	public void addExpectedResults(String[] results) {
		for(DataResults dr : dataResults){
			dr.addExpectedResults(results);
		}
	}

	@Override
	public void addResult(boolean isPass) {
		for(DataResults dr : dataResults){
			dr.addResult(isPass);
		}
	}

	@Override
	public void addBitMap(String bitMapPath) {
		for(DataResults dr : dataResults){
			dr.addBitMap(bitMapPath);
		}
		
	}

	@Override
	public void addRelatedCase(String CaseNo) {
		for(DataResults dr : dataResults){
			dr.addRelatedCase(CaseNo);
		}
		
	}

	@Override
	public void addCustom(String node, String value) {
		for(DataResults dr : dataResults){
			dr.addCustom(node,value);
		}
		
	}

	@Override
	public void addTestCaseCount(String count) {
		for(DataResults dr : dataResults){
			dr.addTestCaseCount(count);
		}
		
	}

	@Override
	public void addCaseReport() {
		for(DataResults dr : dataResults){
			dr.addCaseReport();
		}
		
	}

	@Override
	public void addCaseLog(String step, int result) {
		// TODO Auto-generated method stub
		
	}
	
}
