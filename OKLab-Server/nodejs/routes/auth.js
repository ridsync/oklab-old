module.exports = function(passport){
  var route =  require('express').Router();
  var mongoose = require('mongoose');
  var User     = require('../config/mongodb/models/User');
  var async    = require('async');

  route.get('/register', function(req, res){
      res.render('auth/register', {
                                title: 'OK MEAN STACK Test : 회원가입',
                                formData: req.flash('formData')[0],
                                userIdError: req.flash('userIdError')[0],
                                emailError: req.flash('emailError')[0],
                                nicknameError: req.flash('nicknameError')[0],
                                passwordError: req.flash('passwordError')[0]
                              }
      );
  });

  // id 유효성체크 for ajax
  route.post('/register/validate/id', function(req, res, next){
    var newId = req.body.id;
    console.log('[register] newId Check = ' + newId);
    User.findOne({userId: req.body.id},
      function(err,user){
        if(err) return res.json({success:"false", message:err});

        if(user){
          console.log('[register] user = ' + user);
          res.send("duplicate");
        } else {
          console.log('[register] user = ' + user);
          res.send("validate");
        }
      }
    );
  });
// email 유효성체크 for ajax
  route.post('/register/validate/email', function(req, res, next){
    var newEmail = req.body.email;
    console.log('[register] email Check = ' + newEmail);
    User.findOne({email: req.body.email},
      function(err,user){
        if(err) return res.json({success:"false", message:err});

        if(user){
          console.log('[register] user = ' + user);
          res.send("duplicate");
        } else {
          console.log('[register] user = ' + user);
          res.send("validate");
        }
      }
    );
  });

  route.post('/register', function(req, res){
    var newUser = req.params.user;
    // 왜 user가 안넘어오지 jquery문제 ?
    console.log('[register] newUser = ' + newUser);
    console.log('[register] newUser = ' + req.params.user.email);
    newUser.userLevel = 1;
    User.create(newUser, function (err,user) {
      if(err) return res.json({success:false, message:err});
      req.flash("loginMessage","Thank you for registration!");
      // create에 성공하면 welcome
      // 페이지로 이동 redirct  유저정보 로그인상태로
      res.redirect('/welcome');
    });
  }); // user create

  route.get('/welcome', function(req, res){
    if(req.user && req.user.userId) {
      res.send(`
        <h1>Hello, ${req.user.userId}</h1>
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
    res.render('login/login',{userId:req.flash("userId")[0],email:req.flash("email")[0], loginError:req.flash('loginError'), loginMessage:req.flash('loginMessage')});
  });

  route.post(
    '/login',
    passport.authenticate(
      'local-login',
      {
        successRedirect: '/board',
        failureRedirect: '/auth/login',
        failureFlash: true
      }
    )
  );

  route.get('/logout', function(req, res){
    req.logout();
    req.flash("postsMessage", "Good-bye, have a nice day!");
    req.session.save(function(){
      res.redirect('/auth/login');
    });
  });
  return route;
}
