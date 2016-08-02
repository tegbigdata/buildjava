package com.teg.dynamicClass;

/**
 * 实体对应的属性类
 * 
 * @author Cheng Pei.
 *
 */
public class PropertyBean {
	// 属性数据作用域
	private String propertyScope;
	// 属性名称
	private String propertyName;
	//属性数据类型
	private String propertyType;
	//属性数据值
	private String propertyValue;
	//属性 static 标记
	private boolean staticFlag;
	//属性 final 标记
	private boolean finalFlag;

	public String getPropertyValue() {
		return propertyValue;
	}

	public void setPropertyValue(String propertyValue) {
		this.propertyValue = propertyValue;
	}

	public boolean isStaticFlag() {
		return staticFlag;
	}

	public void setStaticFlag(boolean staticFlag) {
		this.staticFlag = staticFlag;
	}

	public boolean isFinalFlag() {
		return finalFlag;
	}

	public void setFinalFlag(boolean finalFlag) {
		this.finalFlag = finalFlag;
	}

	public String getPropertyScope() {
		return propertyScope;
	}

	public void setPropertyScope(String propertyScope) {
		this.propertyScope = propertyScope;
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

}
