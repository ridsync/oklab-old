module.exports = function(express){
  var mongoose = require('mongoose');
  var route =  require('express').Router();

  route.get('/', function(req, res, next) {
    res.render('index', { title: 'OK MEAN STACK: Main' });
  });

  route.get('/about', function(req, res, next) {
    res.render('about', { title: 'OK MEAN STACK: About' });
  });

  route.get('/game', function(req, res, next) {
    res.render('game', { title: 'OK MEAN STACK: Game DEV' });
  });

  return route;
}
