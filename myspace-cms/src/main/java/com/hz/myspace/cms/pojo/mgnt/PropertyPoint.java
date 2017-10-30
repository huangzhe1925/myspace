package com.hz.myspace.cms.pojo.mgnt;

import java.util.List;

public class PropertyPoint {

	private String propertyName;
	private String propertyType;
	private List<String> propertyStringValues;

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

	public List<String> getPropertyStringValues() {
		return propertyStringValues;
	}

	public void setPropertyStringValues(List<String> propertyStringValues) {
		this.propertyStringValues = propertyStringValues;
	}

	@Override
	public String toString() {
		return "PropertyPoint [propertyName=" + propertyName + ", propertyType=" + propertyType
				+ ", propertyStringValues=" + propertyStringValues + "]";
	}

}
