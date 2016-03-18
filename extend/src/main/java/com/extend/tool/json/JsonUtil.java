package com.extend.tool.json;

import com.automation.tool.util.StringUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by chris.li on 2016/1/18.
 */
public class JsonUtil {
    public static Object getObjectFromJson(String json, Object obj, Map<String, Class> myClasses, List<Object[]> myListClasses){
        try {
            if(StringUtil.isEmpty(json)) {
                //请求结果为空，或者预期结果为空
                throw new Exception("json为空");
            }
            Gson gson = new Gson();
            Object wsObject = null;
            if(json.startsWith("{")) {
                //Map
                wsObject = gson.fromJson(json, Map.class);
            }else if(json.startsWith("[")) {
                //List
                wsObject = gson.fromJson(json, List.class);
            }else {
                //请求结果和预期结果 格式不一致
                throw new Exception("请求结果和预期结果 格式不一致。请求结果："+json);
            }

            return setFieldsValue(wsObject, obj, myClasses, myListClasses);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static Object setFieldsValue(Object wsObject, Object obj, Map<String, Class> myClasses, List<Object[]> myListClasses) throws Exception{
        //获取result结果集
        if(wsObject instanceof Map && !(obj instanceof List)) {
            Map wsMap = (Map)wsObject;
            Class objClass = obj.getClass();
            Field[] fs = objClass.getDeclaredFields();
            for(Field f:fs){
                String name = f.getName(); // 获取属性的名字
                String setMethod = "set" + name.substring(0, 1).toUpperCase() + name.substring(1);
                String getMethod = "get" + name.substring(0, 1).toUpperCase() + name.substring(1);
                String type = f.getGenericType().toString(); // 获取属性的类型
                if (type.equals("class java.lang.String")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = objClass.getMethod(setMethod, String.class);
                    m.invoke(obj, wsMap.get(name));
                }else if (type.equals("class java.lang.Integer")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = objClass.getMethod(setMethod, Integer.class);
                    Integer wsInteger = StringUtil.strToInt(StringUtil.objectToString(wsMap.get(name)));
                    m.invoke(obj, wsInteger);
                }else if (type.equals("class java.lang.Boolean")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = objClass.getMethod(setMethod, Boolean.class);
                    m.invoke(obj, wsMap.get(name));
                }else if (type.equals("class java.lang.Date")) { // 如果type是类类型，则前面包含"class "，后面跟类名
                    Method m = objClass.getMethod(setMethod, Date.class);
                    m.invoke(obj, wsMap.get(name));
                }else if(myClasses.containsKey(type)) {
                    Class childObjClass = myClasses.get(type);
                    Object childObj = childObjClass.newInstance();
                    Object childWsObject = ((Map) wsObject).get(name);
                    Object wsMapValue = setFieldsValue(childWsObject, childObj, myClasses, myListClasses);
                    Method m = objClass.getMethod(setMethod, childObjClass);
                    m.invoke(obj, wsMapValue);
                }else if(f.getType().isAssignableFrom(List.class)) {
                    Method m1 = objClass.getMethod(getMethod);
                    Object childObj = new ArrayList<>();
                    Object childWsObject = ((Map) wsObject).get(name);
                    Object wsMapValue = setFieldsValue(childWsObject, childObj, myClasses, myListClasses);
                    Method m = objClass.getMethod(setMethod, m1.getReturnType());
                    m.invoke(obj, (List)wsMapValue);
                }else {
                    f.setAccessible(true); //设置些属性是可以访问的
                    Integer wsInteger = StringUtil.strToInt(StringUtil.objectToString(wsMap.get(name)));
                    f.set(obj, wsInteger);
                }

            }
        }else if(wsObject instanceof List && obj instanceof List) {
            List wsList = (List)wsObject;
            List objList = (List) obj;
            Class objListClass = (Class)myListClasses.get(0)[0];
            myListClasses.remove(0);
            for(int i=0; i<wsList.size(); i++) {
                Object wsListObj = wsList.get(i);
                Object childObjList = objListClass.newInstance();
                Object wsObjValue = setFieldsValue(wsListObj, childObjList, myClasses, myListClasses);
                objList.add(wsObjValue);
            }
        }else {
            obj = wsObject;
        }
        return obj;
    }

    public static String generateJsonStrForResult(ResultJson resultJson) {
        /*URL:http://apolloae-beta.envisioncn.com/apollohds/ws/rest/timeSeqKpisV2;
        Result:["<value><time>",{"value":["data[0].pr[0].value", "100", "2", "0.00#"], "time":["data[0].pr[0].time"]}];
        Param:{"mdmIds":["d41c9775703e4631bbff2fcb4899637e"],
            "objType":"SITE",
                    "returnType":"SITE",
                    "beginTime":"2015-01-01 00:00:00",
                    "endTime":"2015-12-31 23:59:59",
                    "kpis":["pr"],
            "timeGroup":"Y"
        }*/

        //生成一个类似的json
        Gson gson = new GsonBuilder().disableHtmlEscaping().create();
        return gson.toJson(resultJson.getResultList());
    }

    public static String generateJsonStrForWS(String url, ResultJson resultJson, String param) {
        /*URL:http://apolloae-beta.envisioncn.com/apollohds/ws/rest/timeSeqKpisV2;
        Result:["<value><time>",{"value":["data[0].pr[0].value", "100", "2", "0.00#"], "time":["data[0].pr[0].time"]}];
        Param:{"mdmIds":["d41c9775703e4631bbff2fcb4899637e"],
            "objType":"SITE",
                    "returnType":"SITE",
                    "beginTime":"2015-01-01 00:00:00",
                    "endTime":"2015-12-31 23:59:59",
                    "kpis":["pr"],
            "timeGroup":"Y"
        }*/

        String expectStr = "URL:"+url
                + ";\nResult:" + JsonUtil.generateJsonStrForResult(resultJson)
                + ";\nParam:" + param;
        return expectStr;
    }

    public static void main(String[] args) {
//        List<UserBean> bean = new ArrayList<UserBean>();
//        Map<String, Class> myClasses = new HashMap<>();
//        myClasses.put("class com.apollo.keyword.UserBean", UserBean.class);
//        List<Object[]> myListClasses = new ArrayList<Object[]>();
//        myListClasses.add(new Object[]{UserBean.class, false});
//        myListClasses.add(new Object[]{String.class, false});
//        myListClasses.add(new Object[]{UserBean.class, false});
//        JsonUtil.getObjectFromJson("[{\"id\":30000,\"name\":\"unlogin!\" ,\"age\":2, \"hehe\":[\"aaa\",\"bbb\"], \"userBean\":{\"name\":\"haha222\",\"age\":52}},{\"id\":20000,\"name\":\"unlogin!\" ,\"age\":3, \"userBeans\":[{\"name\":\"haha\",\"age\":5}]}]", bean, myClasses, myListClasses);
//        System.out.print("hehe");

        ResultJson resultJson = new ResultJson();
        resultJson.addResult("<value><time>");
        resultJson.addNumberResultMap("value", "data[0].pr[0].value", "100", "2", "0.00#");
        resultJson.addStringResultMap("time", "data[0].pr[0].time");
        String result = JsonUtil.generateJsonStrForResult(resultJson);
        System.out.println(result);
        String url = "http://apolloae-beta.envisioncn.com/apollohds/ws/rest/timeSeqKpisV2";
        String param = "{\"mdmIds\":[\"d41c9775703e4631bbff2fcb4899637e\"],\n" +
                " \"objType\":\"SITE\",\n" +
                " \"returnType\":\"SITE\",\n" +
                " \"beginTime\":\"2015-01-01 00:00:00\",\n" +
                " \"endTime\":\"2015-12-31 23:59:59\",\n" +
                " \"kpis\":[\"pr\"],\n" +
                " \"timeGroup\":\"Y\"\n" +
                "}";
        String wsResult = JsonUtil.generateJsonStrForWS(url, resultJson, param);
        System.out.println(wsResult);
    }

}

class UserBean {

    private Integer id;

    private int age;

    private UserBean userBean;

    private String name;

    private String address;

    private List<String> hehe;

    private List<UserBean> userBeans;

    public UserBean(){
        System.out.println("实例化");
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public List<String> getHehe() {
        return hehe;
    }

    public void setHehe(List<String> hehe) {
        this.hehe = hehe;
    }

    public List<UserBean> getUserBeans() {
        return userBeans;
    }

    public void setUserBeans(List<UserBean> userBeans) {
        this.userBeans = userBeans;
    }

    public UserBean getUserBean() {
        return userBean;
    }

    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

}
