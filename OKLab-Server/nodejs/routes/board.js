module.exports = function(){
  var route = require('express').Router();
  var Post = require('../config/mongodb/models/Post');
  var Comment = require('../config/mongodb/models/Comment');
  var User = require('../config/mongodb/models/User');
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

  // Search
  route.get('/search' , function(req, res){
      var where = req.query.where;
      var keyword = req.query.keyword;
      console.log("[board] search keyword =  "+ keyword);
      var page = Math.max(1,req.query.page)>1? parseInt(req.query.page):1;
      var limit = Math.max(1,req.query.limit)>1?parseInt(req.query.limit):10;

      var queryKey = {};
      if(where === 'subject_or_content'){
        queryKey = {$or : [{subject:{'$regex': keyword}}, {content:{'$regex': keyword}}]};
      } else if(where === 'content'){
        queryKey = {content:{'$regex': keyword}};
      } else if(where === 'subject'){
        queryKey = {subject:{'$regex': keyword}};
      } else if(where === 'writer_name'){ // TODO 이름 아이디검색어렵네
        queryKey = {userName:keyword};
      } else if(where === 'writer_id'){
        queryKey = {userId:keyword};
        // queryKey = {author:{$elemMatch: {useId:{'$regex': keyword}}}};
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
          Post.find(queryKey)
            .populate('author')
            .sort('-createdAt').skip(skip).limit(limit)
            .exec(function (err,posts) {
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

  // board post add view
  route.get('/add/post' , isLoggedIn , function(req, res, next){
      res.render('board/add');
  });

// board post add
  route.post('/add/post' , isLoggedIn , function(req, res){
      req.body.post.postType = 'normal';
      req.body.post.author = req.user._id;
      req.body.post.hits = 0;
      console.log("add/post new post data =  "+ req.body.post);
      Post.create(req.body.post, function (err,post) {
        if(err) return res.json({success:false, message:err});
        res.redirect('/board');
      });
  });

  // 글상세 post detail
  route.get('/:postId/:page' , function(req, res){
    var postId = req.params.postId;
    var page = req.params.page;

    Post.findOne({_id:postId}).populate('author').exec(function (err,post) {
      if(err) return res.json({success:false, message:err});

      Comment.find({_id: { $in : post.comments } }).populate('author').exec(function (err,comments) {
            if(err) return res.json({success:false, message:err});
            if(!comments) return res.json({success:false, message:'comments null'});

            var commentCount = post.comments.length;

            return res.render("board/detail",{post:post,comments:comments, commentCount:commentCount,
              isFocusComment:false,notices:null,posts:null, maxPage:0, page:page,
              moment:moment, user:req.user, postsMessage:req.flash("postsMessage")[0]
            });

        });
    });
  });

  //글수정,삭제  //TODO isLoggedIn <- 자신의 글만 허용되도록하느것도 필요하다
  route.get('/edit/post/:postId' , isLoggedIn, function(req, res){
    var postId = req.params.postId;

    Post.findOne({_id:postId}).populate('author').exec(function (err,post) {
      if(err) return res.json({success:false, message:err});
      console.log("detail post =  "+ post);

      return res.render("board/edit",{post:post,
        moment:moment, user:req.user, postsMessage:req.flash("postsMessage")[0]
      });
    });
  });

  route.post('/edit/post' , isLoggedIn, function(req, res){
      var postId = req.body.post.postId;
      var subject = req.body.post.subject;
      var content = req.body.post.content;
      var tags = req.body.post.tags;

      Post.findOneAndUpdate({ _id: postId }, { $set: { subject: subject , content: content, tags: tags }},{ new: true })
        .populate('author').exec(function(err,post){
        if (err) return res.json({success:false, message:err});

        var commentCount = post.comments.length;
        res.render('board/detail', {post:post, commentCount:commentCount,notices:null,posts:null, maxPage:0, page:1,
          moment:moment, user:req.user, postsMessage:req.flash("postsMessage")[0]
        });
      });
  });

  route.delete('/delete/post/:postId' , isLoggedIn, function(req, res){
      var postId = req.params.postId;


      // TODO 글삭제시, 댓글도 삭제해야함  !!

      Post.findOneAndRemove({_id:postId, author:req.user._id}, function (err,post) {
        if(err) return res.json({success:false, message:err});
        if(!post) return res.json({success:false, message:"글 삭제를 실패했습니다."});
        res.json({success:true, message:'해당 글을 삭제했습니다.'});
      });
  });

  // 댓글 추가 , 수정 , 삭제
  route.post('/add/comment/:postId' , isLoggedIn , function(req, res){
      var postId = req.params.postId;
      req.body.comment.author = req.user._id;
      req.body.comment.hits = 0;
      Comment.create(req.body.comment, function (err,comment) {
        if(err) return res.json({success:false, message:err});

        Post.findOneAndUpdate({ _id: postId }, { $push: { comments: comment._id}},{ new: true })
          .populate('author').exec(function(err,post){
          if (err) return res.json({success:false, message:err});

          var commentCount = post.comments.length;
          res.redirect('/board/'+postId +'/'+ 1)
        });

      });
  });

  route.put('/edit/comment/:commentId' , isLoggedIn, function(req, res){
    var commentId = req.params.commentId;
    var postId = req.body.postId;
    var newContent = req.body.comment_content;
    console.log('commentId - ' + commentId);
    console.log('postId - ' + postId);
    console.log('newContent - ' + newContent);
    Comment.findOneAndUpdate({ _id: commentId }, { $set: { content: newContent }},{ new: true })
      .populate('author').exec(function(err,comment){
      if (err) return res.json({success:false, message:err});

      res.redirect('/board/'+postId +'/'+ 1);

      // Post.findOne({ _id: postId }).exec(function(error, post) {
      //     res.render('board/detail', {isFocusComment:true, post:post, commentCount:commentCount,notices:null,posts:null, maxPage:0, page:1,
      //       moment:moment, user:req.user, postsMessage:req.flash("postsMessage")[0]
      //     });
      // });

    });
  });

  route.delete('/delete/comment/:commentId' , isLoggedIn, function(req, res){
    var postId = req.body.postId;
    var commentId = req.params.commentId;

    Post.findOneAndUpdate({ _id: postId }, { $pull: { comments: commentId} },{ new: true })
      .populate('author').exec(function(err,post){
      if (err) return res.json({success:false, message:err});

      Comment.findOneAndRemove({_id:commentId, author:req.user._id}, function (err,post) {
        if(err) return res.json({success:false, message:err});
        if(!post) return res.json({success:false, message:"댓글 삭제를 실패했습니다."});
        res.json({success:true, message:'해당 댓글을 삭제했습니다.'});
      });
    });


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
