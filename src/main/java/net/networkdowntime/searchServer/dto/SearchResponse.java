package net.networkdowntime.searchServer.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement
public class SearchResponse implements Serializable {
	private static final long serialVersionUID = 1L;

	List<SearchResult> results = new ArrayList<SearchResult>();

	public List<SearchResult> getResults() {
		return results;
	}

	public void addResult(SearchResult product) {
		this.results.add(product);
	}
	
	
}
