package com.haowu.uitest.hossweb;

import com.haowu.parameter.ParameterHaowu;
import com.haowu.parameter.Variable;
import com.limn.tool.parameter.Parameter;

public class Initialization {
	
	/**
	 * 初始化所有的环境变量
	 */
	public static void start(){
		baseInit();
		applyInit();
	}
	
	private static void baseInit(){
		
		Variable.init(ParameterHaowu.COREPATH);
		
		ParameterHaowu.HOSS_IP = Variable.getValue("hoss.ip");
		
		ParameterHaowu.HOSS_PORT = Integer.parseInt(Variable.getValue("hoss.port"));
		
		ParameterHaowu.HAOWU_IP = Variable.getValue("haowu.ip");
		
		ParameterHaowu.HAOWU_PORT = Integer.parseInt(Variable.getValue("haowu.port"));
		
		Parameter.DBDRIVER = Variable.getValue("db.driver");
		
		Parameter.DBURL = Variable.getValue("db.url");
		
		Parameter.DBUSER = Variable.getValue("db.user");
		
		Parameter.DBPASS = Variable.getValue("db.pass");
		
		ParameterHaowu.HAOWU_URL = Variable.getValue("haowu.url");
		
		Variable.close();
	}
	
	public static void applyInit(){
		
		Variable.init(ParameterHaowu.APPLYPATH);
		
		ParameterHaowu.Manager = Variable.getValue("Manager");
		ParameterHaowu.Director = Variable.getValue("Director");
		ParameterHaowu.City = Variable.getValue("City");
		ParameterHaowu.Area = Variable.getValue("Area");
		ParameterHaowu.ZJGL = Variable.getValue("ZJGL");
		ParameterHaowu.Business = Variable.getValue("Business");
		ParameterHaowu.FGLD = Variable.getValue("FGLD");
		ParameterHaowu.CWZJ = Variable.getValue("CWZJ");
		ParameterHaowu.CWZG = Variable.getValue("CWZG");
		ParameterHaowu.XZFZ = Variable.getValue("XZFZ");
		ParameterHaowu.GLZX = Variable.getValue("GLZX");
		ParameterHaowu.Chairman = Variable.getValue("Chairman");
		
		ParameterHaowu.General_Password = Variable.getValue("General_Password");
		ParameterHaowu.CityName = Variable.getValue("CityName");
		ParameterHaowu.ProjectName = Variable.getValue("ProjectName");
		
		
		
		
	}

}
