function highlight_words(keywords, element) {
    if(keywords) {
        var textNodes;
        var str = keywords.split(" ");
        $(str).each(function() {
            var term = this;
            var textNodes = $(element).contents().filter(function() { return this.nodeType === 3 });
            textNodes.each(function() {
              var content = $(this).text();
              var regex = new RegExp("\\b"+term+"\\b", "gi");
              content = content.replace(regex, '<span class="highlight">' + term + '</span>');
              $(this).replaceWith(content);
            });
        });
    }
}

function scanPdf() {
	$("#pdf-status").empty();
	$("#pdf-status").append("Scanning PDF...");
	$.get("/searchEngine/indexPdf", { url: $("#pdf-input").val() }, function (data) {
		getPdfTitle(false);
	});    	
}

function getPdfTitle(initialLoad) {
	$.get("/searchEngine/getPdfTitle", function (data) {
		$("#pdf-status").empty();
		if (data.value != "") {
	  		$("#pdf-status").append("Scanned " + data.value + "!");
		} else {
			if (!initialLoad) {
				$("#pdf-status").append("Scanned!");
			}
		}
	});    	
}

function getPdfUrl() {
	$.get("/searchEngine/getPdfUrl", function (data) {
		if (data.value != "") {
			$("#pdf-input").val(data.value);
		}
	});    	
}

var lastCompletions = [];

$(document).ready(function() {
	getPdfUrl();
	getPdfTitle(true);
	
    $("#pdf-button").click(function() {
    	scanPdf();
    });

	
	function doSearch() {
		$.get("/searchEngine/search", { query: $("#search-input").val() }, function (data) {
		    var out = data.results;
	  		$("#results-outer").html("<div id=\"results\"></div>");
	    	$(out).each(function(id, result) {
	      		$("#results").append("<h3>" + (id + 1) + ": Page: " + result.page + " Search Weight: " + result.weight + "</h3><div class='result-text'>" + result.result + "</div>");
			});
	    	$( "#results" ).accordion();
	    	$(lastCompletions).each(function(key, value) {
		    	highlight_words(value, ".result-text");
	    	});
		});    	
	}

    $("#search-button").click(function() {
    	$('.ui-autocomplete').hide('');
    	doSearch();
    });

    $("#search-input").keypress(function(e){
        var code = (e.keyCode ? e.keyCode : e.which);
	    if(code == 13) {
	    	$('.ui-autocomplete').hide('');
			doSearch();
	    }
	});

 	$("#search-input").autocomplete({
    	source : function (request, response) {
    		lastCompletions = [];
    		$.get("/searchEngine/completions", { query: $("#search-input").val() }, function (data) {
                var out = data.results;
                response($.each(out, function(key, value) {
                	lastCompletions.push(value);
                    return {
                        label: key,
                        value: value
                    };
                }));
            });
        },
		select : function(event, ui) {
			doSearch();
			return false;
		},
        minLength: 1,
 
        html: true, // optional (jquery.ui.autocomplete.html.js required)
 
      // optional (if other layers overlap autocomplete list)
        open: function(event, ui) {
            $(".ui-autocomplete").css("z-index", 1000);
        }
    });
 	
});