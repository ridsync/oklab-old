module.exports = function(){
  var route = require('express').Router();
  var Post = require('../config/mongodb/models/Post');
  var async    = require('async');
  var moment = require('moment');

  // board main
  route.get('/', function(req, res){
    console.log("[board] session =  "+ JSON.stringify(req.session) );

    var page = Math.max(1,req.query.page)>1? parseInt(req.query.page):1;
    var limit = Math.max(1,req.query.limit)>1?parseInt(req.query.limit):10;

    async.waterfall([function(callback){ // notices
        Post.find({postType:"notice"}).populate("author").exec(function (err,notices) {
          if(err) callback(err);
          callback(null, notices);
        });
      },function(notices, callback){ // paging
        Post.count({postType:"normal"},function(err,count){
          if(err) callback(err);
          skip = (page-1)*limit;
          maxPage = Math.ceil(count/limit);
          callback(null, skip, maxPage, count, notices);
        });
      }, function (skip, maxPage, count, notices, callback) { // posts
        Post.find({postType:"normal"}).populate("author").sort('-createdAt').skip(skip).limit(limit).exec(function (err,posts) {
          if(err) callback(err);
          return res.render('board/list',{moment:moment, notices:notices, posts:posts,
            user:req.user, page:page, maxPage:maxPage, totalCount:count,
            urlQuery:req._parsedUrl.query, postsMessage:req.flash("postsMessage")[0]
          });
        });
      }],function(err){
        if(err) return res.json({success:false, message:err});
      })
  });

  // board post add view
  route.get('/add' , isLoggedIn , function(req, res, next){
      res.render('board/add');
  });
  // Search
  route.get('/search' , function(req, res){
      var where = req.query.where;
      var keyword = req.query.keyword;
      console.log("[board] search keyword =  "+ keyword);
      var page = Math.max(1,req.query.page)>1? parseInt(req.query.page):1;
      var limit = Math.max(1,req.query.limit)>1?parseInt(req.query.limit):10;

      var queryKey = {subject:{'$regex': keyword}};
      if(where === 'subject_or_content'){
        queryKey = {$or : [{subject:{'$regex': keyword}}, {content:{'$regex': keyword}}]};
      } else if(where === 'writer_name'){
        queryKey = {author:{$elemMatch: {userName:{'$regex': keyword}}}};
      } else if(where === 'writer_id'){
        queryKey = {author:{$elemMatch: {useId:{'$regex': keyword}}}};
      } else if(where === 'content'){
        queryKey = {content:{'$regex': keyword}};
      } else if(where === 'subject'){
        queryKey = {subject:{'$regex': keyword}};
      } else {
        queryKey = {tags:{'$regex': keyword}};
      }
          console.log("[board] search queryKey =  "+ JSON.stringify(queryKey) );
      async.waterfall([function(callback){ // paging
          Post.count(queryKey,function(err,count){
            console.log("[board] search count =  "+ count);
            if(err) callback(err);
            skip = (page-1)*limit;
            maxPage = Math.ceil(count/limit);
            callback(null, skip, maxPage, count);
          });
        }, function (skip, maxPage, count, callback) { // posts
          Post.find(queryKey).populate("author").sort('-createdAt').skip(skip).limit(limit).exec(function (err,posts) {
            if(err) callback(err);
            return res.render('board/list',{moment:moment,notices:null, posts:posts,
              user:req.user, page:page, maxPage:maxPage, totalCount:count,
              urlQuery:req._parsedUrl.query, postsMessage:req.flash("postsMessage")[0]
            });
          });
        }],function(err){
          if(err) return res.json({success:false, message:err});
        })

  });

// board post add
  route.post('/add' , isLoggedIn , function(req, res){
      req.body.post.postType = 'normal';
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
    res.redirect('/auth/notAuth');
  }
  return route;
}
