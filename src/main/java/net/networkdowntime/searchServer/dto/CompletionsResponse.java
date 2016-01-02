package net.networkdowntime.searchServer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class CompletionsResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	List<String> results = new ArrayList<String>();

	public List<String> getResults() {
		return results;
	}

	public void setResults(List<String> results) {
		this.results = results;
	}

	public void addResult(String completion) {
		this.results.add(completion);
	}

}
