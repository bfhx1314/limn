package share;

import java.text.SimpleDateFormat;
import java.util.Date;

import common.MyDate;

public class Engine{
	
	private String[][] data = null;
	private ShareData sd = null;
	private int shareMoney = 0;
	
	private int shareInterval = 2;
	
	public Engine(){
	
		sd = new ShareData("");
		data = sd.getData();
		Date temp = null;
		DataCache.flag = MyDate.getDate();
		for(String[] rowData : data){
			if(!rowData[1].isEmpty()){
				if(rowData[4]==null || rowData[4].isEmpty()){
					rowData[4] = "2010-01-01 00:00:00";
				}
				temp = MyDate.getData("yyyy-MM-dd HH:mm:ss",rowData[4]);
				DataCache.All.put(rowData[1],temp);
				DataCache.ShareConut.put(rowData[1], 0);
				if((MyDate.getDate().getTime()-temp.getTime())/1000>=10800){
					DataCache.Ready.put(rowData[1],Integer.parseInt(rowData[3]));
				}
				
			}
		}
		
	}
	
	/**
	 *  ���з���
	 */
	public void run() {
		for(String username:DataCache.Ready.keySet()){
			if(DataCache.Ready.get(username) <= shareMoney){
				cheack(username,share(username));
			}
		}
			
		
	}
	
	
	private void cheack(String username, String Resutls){
		DataCache.updateReady(username, -1);
	}
	
	
	
	private String share(String username){
		
		
		return "";
		
	}
	

	
	
	
//	public void run(){
//		String username = null;
//		String password = null;
//		int wantMoney = 0;
//		Date shareTime = null;
//		for(String[] rowData : data){
//			if(!rowData[0].isEmpty())
//			username = rowData[0];
//			password = rowData[1];
//			wantMoney = Integer.parseInt(rowData[2]);
////			shareTime = rowData[3];
//			
//		}
//
//		
//	}
	
	private int coolDownTime(int wantMoney){
		
		String[] shareDate = sd.getShareData();
		
		int minCoolDown = 9999;
		
		for(String date:shareDate){
			int nowCoolDown = hasCoolDown(date);
			minCoolDown = nowCoolDown<minCoolDown?nowCoolDown:minCoolDown;
		}
		
		return minCoolDown;
		
	}
	
	
	private int hasCoolDown(String time){
		Date pDate = MyDate.getData("yyyy-MM-dd HH:mm:ss", time);
		Date now = MyDate.getData("yyyy-MM-dd HH:mm:ss", MyDate.getDateToString());
		return (int) ((now.getTime() - pDate.getTime())*1000);

	}











	

}
