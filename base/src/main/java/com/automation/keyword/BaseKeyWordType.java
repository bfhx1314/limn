package com.automation.keyword;

/**
 * Created by snow.zhang on 2015-11-02.
 */
public class BaseKeyWordType {

    /**
     * 说明: 启动浏览器 <br>
     * 参数: url<br>
     * 实例: 启动浏览器 http://www.baidu.com<br>
     */
    public final static String START_BROWSER = "启动浏览器";

    /**
     * 说明: 关闭浏览器 <br>
     * 实例: 关闭浏览器 <br>
     */
    public final static String CLOSE_BROWSER = "关闭浏览器";

    /**
     * 说明: 录入文字 <br>
     * 参数: 内容 <br>
     * xpath： <br>
     * 实例: 录入 test <br>
     */
    public final static String INPUT = "录入";

    /**
     * 说明：录入密码
     */
    public final static String INPUT_PASSWORD = "录入密码";

    /**
     * 说明: 点击元素 <br>
     * xpath:  <br>
     * 实例: 点击<br>
     */
    public final static String CLICK = "点击";

    /**
     * 说明: 验证控件的内容，完全匹配 <br>
     * 参数: 属性名称（可选）<br>
     * xpath： <br>
     * 预期结果：预期值 <br>
     * 实例: 验证控件 当日发电量<br>
     */
    public final static String VERIFY = "验证控件";

    /**
     * 说明: 模糊验证控件 <br>
     * 参数: 属性名称（可选）<br>
     * xpath： <br>
     * 预期结果：预期值（正则表达式）<br>
     * 实例: 模糊验证控件 （正则表达式）<br>
     */
    public final static String FUZZY_MATCHING = "模糊验证控件";


    /**
     * 说明: 模糊验证表格 <br>
     * 参数: 第n列、第m~n列<br>
     * xpath： <br>
     * 预期结果：预期值（正则表达式）<br>
     * 实例: 模糊验证表格 \\d+<br>
     */
    public final static String FUZZY_MATCHING_TABLE = "模糊验证表格";

    /**
     * 说明: 选择下拉框 <br>
     * 参数: 下拉框选项值<br>
     * xpath： <br>
     * 实例: 选择下拉框 月<br>
     */
    public final static String SELECT_OPTION = "选择下拉框";

    /**
     * 说明: 等待<br>
     * 参数: 等待时间<br>
     * 实例: 10秒  10000<br>
     */
    public final static String PAUSE_TIME = "等待";

    /**
     * 说明: 同步控件的内容 <br>
     * 参数: 内容<br>
     * 实例: 同步控件 当日发电量<br>
     */
    public final static String SYNC = "同步控件";

    /**
     * 说明: 进入iframe<br>
     * 参数1: 第i个iframe<br>
     * 实例: 进入iframe:2 （从1开始，1代表第一个iframe） <br>
     *
     */
    public final static String ENTER_IFRAME = "进入iframe";

    /**
     * 说明: 退出iframe到初始<br>
     */
    public final static String EXIT_IFRAME = "退出iframe";

    /**
     * 说明: 切换页面 <br>
     * 参数1: 第n个标签页<br>
     * 实例: 切换页面 2 （从1开始，1代表第一个iframe）<br>
     */
    public final static String CHANGE_BRO_TAB = "切换页面";

    /**
     * 说明: 关闭页面 <br>
     * 参数1: 第n个标签页<br>
     * 实例: 关闭页面 2 （从1开始，1代表第一个iframe）<br>
     */
    public final static String CLOSE_BRO_TAB = "关闭页面";

    /**
     * 说明: 鼠标MouseOver事件（大小写不敏感） <br>
     * xpath： <br>
     * 实例: MouseOver <br>
     */
    public final static String MOUSE_OVER = "MouseOver";

    /**
     * 说明：访问URL <br>
     * 参数：URL <br>
     * 实例：访问URL http://www.baidu.com <br>
     */
    public final static String ACCESS_URL = "访问URL";

    /**
     * 说明：等待指定的控件消失。
     */
    public final static String WAIT_WEBELEMENT_DISAPPEAR = "等待控件消失";

    /**
     * 说明：等待指定的控件出现
     */
    public final static String WAIT_WEBELEMENT_APPEAR = "等待控件出现";

    /**
     * 说明：等待指定的文字出现
     */
    public final static String WAIT_TEXT_APPEAR = "等待文字出现";

    /**
     * 说明：字符串加密成BASE64
     */
    public final static String ENCRYPT_BASE64 = "加密BASE64";

    /**
     * 说明：BASE64解密成字符串
     */
    public final static String DECRYPT_BASE64 = "解密BASE64";

    /**
     * 说明：按下 Enter/回车 键
     */
    public final static String ENTER = "回车";

    /**
     * 说明: 验证弹出框的内容，完全匹配 <br>
     * 预期结果：预期值 <br>
     * 实例: 验证控件 当日发电量<br>
     */
    public final static String VERIFY_ALERT = "验证弹出框";

    /**
     * 说明: 关闭弹出框 <br>
     */
    public final static String CLOSE_ALERT = "关闭弹出框";

    /**
     * 说明: 获取控件的值，存入变量中<br>
     * 参数1: xpath<br>
     * 参数2: 变量名<br>
     */
    public final static String GET_WEBELEMENT_VALUE_TO_VAR = "获取";
}
