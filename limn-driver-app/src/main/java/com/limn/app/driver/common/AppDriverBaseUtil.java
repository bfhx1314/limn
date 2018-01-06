package com.limn.app.driver.common;

import com.limn.app.driver.bean.SlidingPathBean;
import com.limn.tool.random.RandomData;

import java.util.ArrayList;

public class AppDriverBaseUtil {

    public static ArrayList<SlidingPathBean> getRandonSlidingPaths(SlidingPathBean original , int random){
        ArrayList<SlidingPathBean> slidingPath = new ArrayList<SlidingPathBean>();
        int startX = original.getStartX();
        int startY = original.getStartY();
        int endX = original.getEndX();
        int endY = original.getEndY();

        for(int i =0 ; i < random ; i++){

            int listEndX = RandomData.getNumberRange(startX,endX);
            int listEndY = RandomData.getNumberRange(startY,endY);
            SlidingPathBean spb = new SlidingPathBean();
            spb.setStartX(startX);
            spb.setStartY(startY);

            if(i == random - 1) {
                //最后一组将endx endy 覆盖
                spb.setEndX(endX);
                spb.setEndY(endY);
            }else{
                spb.setEndX(listEndX);
                spb.setEndY(listEndY);
            }
            slidingPath.add(spb);

            //滑动后的修改起始位置
            startX = listEndX;
            startY = listEndY;
        }

        return slidingPath;
    }

    public static void main(String[] args){
        SlidingPathBean spb = new SlidingPathBean();
        spb.setStartX(50);
        spb.setStartY(1000);
        spb.setEndX(50);
        spb.setEndY(100);

        ArrayList<SlidingPathBean> a = getRandonSlidingPaths(spb,5);
        System.out.println(a);
    }
}
