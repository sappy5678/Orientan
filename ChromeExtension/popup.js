function renderStatus(statusText) {
  document.getElementById('status').textContent = statusText;
}

document.addEventListener('DOMContentLoaded', function() {
var historyString="";
  // browes time www
 chrome.history.search({
	      'text': '' ,
        'maxResults': 1000             // Return every history item....
         // that was accessed less than one week ago.
	    },
	    function(historyItems) {
	      // For each history item, get details on all visits.
        console.log("length" + historyItems.length);
        historyItems.sort(function(a,b){
          if(a.visitCount < b.visitCount){
            return 1;
          }
          else if(a.visitCount > b.visitCount){
            return -1;
          }
          else
            return 0;
          
        });
	      for (var i = 0; i < historyItems.length; ++i) {
	        var url = historyItems[i].url;
          console.log(url);
          console.log(historyItems[i].title);
          console.log(historyItems[i].visitCount);

	        var processVisitsWithUrl = function(url) {
	          // We need the url of the visited item to process the visit.
	          // Use a closure to bind the  url into the callback's args.
            console.log("HHH");
            console.log(url);
	          /*
            return function(visitItems) {
	            processVisits(url, visitItems);
          };
          */
          
	        };
	       // chrome.history.getVisits({url: url}, processVisitsWithUrl(url));
	       // numRequestsOutstanding++;
	      }
        /*
	      if (!numRequestsOutstanding) {
	        onAllVisitsProcessed();
	      }
        */
    });
});