package com.teg.dynamicClass;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teg.dynamicClass.exceptions.BuildException;
import com.teg.dynamicClass.utils.JDBCUtil;
import com.teg.dynamicClass.utils.StringUtil;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DynamicClassGenerator {
	private static void generateClass(SourceClassTemplate sourceTemplate, String ftlName, String srcPath)
			throws BuildException {
		try {
			// 指定模板文件路径
			ClassTemplate classTemplate = fillClassTemplate(sourceTemplate);
			Configuration cfg = new Configuration();
			cfg.setClassForTemplateLoading(DynamicClassGenerator.class, "template");
			cfg.setDefaultEncoding("UTF-8");
			// cfg.setObjectWrapper(new DefaultObjectWrapper());
			// 获取 模板文件
			Template ftlTemplate = cfg.getTemplate(ftlName); // 可从参数配置
			// 创建 数据模型
			Map<String, Object> dataModal = new HashMap<String, Object>();
			dataModal.put("entity", classTemplate);
			// 创建JAVA 文件
			buildjava(dataModal, classTemplate, ftlTemplate, srcPath);

		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
	}

	private static SourceClassTemplate initSourceClassTemplate(String javaPackage, String className,
			boolean isNeedByteOrder) {
		SourceClassTemplate template = new SourceClassTemplate();
		template.setJavaPackage(javaPackage); // 创建包名
		template.setClassName(className); // 创建类名
		template.setConstructors(false); // 是否创建构造函数
		template.setSuperclass(PropertyEnum.STRUCT);
		template.setNeedByteOrder(isNeedByteOrder);
		// 导入包
		List<String> importPropertyList = new ArrayList<String>();
		importPropertyList.add(PropertyEnum.STRUCT_JARPATH);
		if (isNeedByteOrder) {
			// 增加导入包
			importPropertyList.add(PropertyEnum.BYTEORDER_JARPATH);
		}
		template.setImportProperties(importPropertyList);
		return template;
	}

	private static void buildjava(Map<String, Object> dataModal, ClassTemplate classTemplate, Template ftlTemplate,
			String srcPath) throws IOException, TemplateException {
		// 创建.java类文件
		String filepath = System.getProperty("user.dir") + srcPath;
		File outDirFile = new File(filepath);
		if (!outDirFile.exists()) {
			outDirFile.mkdir();
		}
		File javaFile = toJavaFilename(outDirFile, classTemplate.getJavaPackage(), classTemplate.getClassName());
		// 创建.java类文件
		if (javaFile != null) {
			Writer javaWriter = new FileWriter(javaFile);
			ftlTemplate.process(dataModal, javaWriter);
			javaWriter.flush();
			// System.out.println("文件生成路径：" + javaFile.getCanonicalPath());
			javaWriter.close();
		}
		// print console
		Writer out = new OutputStreamWriter(System.out);
		ftlTemplate.process(dataModal, out);
		out.flush();
		out.close();
	}

	/**
	 * 创建.java文件所在路径 和 返回.java文件File对象
	 * 
	 * @param outDirFile
	 *            生成文件路径
	 * @param javaPackage
	 *            java包名
	 * @param javaClassName
	 *            java类名
	 * @return
	 */
	private static File toJavaFilename(File outDirFile, String javaPackage, String javaClassName) {
		String packageSubPath = javaPackage.replace('.', '/');
		File packagePath = new File(outDirFile, packageSubPath);
		File file = new File(packagePath, javaClassName + ".java");
		if (!packagePath.exists()) {
			packagePath.mkdirs();
		}
		return file;
	}

	/**
	 * 根据属性配置创建 声明变量语句
	 * 
	 * @param attribute
	 * @return
	 */
	private static String buildDeclareLine(PropertyBean attribute) {
		StringBuffer retBuffer = new StringBuffer();
		retBuffer.append(attribute.getPropertyScope());
		if (attribute.isFinalFlag()) {
			retBuffer.append(" final");
		}
		if (attribute.isStaticFlag()) {
			retBuffer.append(" static");
		}
		retBuffer.append(" " + attribute.getPropertyType() + " " + attribute.getPropertyName());
		if (attribute.getPropertyValue() != null) {
			// 赋值
			retBuffer.append(" = " + attribute.getPropertyValue());
		} else if (attribute.isFinalFlag()) {
			// new object
			retBuffer.append(" = new " + attribute.getPropertyType() + "()");
		}

		retBuffer.append(";");
		return retBuffer.toString();
	}

	private static void checkSourceClassTemplate(SourceClassTemplate template) throws BuildException {
		if (StringUtil.isEmpty(template.getClassName())) {
			throw new BuildException("Class name is null.");
		}
		if (StringUtil.isEmpty(template.getJavaPackage())) {
			template.setJavaPackage(PropertyEnum.TARGET_PATH);
		}
		if (StringUtil.isEmpty(template.getSuperclass())) {
			template.setSuperclass(PropertyEnum.STRUCT);
		}

	}

	private static void checkProperty(PropertyBean propertyBean) throws BuildException {
		if (StringUtil.isEmpty(propertyBean.getPropertyName())) {
			throw new BuildException("property name is null.");
		} else if (StringUtil.isEmpty(propertyBean.getPropertyType())) {
			throw new BuildException("property type is null.");
		}

		if (StringUtil.isEmpty(propertyBean.getPropertyScope())) {
			propertyBean.setPropertyScope(PropertyEnum.SCOPE_PUBLIC);
		}
	}

	private static ClassTemplate fillClassTemplate(SourceClassTemplate source) throws BuildException {
		checkSourceClassTemplate(source);
		ClassTemplate template = new ClassTemplate();
		template.setJavaPackage(source.getJavaPackage()); // 创建包名
		template.setClassName(source.getClassName()); // 创建类名
		template.setConstructors(source.isConstructors()); // 是否创建构造函数
		template.setSuperclass(source.getSuperclass()); // 是否有父类
		template.setNeedByteOrder(source.isNeedByteOrder()); // 是否有byteOrder方法

		List<String> propertyLineList = new ArrayList<String>();
		if (source.getProperties() != null && !source.getProperties().isEmpty()) {
			for (PropertyBean propertyBean : source.getProperties()) {
				checkProperty(propertyBean);
				propertyLineList.add(buildDeclareLine(propertyBean));
			}
		}

		// 将属性集合添加到实体对象中
		template.setPropertyLines(propertyLineList);

		List<ImportPropertyBean> importPropertyList = new ArrayList<ImportPropertyBean>();

		boolean findStruct = false;
		if (source.getImportProperties() != null && !source.getImportProperties().isEmpty()) {
			for (String importProperties : source.getImportProperties()) {
				if (!findStruct && importProperties.trim().equals(PropertyEnum.STRUCT_JARPATH)) {
					findStruct = true;
				}
				importPropertyList.add(new ImportPropertyBean(importProperties));
			}
		}
		if (!findStruct) {
			importPropertyList.add(new ImportPropertyBean(PropertyEnum.STRUCT_JARPATH));
		}
		template.setImportProperties(importPropertyList);

		return template;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, SourceClassTemplate> buildSourceClassTemplate(JDBCUtil jdbcUtilSingle,String javaPackage, String classTableName) {
		//signalclassname1;
		Map<String, SourceClassTemplate> classMap = new HashMap<String, SourceClassTemplate>();
		String field_groupId = JDBCUtil.FILED_CLASSGROUPID;
		try {

			String classNameSql = "select * from " + classTableName;
			List<Map<String, Object>> list = jdbcUtilSingle.query(classNameSql, List.class);

			if (list != null) {
				for (Map<String, Object> map : list) {
					String groupid = map.get(field_groupId).toString();
					boolean isNeedByteOrder = map.get(JDBCUtil.FIELD_BYTEORDERFLAG).toString().equals("1") ? true : false;
					String className = map.get(JDBCUtil.FIELD_CLASSNAME).toString();
					SourceClassTemplate template = DynamicClassGenerator.initSourceClassTemplate(javaPackage, className,
							isNeedByteOrder);
					classMap.put(groupid, template);
				}

			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return classMap;
	}

	@SuppressWarnings("unchecked")
	private static Map<String, List<PropertyBean>> buildSourceClassProperties(JDBCUtil jdbcUtilSingle,
			String signalTableName, String classTableName, String signalName) {
		Map<String, List<PropertyBean>> propertiesMap = new HashMap<String, List<PropertyBean>>();

		try {
			String field_groupId = JDBCUtil.FILED_GROUPID;
			String field_orderId = JDBCUtil.FILED_ORDERID;
			String field_className =JDBCUtil.FIELD_CLASSNAME;
			String field_classGroupId = JDBCUtil.FILED_CLASSGROUPID;
			String propertiesSql = "select " + signalTableName + ".*, "+classTableName+"."+field_className+" from " + signalTableName
					+ ","+classTableName+" " + "where " + signalTableName + "." + field_groupId
					+ " = "+classTableName+"."+field_classGroupId+" " + "ORDER BY " + field_groupId + "," + field_orderId + "";
			
			List<Map<String, Object>> list = jdbcUtilSingle.query(propertiesSql, List.class);
			// String preGroupId ="";
			if (list != null) {
				for (Map<String, Object> map : list) {

					PropertyBean attribute = new PropertyBean();
					attribute.setPropertyScope(map.get(JDBCUtil.FILED_SCOPE).toString());
					boolean finalFlag = map.get(JDBCUtil.FILED_FINALFLAG).toString().equals("1") ? true : false;
					attribute.setFinalFlag(finalFlag);
					boolean staticFlag = map.get(JDBCUtil.FILED_STATICFLAG).toString().equals("1") ? true : false;
					attribute.setStaticFlag(staticFlag);
					attribute.setPropertyType(map.get(JDBCUtil.FILED_TYPE).toString());
					if (map.get(signalName) != null) {
						attribute.setPropertyName(map.get(signalName).toString());
					} else if (map.get(JDBCUtil.FILED_NAME) != null) {
						attribute.setPropertyName(map.get(JDBCUtil.FILED_NAME).toString());
					} else {
						attribute.setPropertyName("null");
					}
					if (map.get(JDBCUtil.FILED_VALUE) != null) {
						attribute.setPropertyValue(map.get(JDBCUtil.FILED_VALUE).toString());
					}

					String groupid = map.get(field_groupId).toString();
					List<PropertyBean> propertyList = propertiesMap.get(groupid);
					if (propertyList == null) {
						propertyList = new ArrayList<PropertyBean>();
					}
					propertyList.add(attribute);
					propertiesMap.put(groupid, propertyList);
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return propertiesMap;
	}

	public static void buildDynamicJava(JDBCUtil jdbcUtilSingle, String signalTableName, String signalName, String classTableName, String javaPackage, String scrPath) {
		//String classTableName = "signalclassname";
		Map<String, SourceClassTemplate> classMap = DynamicClassGenerator.buildSourceClassTemplate(jdbcUtilSingle, javaPackage, classTableName);
		Map<String, List<PropertyBean>> propertiesMap = DynamicClassGenerator.buildSourceClassProperties(jdbcUtilSingle,
				signalTableName, classTableName, signalName);

		for (String key : classMap.keySet()) {
			SourceClassTemplate template = classMap.get(key);
			template.setProperties(propertiesMap.get(key));
			DynamicClassGenerator.generateClass(template, "classTemplate.ftl", scrPath);
		}

	}

}
