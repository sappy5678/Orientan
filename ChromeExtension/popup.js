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
        //送出jsonForm
        //JquerySendJson("http://localhost:5000/postTest",JSON.stringify(historyItems))
        SendJson("http://localhost:5000/api/v1.0/UserHistory/TestUser",JSON.stringify({"jsonForm":historyItems}))
	      for (var i = 0; i < historyItems.length; ++i) {
	        var url = historyItems[i].url;
          // console.log(url);
          // console.log(historyItems[i].title);
          // console.log(historyItems[i].visitCount);

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


function SendJson(destin,json)
{

  var xhr = new XMLHttpRequest();
  xhr.open('POST', destin);
  //xhr.setRequestHeader("Content-Type", "application/json;charset=UTF-8");
  xhr.send(json);

  xhr.onreadystatechange = function(){
    var DONE = 4; // readyState 4 代表已向服务器发送请求
    var OK = 200; // status 200 代表服务器返回成功
    if(xhr.readyState === DONE){
        if(xhr.status === OK){
            console.log(xhr.responseText); // 这是返回的文本
        } else{
            console.log("Error: "+ xhr.status); // 在此次请求中发生的错误
        }
    }
}
}

function JquerySendJson(destin,json)
{
  checkRegAcc = function (){
  $.ajax({
    url: destin,
    type: 'POST',
    data: {
      jsonForm: json
    },
    error: function(xhr) {
      console.log('Ajax request 發生錯誤');
    },
    success: function(response) {
      console.log(response)
 
    }
  });
};
}