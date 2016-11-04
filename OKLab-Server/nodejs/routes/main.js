module.exports = function(express){
  var mongoose = require('mongoose');
  var route =  require('express').Router();

  route.get('/', function(req, res, next) {
    res.render('index', { title: 'OK MEAN STACK Test' });
  });
  return route;
}
