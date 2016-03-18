package com.automation.tool.util;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by ${chris.li} on ${2015-10-30}.
 */
public class StringUtil {

    public static final String formatStr = "0.00##########"; //小数点后保留两位

    /**
     * 格式化数字  number*multiplyNum后取小数点后scale位，并以bigDecimalRoud方式进位，最后以format格式化
     * @param number  例：1.25
     * @param multiplyNum 例：1000（可以为空）
     * @param scale 例：2（可以为空）
     * @param roundingMode 例：RoundingMode.HALF_UP（scale必须存在;可以为空）
     * @param format 例：0.##（可以为空）
     * @return String
     */
    public static String strNumberFormat(String number,String multiplyNum, Integer scale, RoundingMode roundingMode, String format){
        String returnNum = number;
        if(StringUtil.isEmpty(number)) {
            return returnNum;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(number.trim());
            if (!StringUtil.isEmpty(multiplyNum)) {
                bigDecimal = bigDecimal.multiply(new BigDecimal(multiplyNum));
            }
            if (scale!=null && roundingMode!=null) {
                bigDecimal = bigDecimal.setScale(scale, roundingMode);
            }else if(scale!=null) {
                bigDecimal = bigDecimal.setScale(scale);
            }
            returnNum = bigDecimal.toString();
            if (!StringUtil.isEmpty(format)) {
                DecimalFormat d = new DecimalFormat(format);
                returnNum = d.format(bigDecimal);
            }
            return returnNum;
        }catch (Exception e) {
            return returnNum;
        }
    }

    /**
     * 判断字符串是否为空
     * @param str 字符串
     * @return true||false
     */
    public static boolean isEmpty(String str) {
        if (str==null || str.isEmpty() || str.trim().isEmpty()){
            return true;
        }else {
            return  false;
        }
    }

    /**
     * 判断是否为数字
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        if(StringUtil.isEmpty(str)) {
            return false;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(str.trim());
            if(bigDecimal!=null) {
                return true;
            }
            return false;
        }catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断num1是否大于num2
     * @param num1 字符串1
     * @param num2 字符串2
     * @return true||false
     */
    public static boolean biggerThan(String num1, String num2) {
        try {
            String regexStr = "^(\\d{1,3}(,\\d\\d\\d)*(\\.\\d+)?)$";
            if(!StringUtil.isEmpty(num1) && RegExp.matchRegExp(num1, regexStr) && num1.contains(",")) {
                num1 = num1.replace(",", "");
            }
            if(!StringUtil.isEmpty(num2) && RegExp.matchRegExp(num2, regexStr) && num2.contains(",")) {
                num1 = num1.replace(",", "");
            }
            if(new BigDecimal(num1).compareTo(new BigDecimal(num2))>0) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 判断num1是否大于num2
     * @param num1 字符串1
     * @param num2 字符串2
     * @return true||false
     */
    public static boolean equalValue(String num1, String num2) {
        try {
            String regexStr = "^(\\d{1,3}(,\\d\\d\\d)*(\\.\\d+)?)$";
            if(!StringUtil.isEmpty(num1) && RegExp.matchRegExp(num1, regexStr) && num1.contains(",")) {
                num1 = num1.replace(",", "");
            }
            if(!StringUtil.isEmpty(num2) && RegExp.matchRegExp(num2, regexStr) && num2.contains(",")) {
                num1 = num1.replace(",", "");
            }

            if(new BigDecimal(num1).compareTo(new BigDecimal(num2))==0) {
                return true;
            }else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 格式化数字(保留有效位)  number*multiplyNum后取小数点后scale位，并以roundingMode方式进位，最后以format格式化
     * 特别说明：如果number*multiplyNum后结果为0.02356，那么取值进2位四舍五入后值为0.024
     * @param number  例：1.25
     * @param multiplyNum 例：1000（可以为空）
     * @param scale 例：2（可以为空）
     * @param roundingMode 例：RoundingMode.HALF_UP（scale必须存在;可以为空）
     * @param format 例：0.##（可以为空）
     * @return String
     */
    public static String strNumberValidFormat(String number,String multiplyNum, Integer scale, RoundingMode roundingMode, String format) {
        String returnNum = number;
        if(StringUtil.isEmpty(number)) {
            return returnNum;
        }
        try {
            BigDecimal bigDecimal = new BigDecimal(number.trim());
            if (!StringUtil.isEmpty(multiplyNum)) {
                bigDecimal = bigDecimal.multiply(new BigDecimal(multiplyNum));
            }
            if(scale!=null) {
                if(bigDecimal.compareTo(BigDecimal.ONE)<0 && bigDecimal.compareTo(BigDecimal.ZERO)>0) {
                    bigDecimal = StringUtil.toFormatValidNum(bigDecimal, scale, roundingMode);
                }else {
                    if (roundingMode!=null) {
                        bigDecimal = bigDecimal.setScale(scale, roundingMode);
                    }else{
                        bigDecimal = bigDecimal.setScale(scale);
                    }
                }
            }
            returnNum = bigDecimal.toString();
            if (!StringUtil.isEmpty(format)) {
                DecimalFormat d = new DecimalFormat(format);
                returnNum = d.format(bigDecimal);
            }
            return returnNum;
        }catch (Exception e) {
            return returnNum;
        }
    }

    /**
     * 返回对象的有效数字
     * @param obj BigDecimal
     * @param length 长度
     * @return BigDecimal
     */
    public static BigDecimal toFormatValidNum(BigDecimal obj, int length, RoundingMode roundingMode) {
        try {
            if(obj!=null && length>0) {
                BigDecimal divisor = BigDecimal.ONE;
                MathContext mc = new MathContext(length);
                if(roundingMode!=null) {
                    mc = new MathContext(length, roundingMode);
                }
                return obj.divide(divisor, mc);
            }
            return new BigDecimal("0");
        }catch (Exception e) {
            return new BigDecimal("0");
        }
    }

    /**
     * 两数相除
     * @param num1 数字1
     * @param num2 数字2
     * @param scale scale of the {@code BigDecimal} quotient to be returned.
     * @param roundingMode rounding mode to apply.
     * @return BigDecimal
     */
    public static BigDecimal divide(String num1, String num2, int scale, RoundingMode roundingMode) {
        try {
            BigDecimal b1 = new BigDecimal(num1);
            BigDecimal b2 = new BigDecimal(num2);
            return  b1.divide(b2, scale, roundingMode);
        } catch (Exception e) {
            return  new BigDecimal("0");
        }
    }

    /**
     * 两数相除减
     * @param num1 数字1
     * @param num2 数字2
     * @return BigDecimal
     */
    public static BigDecimal subtract(String num1, String num2) {
        try {
            if(StringUtil.isEmpty(num1) || StringUtil.isEmpty(num2)) {
                return  new BigDecimal("0");
            }
            BigDecimal b1 = new BigDecimal(num1);
            BigDecimal b2 = new BigDecimal(num2);
            return  b1.subtract(b2);
        } catch (Exception e) {
            return  new BigDecimal("0");
        }
    }

    public static String trim(String str) {
        if(str==null) {
            return null;
        }else {
            return str.trim();
        }
    }

    public static boolean matchValues(String expectedValue, String actValue) {
        if((expectedValue==null && actValue==null)
                || ("".equals(StringUtil.trim(expectedValue)) && "".equals(StringUtil.trim(actValue)))
                || (!StringUtil.isEmpty(expectedValue) && !StringUtil.isEmpty(actValue)
                    && StringUtil.trim(expectedValue).equals(StringUtil.trim(actValue))) ) {
            //获取页面的数据和数据库比较,相同则成功
            return true;
        }
        return false;
    }

    public static Integer strToInt(String str) {
        try {
            return new Integer(str);
        } catch (Exception e) {
            return null;
        }
    }

    public static String objectToString(Object obj) {
        try {
            if(obj==null) {
                return  null;
            }
            if(obj instanceof Double) {
                return new BigDecimal((Double)obj).toString();
            }else if(obj instanceof Integer) {
                return new BigDecimal((Integer)obj).toString();
            }else if(obj instanceof Float) {
                return new BigDecimal((Float)obj).toString();
            }else {
                return obj.toString();
            }
        } catch (Exception e) {
            return obj.toString();
        }
    }

    public static String trimAllSpace(String str) {
        if(str==null) {
            return str;
        }else {
            return str.replace(" ", "").replace("\n", "").replace("\r", "");
        }
    }

    public static String addZeroToIntStr(Integer num, int zeroNum){
        if (num == null){
            return null;
        }else if(zeroNum<=0){
            return num+"";
        }else {
            String numStr = num + "";
            int numLength = numStr.length();
            if (zeroNum > numLength){
                for(int i=0; i<(zeroNum-numLength); i++){
                    numStr = "0" + numStr;
                }
            }
            return numStr;
        }
    }

    public static  void  main(String[] args) {
//        System.out.println(true&&true);
//        System.out.println("dd".replace("123", "456"));
//        Object obj = 123456789.12d;
//        System.out.println((String)obj);

        String result = "{...}data[0].pr[0].value{%}";
        System.out.println(result.replaceFirst("^\\{(.*)\\}$", ""));
        System.out.println(RegExp.matchRegExp(result, "^\\{(.*)\\}$"));
    }
}
