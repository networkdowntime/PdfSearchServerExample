package net.networkdowntime.searchServer.dto;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchResult {
	String type;
	String result;
	int weight;
	int page;

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

}
