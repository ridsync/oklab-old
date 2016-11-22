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
    var newUser = req.body.user;
    newUser.userLevel = 1; // 회원가입 기본레벨 부여
    User.create(newUser, function (err,user) {
      if(err) return res.json({success:false, message:err});
      req.flash("loginMessage","Thank you for registration!");
      // create에 성공하면 welcome
      // 페이지로 이동 redirct  유저정보 로그인상태로
      res.redirect('/auth/welcome');
    });
  }); // user create

  route.post('/welcome',
     passport.authenticate(
      'local-login',
      {
        successRedirect: '/auth/welcome',
        failureRedirect: '/auth/login',
        failureFlash: true
      }
    ));

    route.get('/welcome',  function(req, res){
      // console.log('[welcome] user = ' + user);
      // req.flash('userId',user.userId);
      // req.flash('email',user.email);
      if(isLoggedIn(req ,res)){
        res.redirect('/');
      } else {
        res.render('auth/welcome', {title: 'Welcome Page'});
      }
    });

    route.get('/login', function(req, res){
      if(isLoggedIn(req ,res)){
        res.redirect('/');
      } else {
        res.render('auth/login',{title: 'Login Page', userId:req.flash("userId")[0]});
      }
    });

  route.get('/loginResult', function(req, res){
    var loginError = req.flash('loginError');
    if(isLoggedIn(req, res)){
      console.log('[login] login success userId = ' + req.flash("userId")[0]);
      res.send(null);
    } else {
      console.log('[login] login failed loginError = ' + loginError);
      res.send(loginError);
      req.flash('loginError',null);
    }
  });

  route.post('/login',
    passport.authenticate(
      'local-login',
      {
        successRedirect: '/auth/loginResult',
        failureRedirect: '/auth/loginResult',
        failureFlash: true
      }
    )
  );

  route.get('/logout', function(req, res){
    req.logout();
    req.flash("postsMessage", "Good-bye, have a nice day!");
    req.session.save(function(){
      res.redirect('back');
    });
  });

  route.get('/notAuth', function(req, res, next) {
    // res.render('auth/notauth');
    res.redirect('/auth/login');
  });

  function isLoggedIn(req, res) {
    if (req.isAuthenticated()){
      return true;
    }
    return false
  }

  return route;
}
