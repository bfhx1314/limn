package common;

import java.util.Random;

public class Common {

	public static int random(int min, int max) {
		Random random = new Random();

		return random.nextInt(max) % (max - min + 1) + min;

	}
	
	public static void sleepRandom(int min, int max){
		int wait = random(min,max);
		try {
			Thread.sleep(wait);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public static void randomNotRepeat(int min, int max) {

		int num = 0;
		boolean[]  bool = new boolean[max];
		do {

			num = random(min, max);

		} while (bool[num]);

	}
	
	public static void ran(){
		String[] name = {"ÕÔ","Ç®","Ëï","Àî","ÖÜ","Îâ","Ö£","Íõ","·ë","³Â","ñÒ","ÎÀ","½¯","Éò","º«","Ñî","Öì"};
	}

}
