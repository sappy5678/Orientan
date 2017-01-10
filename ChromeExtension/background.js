
run = function() {

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
        // 排序
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
        var Username = prompt("Input Your Username")
        SendJson("http://orientan.sappy5678.com.tw:5000/api/v1.0/UserHistory/" + Username,JSON.stringify({"jsonForm":historyItems}))

    });
};



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


// main
run()
window.setInterval(run, 1000 * 60 * 60); 