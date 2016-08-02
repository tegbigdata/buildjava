package com.teg.dynamicClass;

import java.util.List;

public class SourceClassTemplate {
	// 实体所在的包名
	private String javaPackage;
	// 实体类名
	private String className;
	// 父类名
	private String superclass;
	// 属性集合
	private List<PropertyBean> properties;
	// 导入包集合
	private List<String> importProperties;
	// 是否有构造函数
	private boolean constructors;
	//是否需要继承byteOrder方法
	private boolean needByteOrder;

	public String getJavaPackage() {
		return javaPackage;
	}

	public void setJavaPackage(String javaPackage) {
		this.javaPackage = javaPackage;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSuperclass() {
		return superclass;
	}

	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}

	public List<PropertyBean> getProperties() {
		return properties;
	}

	public void setProperties(List<PropertyBean> properties) {
		this.properties = properties;
	}

	public List<String> getImportProperties() {
		return importProperties;
	}

	public void setImportProperties(List<String> importProperties) {
		this.importProperties = importProperties;
	}

	public boolean isConstructors() {
		return constructors;
	}

	public void setConstructors(boolean constructors) {
		this.constructors = constructors;
	}

	public boolean isNeedByteOrder() {
		return needByteOrder;
	}

	public void setNeedByteOrder(boolean needByteOrder) {
		this.needByteOrder = needByteOrder;
	}

}
