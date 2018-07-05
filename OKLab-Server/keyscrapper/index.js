var request = require("request");
var cheerio = require("cheerio");
var cron = require('node-cron');
var scrapeIt = require("scrape-it");

var url = "https://api.naver.com/keywordstool?hintKeywords=아이유&showDetail=1";
var url2 = "http://naver.github.io/searchad-apidoc/#/guides";

cron.schedule('*/1 * * * * *', function(){
  var hrTime = new Date().getTime()
  console.log(hrTime);
  scrapeIt({url: url, headers: { "User-agent": "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)" ,
  "Content-Type" : "application/json; charset=UTF-8",
  "X-Timestamp":hrTime,
  "X-API-KEY":"0100000000473e1233850c12de26aad47666b0559316658062912fa0ba5927d2d3a6c49f91" ,
  "X-Customer":"1421887" ,
  "X-Signature":"1421887" ,}
  }).then( function ( data, response ) {
      // console.log("Status Code: "+response.statusCode);
      console.log(data);
  });

  request(url2, function (error, response, body) {
    if (!error) {
      var $ = cheerio.load(body);
      var temperature = $("[data-variable='temperature'] .wx-value").html();
      console.log("It’s " + temperature + " degrees Fahrenheit.");
    } else {
      console.log("We’ve encountered an error: " + error);
    }
  });
});

console.log('[KeyScrapper] Running a task every 5sec');
