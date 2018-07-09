var request = require("request");
var cheerio = require("cheerio");
var cron = require('node-cron');
var scrapeIt = require("scrape-it");
var CryptoJS = require("crypto-js");
var qs = require('querystring');

var baseUrl = "https://api.naver.com";
var keyword = qs.escape("속옷")
var path = "/keywordstool?format=json&siteId=&mobileSiteId=&hintKeywords=" +keyword+"&includeHintKeywords=0&showDetail=1&biztpId=&mobileBiztpId=&month=&event=&keyword=";
var apiKey = "0100000000473e1233850c12de26aad47666b0559316658062912fa0ba5927d2d3a6c49f91";
var secretKey = "AQAAAABHPhIzhQwS3iaq1HZmsFWTgDNaBI4y5Ka7cwgttfkwIg==";
var customerId = "1421887";

cron.schedule('*/1 * * * * *', function(){
  var hrTime = new Date().getTime()
  console.log(hrTime);
  scrapeIt({
    url: baseUrl + path ,
   headers: { "User-agent": "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)" ,
    "Content-Type" : "application/json; charset=UTF-8",
    "X-Timestamp":hrTime,
    "X-API-KEY": apiKey ,
    "X-Customer": customerId ,
    "X-Signature": getSignature(hrTime + "." + "GET" + "." + "/keywordstool")}
  }).then( function ( data, response ) {
      // console.log("Status Code: "+response.statusCode);
      console.log(data);
  });

//   var options = {
//     url: url,
//     headers: {
//       "User-agent": "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)" ,
//        "Content-Type" : "application/json; charset=UTF-8",
//        "X-Timestamp":hrTime,
//        "X-API-KEY": apiKey ,
//        "X-Customer": customerId ,
//        "X-Signature": getSignature(hrTime + "." + "GET" + "." + "/keywordstool")
//     }
//   };
//
//   request(options, function (error, response, body) {
//     if (!error) {
//       console.log(body)
//       // var $ = cheerio.load(body);
//       // var temperature = $("[data-variable='temperature'] .wx-value").html();
//       // console.log("It’s " + temperature + " degrees Fahrenheit.");
//     } else {
//       console.log("We’ve encountered an error: " + error);
//     }
//   });
});

console.log('[KeyScrapper] Running a task every 5sec');

function getSignature(data){
  var hash = CryptoJS.HmacSHA256(data, secretKey);
    var hashInBase64 = CryptoJS.enc.Base64.stringify(hash);
  return  hashInBase64
}
