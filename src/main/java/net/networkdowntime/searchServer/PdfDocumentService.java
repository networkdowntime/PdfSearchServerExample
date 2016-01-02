package net.networkdowntime.searchServer;

import java.io.IOException;

import net.networkdowntime.searchServer.dto.SearchResponse;

public interface PdfDocumentService {

	public void indexDocument(String url) throws IOException;
	
	public SearchResponse attachPageTextToSearchResponse(SearchResponse response);
	
	public String getPdfTitle();
	
	public String getUrl();
	
}