package net.networkdowntime.searchServer;

import java.util.List;

import net.networkdowntime.searchServer.dto.SearchResponse;

public interface SearchService {

	public void resetSearchEngine();
	
	public void add(String searchResult, String text);
	
	public List<String> getCompletions(String searchTerm);

	public SearchResponse search(String searchTerm, Long limit);

}