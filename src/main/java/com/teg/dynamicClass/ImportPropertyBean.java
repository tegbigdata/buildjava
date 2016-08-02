package com.teg.dynamicClass;

public class ImportPropertyBean {

	// 导入包名称
	private String jarName;

	public ImportPropertyBean(String jarName) {
		super();
		this.jarName = jarName;
	}

	public String getJarName() {
		return jarName;
	}

	public void setJarName(String jarName) {
		this.jarName = jarName;
	}

}
