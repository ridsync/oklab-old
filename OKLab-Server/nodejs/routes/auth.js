module.exports = function(passport){
  var bkfd2Password = require("pbkdf2-password");
  var hasher = bkfd2Password();
  var conn = require('../config/mysql/db')();
  var route =  require('express').Router();

  /* GET users listing. */
  route.post('/register', function(req, res){
      hasher({password:req.body.password}, function(err, pass, salt, hash){
        var user = {
          authId:'local:'+req.body.username,
          username:req.body.username,
          password:hash,
          salt:salt,
          displayName:req.body.displayName,
          email:req.body.email
        };
        var sql = 'INSERT INTO users SET ?';
        conn.query(sql, user, function(err, results){
          if(err){
            console.log(err);
            res.status(500);
          } else {
            req.login(user, function(err){
              req.session.save(function(){
                res.redirect('/welcome');
              });
            });
          }
        });
      });
    });
  route.get('/register', function(req, res){
    var sql = 'SELECT id,title FROM topic';
    conn.query(sql, function(err, topics, fields){
      res.render('auth/register', {topics:topics});
    });
  });

  route.get('/welcome', function(req, res){
    if(req.user && req.user.displayName) {
      res.send(`
        <h1>Hello, ${req.user.displayName}</h1>
        <a href="/auth/logout">logout</a>
      `);
    } else {
      res.send(`
        <h1>Welcome</h1>
        <ul>
          <li><a href="/auth/login">Login</a></li>
          <li><a href="/auth/register">Register</a></li>
        </ul>
      `);
    }
  });

  route.get('/login', function(req, res){
    var sql = 'SELECT id,title FROM topic';
    conn.query(sql, function(err, topics, fields){
      res.render('auth/login', {topics:topics});
    });
  });
  route.post(
    '/login',
    passport.authenticate(
      'local',
      {
        successRedirect: '/topic',
        failureRedirect: '/auth/login',
        failureFlash: false
      }
    )
  );
  route.get('/logout', function(req, res){
    req.logout();
    req.session.save(function(){
      res.redirect('/topic');
    });
  });
  return route;
}
