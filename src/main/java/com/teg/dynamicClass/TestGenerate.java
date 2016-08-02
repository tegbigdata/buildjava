package com.teg.dynamicClass;

import com.teg.dynamicClass.utils.JDBCUtil;

public class TestGenerate {
	@SuppressWarnings("unchecked")
	public static void main(String[] args) {
		
		final String javaPackage = "com.aircondtion.base";  //生成的STRUCT存放位置. e.g. com.aircondtion.base
		/**
		 * 存放signal info的表
		 * Note 必须包含如下用于自动生成Struct的相关字段 :
		 *      build_scope varchar  (public/private)
		 *      build_isfinal     tinyint (1/0)
		 *      build_isstatic	tinyint (1/0)
		 *      build_type	 varchar (Unsigned8,Unsigned16, int, Integer, boolean,float..........)
		 *      build_value  varchar 100  (与build_type对应的值)
		 *      build_orderid int   (生成顺序ID)
		 *      build_groupid int   (对象CLASSNAME表的groupid)
		 *      build_name varchar  (备用 如果singalName字段 则使用build_name)
		 *      
		 */
		final String signalTableName = "signalproperties"; //signal info 表名
		final String classTableName = "signalclassname";   //class name 表名 默认：signalclassname
		final String signalName = "signalName"; //signalTableName中信号名称（英文）的字段名
		String scrPath = "/src/main/java"; //工程的java source根路径  如：  maven: /src/main/java , JAVA project :/src
		JDBCUtil jdbcUtilSingle = JDBCUtil.getInitJDBCUtil("jdbc:mysql://localhost:3306/test", "root","123456"); 	//设置数据库连接配置
		DynamicClassGenerator.buildDynamicJava (jdbcUtilSingle, signalTableName, signalName, classTableName, javaPackage, scrPath);
		
		DynamicClassGenerator.buildDynamicJava (jdbcUtilSingle, "test_signalinfo", "signalName", "signalclassname1", javaPackage, scrPath);
	}
}
