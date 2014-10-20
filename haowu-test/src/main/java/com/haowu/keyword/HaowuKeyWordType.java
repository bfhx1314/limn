package com.haowu.keyword;

public class HaowuKeyWordType {
	
	/**
	 * 说明: 登陆 hoss平台<br>
	 * 参数1: 账号<br>
	 * 参数2: 密码<br>
	 * 用法: 登陆:[账号]:[密码]<br>
	 * 实例: 登陆:limn-manager:111111<br>
	 */
	public final static String LOGIN = "登录";
	
	/**
	 * 说明: 注销登陆 hoss平台<br>
	 * 实例: 注销<br>
	 */
	public final static String LOGOUT = "注销";
	
	/**
	 * 说明: 菜单的选择<br>
	 * 参数1: 菜单名称<br>
	 * 用法: 菜单:[菜单名称]<br>
	 * 实例: 菜单:审批管理<br>
	 */
	public final static String MENU = "菜单";
	
	/**
	 * 说明: 列表的选择<br>
	 * 参数1: 列表名称<br>
	 * 用法: 列表:[列表名称]<br>
	 * 实例: 列表:我的草稿<br>
	 */
	public final static String LIST = "列表";
	
	/**
	 * 说明: 启动浏览器<br>
	 * 可选参数1: 浏览器类型 : firefox,ie,chrome<br>
	 * 用法: 启动浏览器:[浏览器类型]<br>
	 * 实例: 启动浏览器:firefox<br>
	 */
	public final static String START_BROWSER = "启动浏览器";
	
	/**
	 * 说明: 关闭打开的浏览器<br>
	 * 实例: 关闭浏览器
	 */
	public final static String CLOSE_BROWSER = "关闭浏览器";
	
	/**
	 * 说明: 工作流审批流程,所有审批环节都是通过<br>
	 * 参数1: 审批单名称(包括以下单据:<br>
	 * 			项目全周期预算表<br>
	 * 			项目月度预算表<br>
	 * 			项目费用申请单<br>
	 * 			项目费用报销单<br>
	 * 			项目合同审批单<br>
	 * 			项目合同付款单<br>
	 * 			项目物品领用申请单<br>
	 * 			项目物品核销单<br>
	 * 			渠道佣金结算单<br>
	 * 			渠道佣金结算标准申请单<br>
	 * 			渠道任务分解<br>
	 * 			项目保证金付款单<br>
	 * 			项目退款申请单)<br>
	 * 用法: 工作流:[审批单名称]<br>
	 * 实例: 工作流:项目全周期预算表<br>
	 * 提醒: 请注意审批单直接的关联,以及直接的依赖<br>
	 */				
	public final static String WORKFLOW = "工作流";
	
	/**
	 * 说明: 工作流审批流程,覆盖所有的审批操作<br>
	 * 参数1: 审批单名称(包括以下单据:<br>
	 * 			项目全周期预算表<br>
	 * 			项目月度预算表<br>
	 * 			项目费用申请单<br>
	 * 			项目费用报销单<br>
	 * 			项目合同审批单<br>
	 * 			项目合同付款单<br>
	 * 			项目物品领用申请单<br>
	 * 			项目物品核销单<br>
	 * 			渠道佣金结算单<br>
	 * 			渠道佣金结算标准申请单<br>
	 * 			渠道任务分解<br>
	 * 			项目保证金付款单<br>
	 * 			项目退款申请单)<br>
	 * 用法: 工作流:[审批单名称]<br>
	 * 实例: 工作流:项目全周期预算表<br>
	 * 提醒: 请注意审批单直接的关联,以及直接的依赖<br>
	 */	
	public final static String WORKFLOW_TEST = "工作流测试";
	
	public final static String Check = "验证";
}
