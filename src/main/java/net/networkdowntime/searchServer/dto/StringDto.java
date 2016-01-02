package net.networkdowntime.searchServer.dto;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class StringDto implements Serializable {
	private static final long serialVersionUID = 1L;

	String value;

	public StringDto(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
