module.exports = function(){
  var express = require('express');
  // var session = require('express-session'); // 별도 세션js로 뺌
  // var Store = require('express-session').Store;
  // var MongooseStore = require('mongoose-express-session')(Store);
  // var db = require('./mongodb/db')();
  var session 	= require('./session');
  var path = require('path');
  var flash = require('connect-flash');
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
  app.use(bodyParser.urlencoded({ extended: true }));
  app.use(cookieParser());
  app.use(express.static('public'));
  app.use(require('stylus').middleware('public'));
  app.use(flash());
  app.use(session);
  // app.use(session({
  //   secret: '1234DSFs@adf1234!@#$asd',
  //   resave: false,
  //   saveUninitialized: true,
  //   store:new MongooseStore({connection: db}),
  //   // cookie: { maxAge: 10 * 60 * 1000 } // TODO maxAge는 로그인시 필수네? expires가 필요없나 ?
  //   cookie: { maxAge: 9 * 60 * 1000 }
  // }));
  return app;
}
