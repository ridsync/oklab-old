module.exports = function(express){
  var conn = require('../config/mysql/db')();
  var route =  require('express').Router();

  route.get('/', function(req, res, next) {
    res.render('index', { title: 'OK Express Test' , user:'fdafsd'});
  });
  return route;
}
