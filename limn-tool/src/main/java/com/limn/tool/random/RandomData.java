package com.limn.tool.random;


import java.util.Random;


/**
 * 随机的方法
 * @author limn
 *
 */
public class RandomData {
	
	
	public static void main(String[] args){
		while(true){
			System.out.println(RandomData.getString(8));
		}
	}
	
	
	/**
	 * 范围数据随机
	 * @param min
	 * @param max
	 * @return
	 */
	public static int getNumberRange(int min, int max){
		if(min == max){
			return min;
		}else if (min > max){
			int r = max;
			max = min;
			min = r;
		}

		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}
	
	/**
	 * 16进制的数值随机获取
	 * @return
	 */
	public static String getValueBy0x(){
		int max = 16;
		int min = 0;
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;

		String re = "";

		switch (s) {
		case 10:
			re = "a";
			break;
		case 11:
			re = "b";
			break;
		case 12:
			re = "c";
			break;
		case 13:
			re = "d";
			break;
		case 14:
			re = "e";
			break;
		case 15:
			re = "f";
			break;
		default:
			re = String.valueOf(s);
			break;

		}
		return re;
	}

	
	/**
	 * 随机生成MAC地址
	 * @return
	 */
	public static String getMAC() {

		return getValueBy0x() + getValueBy0x() + ":" + getValueBy0x() + getValueBy0x() + ":"
				+ getValueBy0x() + getValueBy0x() + ":" + getValueBy0x() + getValueBy0x() + ":"
				+ getValueBy0x() + getValueBy0x() + ":" + getValueBy0x() + getValueBy0x();
	}
	
	/**
	 * 随机生成一个字符串 字母大小写+数字
	 * @param length 字符串长度
	 * @return
	 */
	public static String getString(int length){
		Random randGen = new Random();
		char[] data = ("0123456789abcdefghijklmnopqrstuvwxyz"
				+ "ABCDEFGHIJKLMNOPQRSTUVWXYZ").toCharArray(); 
        char [] randBuffer = new char[length];
        for (int i=0; i<randBuffer.length; i++) {
            randBuffer[i] = data[randGen.nextInt(61)];
        }
        return new String(randBuffer);
	}
	
	
	
}

