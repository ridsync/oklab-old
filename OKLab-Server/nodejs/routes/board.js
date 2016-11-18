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

    renderPostsList(req, res, false)
  });

  // Search
  route.get('/search' , function(req, res){
      var where = req.query.where;
      var keyword = req.query.keyword;
      console.log("[board] search keyword =  "+ keyword);
      var page = Math.max(1,req.query.page)>0? parseInt(req.query.page):1;
      var limit = Math.max(1,req.query.limit)>0?parseInt(req.query.limit):10;

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
              return res.render('board/list',{notices:null, posts:posts,
                page:page, maxPage:maxPage, totalCount:count,
                moment:moment ,user:req.user, urlQuery:req._parsedUrl.query, postsMessage:req.flash("postsMessage")[0]
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
      console.log("add/post new post data =  "+ req.body.post);
      Post.create(req.body.post, function (err,post) {
        if(err) return res.json({success:false, message:err});
        res.redirect('/board');
      });
  });

  // 글상세 post detail
  route.get('/detail/:postId' , function(req, res){
    var postId = req.params.postId;
    var isFocusComment = req.query.isFocusComment;

    // hits 조회수증가여부 쿠키 postId 체크.
    var incHitsCount = 1;
    var hitIds = req.cookies.hitIds;
    if(hitIds) incHitsCount = hitIds.indexOf(postId) >= 0 ? 0 : 1;

    // 글정보 조회 및 hit 증가
    Post.findOneAndUpdate({ _id: postId }, {$inc: {hits:incHitsCount}},{ new: true })
      .populate('author').exec(function (err,post) {
      if(err) return res.json({success:false, message:err});

      // hits 조회한 postId 쿠키저장.
      var arHitIds = [];
      if(hitIds) arHitIds = arHitIds.concat(hitIds);
      if(incHitsCount == 1) arHitIds.push(postId);
      res.cookie('hitIds', arHitIds, { maxAge: 10 * 60 * 1000 , httpOnly: true});

      // 댓글리스트 조회
      Comment.find({_id: { $in : post.comments } }).populate('author').exec(function (err,comments) {
            if(err) return res.json({success:false, message:err});

            renderPostsList(req, res , true , post, comments ,isFocusComment)
        });
    });
  });

  //글수정,삭제  //TODO isLoggedIn <- 자신의 글만 허용되도록하느것도 필요하다
  route.get('/edit/post/:postId' , isLoggedIn, function(req, res){
    var postId = req.params.postId;
    var page = req.query.page;

    Post.findOne({_id:postId}).populate('author').exec(function (err,post) {
      if(err) return res.json({success:false, message:err});
      console.log("detail post =  "+ post);

      return res.render("board/edit",{post:post,page:page,
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

          return res.redirect('/board/detail/'+ postId + '?page='+ req.query.page);
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
      var page = req.query.page;
      req.body.comment.author = req.user._id;
      Comment.create(req.body.comment, function (err,comment) {
        if(err) return res.json({success:false, message:err});

        Post.findOneAndUpdate({ _id: postId }, { $push: { comments: comment._id}},{ new: true })
          .populate('author').exec(function(err,post){
          if (err) return res.json({success:false, message:err});

          var commentCount = post.comments.length;
          res.redirect('/board/detail/'+postId + '?page='+ page  + '&isFocusComment=true');
        });

      });
  });

  route.put('/edit/comment/:commentId' , isLoggedIn, function(req, res){
    var commentId = req.params.commentId;
    var postId = req.body.postId;
    var newContent = req.body.comment_content;
    var page = req.query.page;
    console.log('commentId - ' + commentId);
    console.log('postId - ' + postId);
    console.log('newContent - ' + newContent);
    Comment.findOneAndUpdate({ _id: commentId }, { $set: { content: newContent }},{ new: true })
      .populate('author').exec(function(err,comment){
      if (err) return res.json({success:false, message:err});

      res.redirect('/board/detail/'+postId + '?isFocusComment=true' +'&page='+ page);

      // Post.findOne({ _id: postId }).exec(function(error, post) {
      //     res.render('board/detail', {isFocusComment:true, post:post, commentCount:commentCount,notices:null,posts:null, maxPage:0, page:1,
      //       moment:moment, user:req.user, postsMessage:req.flash("postsMessage")[0]
      //     });
      // });

    });
  });

  // for ajax
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

  function renderPostsList(req , res , isDetailView, post , comments , isFocusComment){

    var page = Math.max(1,req.query.page)>0?parseInt(req.query.page):1;
    var limit = Math.max(1,req.query.limit)>0?parseInt(req.query.limit):10;

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
          if(isDetailView){ // 디테일 화면용 render
            return res.render("board/detail",{post:post,comments:comments,isFocusComment:isFocusComment,
              notices:notices,posts:posts, maxPage:maxPage, page:page,totalCount:count,
              moment:moment, user:req.user, postsMessage:req.flash("postsMessage")[0]
            });
          } else {
            return res.render('board/list',{notices:notices, posts:posts, page:page, maxPage:maxPage, totalCount:count,
              moment:moment, user:req.user, urlQuery:req._parsedUrl.query, postsMessage:req.flash("postsMessage")[0]
            });
          }

        });
      }],function(err){
        if(err) return res.json({success:false, message:err});
      })
  }

  return route;
}
