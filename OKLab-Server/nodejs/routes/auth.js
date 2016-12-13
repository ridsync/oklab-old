module.exports = function(passport){
  var route    = require('express').Router();
  var mongoose = require('mongoose');
  var User     = require('../config/mongodb/models/User');
  var async    = require('async');
  var request  = require('request');

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

  // with ajax
  route.post('/register', function(req, res){

    if(req.body['g-recaptcha-response'] === undefined || req.body['g-recaptcha-response'] === '' || req.body['g-recaptcha-response'] === null) {
      return res.json({"responseCode" : 1,"responseDesc" : "Google Captcha를 체크하세요 !!"});
    }
    // Put your secret key here.
    var secretKey = "6LdAowwUAAAAAO_AE45BnXXxMGpi_lC-McogXttb";
    // req.connection.remoteAddress will provide IP address of connected user.
    var verificationUrl = "https://www.google.com/recaptcha/api/siteverify?secret=" + secretKey + "&response=" + req.body['g-recaptcha-response'] + "&remoteip=" + req.connection.remoteAddress;
    // Hitting GET request to the URL, Google will respond with success or error scenario.
    request(verificationUrl,function(error,response,body) {
      body = JSON.parse(body);
      // Success will be true or false depending upon captcha validation.
      if(body.success !== undefined && !body.success) {
        return res.json({"responseCode" : 1,"responseDesc" : "Failed captcha verification !!"});
      }
      // res.json({"responseCode" : 0,"responseDesc" : "Sucess"});

      var newUser = req.body.user;
      newUser.userLevel = 1; // 회원가입 기본레벨 부여
      User.create(newUser, function (err,user) {
        if(err) return res.json({success:false, message:err});
        req.flash("loginMessage","Thank you for registration!");
        // create에 성공하면 welcome
        // 페이지로 이동 redirct  유저정보 로그인상태로
        console.log('[register] Regist Success body = ' + body.success);
        // res.redirect('/auth/welcome');
        res.json({"responseCode" : 0,"responseDesc" : "Sucess"});
      });
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
        res.render('auth/login',{title: 'Login Page', userId:req.flash("userId")[0], resMessage:req.flash("noAuth")});
      }
    });

  // for ajax
  route.get('/loginResult', function(req, res){
    var loginError = req.flash('loginError');
    if(isLoggedIn(req, res)){
      console.log('[login] login success userId = ' + req.flash("userId")[0]);
      res.send(null);
    } else { // TODO 로그인에러 처리(아이디암호확인 메세지전달등)
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

  route.get('/profile', function(req, res){
    if(isLoggedIn(req ,res)){
      res.render('auth/profile',{title: 'User Profle', user:req.user});
    } else {
      res.redirect('/auth/login');
    }
  });

  route.get('/profile/update', function(req, res){
    if(isLoggedIn(req ,res)){
      res.render('auth/profileUpdate',{title: 'User Profle Update', user:req.user});
    } else {
      res.redirect('/auth/notAuth');
    }
  });

  route.post('/profile/update', function(req, res){
    if(isLoggedIn(req ,res)){
        var newPassword = User.hashPassword(req.body.user.password);
        var newUserName = req.body.user.userName;
        var newEmail = req.body.user.email;
        var newPhone = req.body.user.phone;
        User.findOneAndUpdate({userId:req.user.userId},
            { $set: { password: newPassword , userName:newUserName, email:newEmail, phone:newPhone}}, { new: true })
        .exec(function (err,user) {
            if(err) return res.json({success:false, message:err});
            console.log('[profile update] Success User = ' + JSON.stringify(user) );

            req.login(user, function(err) {
                if (err) return next(err);
                res.redirect('/auth/profile');
            });
        });
    } else {
      res.redirect('/auth/notAuth');
    }
  });

  route.get('/notAuth', function(req, res, next) {
    // res.render('auth/notauth');
    req.flash('noAuth', '권한이 없습니다. 로그인 후 다시 시도하세요.')
    res.redirect('/auth/login');
  });

  function isLoggedIn(req, res) {
    if (req.isAuthenticated()){
      return true;
    }
    return false;
  }

  return route;
};
