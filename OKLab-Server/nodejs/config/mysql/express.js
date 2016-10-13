module.exports = function(){
  var express = require('express');
  var session = require('express-session');
  var MySQLStore = require('express-mysql-session')(session);
  var path = require('path');
  var favicon = require('serve-favicon');
  var logger = require('morgan');
  var cookieParser = require('cookie-parser');
  var bodyParser = require('body-parser');

  var app = express();
  // view engine setup
  app.locals.pretty = true;
  app.set('views', './views');
  // app.set('views', path.join(__dirname, 'views'));
  app.set('view engine', 'ejs');

  // uncomment after placing your favicon in /public
  //app.use(favicon(path.join(__dirname, 'public', 'favicon.ico')));
  app.use(logger('dev'));
  app.use(bodyParser.json());
  app.use(bodyParser.urlencoded({ extended: false }));
  app.use(cookieParser());
  app.use(require('stylus').middleware(path.join(__dirname, 'public')));
  // app.use(express.static(path.join(__dirname, 'uploads')));
  app.use('/files', express.static('files'));

  app.use(session({
    secret: '1234DSFs@adf1234!@#$asd',
    resave: false,
    saveUninitialized: true,
    store:new MySQLStore({
      host     : '10.23.51.124',
      port     : '3306',
      user     : 'root',
      password : 'gytjd66',
      database : 'o2'
    })
  }));
  return app;
}
