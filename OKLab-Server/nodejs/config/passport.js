module.exports = function(app){
  var passport = require('passport');
  var LocalStrategy = require('passport-local').Strategy;
  // var FacebookStrategy = require('passport-facebook').Strategy;
  var User          = require('./mongodb/models/User');

  app.use(passport.initialize());
  app.use(passport.session());
  passport.serializeUser(function(user, done) {
    console.log('serializeUser', user);
    done(null, user);
  });
  passport.deserializeUser(function(user, done) {
    console.log('deserializeUser', user);
    // done(null, user);
    User.findById(user._id, function (err, user) {
        console.log('deserializeUser findById ', user);
			done(err, user);
		});
    // User.findOne({
    //     where: {
    //         userId: user.userId
    //     }
    // }).then(function( user ){
    //   console.log('deserializeUser findOne  user = ' + user);
    //     done(null, user);
    // }).catch(function( err ){
    //     done(err);
    // });
  });
  passport.use('local-login',
    new LocalStrategy({
        usernameField : 'userId',
        passwordField : 'password',
        passReqToCallback : true
      },
      function(req, userId, password, done) {
        User.findOne({ 'userId' :  userId }, function(err, user) {
          if (err) return done(err);
          console.log('[passport] User Logining  err = ' + err);
          if (!user){
              req.flash("userId", req.body.userId);
              return done(null, false, req.flash('loginError', '아이디 또는 비밀번호가 올바르지 않습니다.'));
          }
          if (!user.authenticate(password)){
              req.flash("userId", req.body.userId);
              return done(null, false, req.flash('loginError', '아이디 또는 비밀번호가 올바르지 않습니다.'));
          }
          console.log('[passport] User Login Success  user = ' + user.userId);
          req.flash('postsMessage', 'Welcome '+user.userName+'!');
          return done(null, user);
        });
      }
    )
  );

  return passport;
}
