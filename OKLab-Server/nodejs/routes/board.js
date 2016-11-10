module.exports = function(){
  var route = require('express').Router();
  var Post = require('../config/mongodb/models/Post');

  // board main
  route.get('/', function(req, res){
    res.render('board/list');
  });

  // board post add view
  route.get('/add' , isLoggedIn , function(req, res, next){
      res.render('board/add');
  });
// board post add
  route.post('/add' , isLoggedIn , function(req, res){
      req.body.post.postType = 'pt1';
      req.body.post.author = req.user._id;
      req.body.post.hits = 0;
      console.log("add/ new post data =  "+ req.body.post);
      Post.create(req.body.post, function (err,post) {
        if(err) return res.json({success:false, message:err});
        res.redirect('/board');
      });
  });

  route.put('/edit/:postId' , function(req, res){
      console.log("add - "+req.params);
  });

  route.delete('/delete/:postId' , function(req, res){
      console.log("add - "+req.params);
  });

  // board post detail
  route.get('/:postId' , function(req, res){
      console.log("readPost - "+req.params);
  });

  function isLoggedIn(req, res, next) {
    if (req.isAuthenticated()){
      return next();
    }
    req.flash("postsMessage","Please login first.");
    res.redirect('/auth/notlogged');
  }
  return route;
}
