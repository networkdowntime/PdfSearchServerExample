package net.networkdowntime.searchServer.impl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.math.NumberUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import net.networkdowntime.search.engine.InMemorySearchEngine;
import net.networkdowntime.search.engine.SearchEngine;
import net.networkdowntime.searchServer.SearchService;
import net.networkdowntime.searchServer.dto.SearchResponse;
import net.networkdowntime.searchServer.dto.SearchResult;

@Service("searchService")
public class SearchServiceImpl implements SearchService {
	static final Logger logger = LogManager.getLogger(SearchServiceImpl.class.getName());

	SearchEngine searchEngine = new InMemorySearchEngine();

	@Override
	public void resetSearchEngine() {
		searchEngine = new InMemorySearchEngine();
	}
	
	@Override
	public void add(String searchResult, String text) {
		boolean added = false;
	
		if (NumberUtils.isNumber(searchResult)) {
			try {
				Long longResult = Long.parseLong(searchResult);
				searchEngine.add(longResult, text);
				added = true;
				logger.debug("Added text for long result: " + longResult);
			} catch (NumberFormatException e) {
			}
		}
		if (!added) {
			searchEngine.add(searchResult, text);
			logger.debug("Added text for string result: " + searchResult);
		}
	}

	@Override
	public List<String> getCompletions(String searchTerm) {
		return searchEngine.getCompletions(searchTerm, true, 10);
	}

	@Override
	public SearchResponse search(String searchTerm, Long limit) {
		logger.debug("SearchServiceImpl.search(): Begin; searchTerm: " + searchTerm + "; limit: " + limit);
		limit = (limit == null) ? 20 : limit;

		SearchResponse response = new SearchResponse();

		// get the search results, prep productIds for the DB query, and set the order in orderedProducts
		for (@SuppressWarnings("rawtypes") net.networkdowntime.search.SearchResult result : searchEngine.search(searchTerm, limit.intValue())) {
			SearchResult dtoResult = new SearchResult();
			dtoResult.setType(result.getType().toString());
			dtoResult.setWeight(result.getWeight());
			dtoResult.setResult(result.getResult().toString());
			response.addResult(dtoResult);
		}

		logger.debug("SearchServiceImpl.search(): End");

		return response;
	}
}
