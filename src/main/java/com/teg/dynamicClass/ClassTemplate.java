package com.teg.dynamicClass;

import java.util.List;

public class ClassTemplate {
	// 实体所在的包名
	private String javaPackage;
	// 实体类名
	private String className;
	// 父类名
	private String superclass;
	// 属性集合
	private List<String> propertyLines;
	// 导入包集合
	private List<ImportPropertyBean> importProperties;
	// 是否有构造函数
	private boolean constructors;
	// 是否需要继承byteOrder方法
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

	public List<String> getPropertyLines() {
		return propertyLines;
	}

	public void setPropertyLines(List<String> propertyLines) {
		this.propertyLines = propertyLines;
	}

	public void setSuperclass(String superclass) {
		this.superclass = superclass;
	}

	public List<ImportPropertyBean> getImportProperties() {
		return importProperties;
	}

	public void setImportProperties(List<ImportPropertyBean> importProperties) {
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
