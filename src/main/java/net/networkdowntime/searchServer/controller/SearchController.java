package net.networkdowntime.searchServer.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import net.networkdowntime.searchServer.PdfDocumentService;
import net.networkdowntime.searchServer.SearchService;
import net.networkdowntime.searchServer.dto.CompletionsResponse;
import net.networkdowntime.searchServer.dto.SearchResponse;
import net.networkdowntime.searchServer.dto.StringDto;

@RestController
@RequestMapping("/searchEngine")
public class SearchController {

	@Autowired
	SearchService searchService;

	@Autowired
	PdfDocumentService documentService;

	@RequestMapping(value = "/add", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseStatus(value = HttpStatus.OK)
	public void add(
			@RequestParam(required = true, value = "searchResult") String searchResult,
			@RequestParam(required = false, value = "text") String text) {

		searchService.add(searchResult, text);
	}

	@RequestMapping(value = "/completions", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody CompletionsResponse completions(
			@RequestParam(required = false, value = "query") String keywords) {

		CompletionsResponse response = new CompletionsResponse();
		response.setResults(searchService.getCompletions(keywords));

		return response;
	}

	@RequestMapping(value = "/search", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	public @ResponseBody SearchResponse search(
			@RequestParam(required = false, value = "query") String keywords,
			@RequestParam(required = false, value = "limit") Long limit) {

		SearchResponse searchResponse = searchService.search(keywords, limit);
		searchResponse = documentService.attachPageTextToSearchResponse(searchResponse);
		
		return searchResponse;
	}

	@RequestMapping(value = "/indexPdf", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseStatus(value = HttpStatus.OK)
	public void indexPdf(
			@RequestParam(required = true, value = "url") String url) {
		try {
		documentService.indexDocument(url);
		} catch (IOException e) {
			throw new IllegalArgumentException(e.getMessage());
		}
	}

	@RequestMapping(value = "/getPdfTitle", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody StringDto getPdfTitle() {
		return new StringDto(documentService.getPdfTitle());
	}

	@RequestMapping(value = "/getPdfUrl", method = RequestMethod.GET, produces = { "application/json;charset=UTF-8" })
	@ResponseStatus(value = HttpStatus.OK)
	public @ResponseBody StringDto getPdfUrl() {
		return new StringDto(documentService.getUrl());
	}

}
