module.exports = function(app){
  var passport = require('passport');
  var LocalStrategy = require('passport-local').Strategy;
  // var FacebookStrategy = require('passport-facebook').Strategy;
  var User          = require('./models/User');

  app.use(passport.initialize());
  app.use(passport.session());
  passport.serializeUser(function(user, done) {
    console.log('serializeUser', user);
    done(null, user.userId);
  });
  passport.deserializeUser(function(user, done) {
    console.log('deserializeUser', user);
    User.findOne({
        where: {
            userId: user.userId
        }
    }).then(function( user ){
        done(null, user);
    }).catch(function( err ){
        done(err);
    });
    // User.findById(id, function(err, user) {
    //   done(err, user);
    // });
  });
  passport.use('local-login',
    new LocalStrategy({
        usernameField : 'userId',
        passwordField : 'password',
        passReqToCallback : true
      },
      function(req, id, password, done) {
        User.findOne({ 'userId' :  userId }, function(err, user) {
          if (err) return done(err);

          if (!user){
              req.flash("userId", req.body.userId);
              return done(null, false, req.flash('loginError', 'No user found.'));
          }
          if (!user.authenticate(password)){
              req.flash("userId", req.body.userId);
              return done(null, false, req.flash('loginError', 'Password does not Match.'));
          }
          req.flash('postsMessage', 'Welcome '+user.userName+'!');
          return done(null, user);
        });
      }
    )
  );

  return passport;
}
