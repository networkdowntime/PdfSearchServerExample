package net.networkdowntime.searchServer.impl;

import java.io.IOException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.networkdowntime.searchServer.PdfDocumentService;
import net.networkdowntime.searchServer.SearchService;
import net.networkdowntime.searchServer.dto.SearchResponse;
import net.networkdowntime.searchServer.dto.SearchResult;

@Service("pdfDocumentService")
public class PdfDocumentServiceImpl implements PdfDocumentService {
	static final Logger logger = LogManager.getLogger(PdfDocumentService.class.getName());

	PDDocument pddDocument;
	PDFTextStripper textStripper;
	String url = "";
	
	@Autowired
	SearchService searchService;

	@Override
	public void indexDocument(String url) throws IOException {
		pddDocument = PDDocument.load(new URL(url));
		int pageCount = pddDocument.getNumberOfPages();
		this.url = url;
		
		searchService.resetSearchEngine();

		textStripper = new PDFTextStripper();

		for (int page = 1; page <= pageCount; page++) {
			textStripper.setStartPage(page);
			textStripper.setEndPage(page);
			String doc = textStripper.getText(pddDocument);

			searchService.add(Integer.toString(page), doc);
			logger.debug("Added page: " + page);
		}
	}

	public SearchResponse attachPageTextToSearchResponse(SearchResponse response) {
		for (SearchResult result : response.getResults()) {
			if ("Long".equals(result.getType())) {
				int page = Integer.parseInt(result.getResult());
				textStripper.setStartPage(page);
				textStripper.setEndPage(page);
				result.setPage(page);
				String doc;
				try {
					doc = textStripper.getText(pddDocument);
				} catch (IOException e) {
					e.printStackTrace();
					doc = "Error in PDF processing.";
				}

				result.setResult(doc);
			}
		}
		return response;
	}

	@Override
	public String getPdfTitle() {
		if (pddDocument != null) {
			return pddDocument.getDocumentInformation().getTitle();
		} else {
			return "";
		}
	}
	
	@Override
	public String getUrl() {
		return url;
	}
}
