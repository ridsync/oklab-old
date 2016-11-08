module.exports = function(){
  var route = require('express').Router();
  var conn = require('../config/mysql/db')();

  route.get(['/', '/:id'], function(req, res){
    var sql = 'SELECT id,title FROM topic';
    conn.query(sql, function(err, topics, fields){
      var id = req.params.id;
      if(id){
        var sql = 'SELECT * FROM topic WHERE id=?';
        conn.query(sql, [id], function(err, topic, fields){
          if(err){
            console.log(err);
            res.status(500).send('Internal Server Error');
          } else {
            res.render('board/list', {topics:topics, topic:topic[0], user:req.user});
          }
        });
      } else {
        res.render('board/list', {topics:topics, user:req.user});
      }
    });
  });

  function isLoggedIn(req, res, next) {
    if (req.isAuthenticated()){
      return next();
    }
    req.flash("postsMessage","Please login first.");
    res.redirect('/');
  }
  return route;
}
