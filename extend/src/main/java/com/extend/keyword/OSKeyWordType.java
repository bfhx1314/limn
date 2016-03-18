package com.extend.keyword;

/**
 * Created by snow.zhang on 2015/11/19.
 */
public class OSKeyWordType {

    /**
     * 说明：验证canvas控件，window.charData中option.series中data数组中有一个值大于0
     */
    public final static String CANVAS_SINGLE_GREATETHEN_ZERO = "canvas单值大于零";

    /**
     * 说明：通过WS POST方式 验证控件
     */
    public final static String WS_POST_VERIFY = "WSPost验证";

    /**
     * 说明：通过WS POST方式 验证柱状图
     */
    public final static String WS_POST_COLUMN_VERIFY = "WSPost柱状图验证";

    /**
     * 说明：通过SQL 验证 表格
     */
    public final static String SQL_VERIFY_TABLE = "SQL表格验证";

    /**
     * 说明：通过SQL 验证 CharData
     */
    public final static String SQL_VERIFY_CHAR_DATA = "SQL验证CharData";

    /**
     * 说明：通过SQL方式 验证柱状图
     */
    public final static String SQL_COLUMN_VERIFY = "SQL柱状图验证";

    /**
     * 说明：通过SQL方式 验证点状图
     */
    public final static String SQL_POINTS_VERIFY = "SQL点状图验证";

    /**
     * 说明：通过SQL方式 验证点状图
     */
    public final static String SQL_POINTS_FUZZY_MATCHING = "SQL点状图模糊验证";

    /**
     * 说明: JS操作 <br>
     * xpath:  <br>
     * 实例: 点击<br>
     */
    public final static String JS_OPERATE = "JS操作";

    /**
     * 说明: 验证控件的内容是否为当前时间<br>
     * 预期值: 日;0;yyyy-MM-dd<br>
     * xpath： <br>
     * 实例: 验证控件 当日发电量<br>
     */
    public final static String VERIFY_CURRENT_TIME = "验证当前时间";

}
