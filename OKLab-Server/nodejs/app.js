var app = require('./config/mongodb/express')();
var passport = require('./config/mongodb/passport')(app);
// var app = require('./config/mysql/express')();
// var passport = require('./config/mysql/passport')(app);

// res initialize
app.use(function(req, res, next) {
  // console.log('[api common] :: params  = ' + JSON.stringify(req.params) );
  console.log('[api common] :: locals user = ' + req.user);
  res.locals.user = req.user;
  res.locals.title = 'OK MEAN Stack: Project';
  next();
});

// default routes
var main = require('./routes/main')();
var auth = require('./routes/auth')(passport);
var board = require('./routes/board')();
var file = require('./routes/file')();
// var index = require('./routes/mysql/index')();
// var auth = require('./routes/mysql/auth')(passport);
// var board = require('./routes/mysql/board')();

app.use('/', main);
app.use('/auth', auth);
app.use('/board', board);
app.use('/file', file);

// catch 404 and forward to error handler
app.use(function(req, res, next) {
  var err = new Error('Not Found');
  err.status = 404;
  // next(err);
  res.render('error404');
})

// error handlers

// development error handler
// will print stacktrace
if (app.get('env') === 'development') {
  app.use(function(err, req, res, next) {
    res.status(err.status || 500);
    res.render('error', {
      message: err.message,
      error: err
    });
  });
}

// production error handler
// no stacktraces leaked to user
app.use(function(err, req, res, next) {
  res.status(err.status || 500);
  res.render('error', {
    message: err.message,
    error: {}
  });
});


module.exports = app;
