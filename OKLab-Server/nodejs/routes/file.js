module.exports = function(){

  var gConst = require('../config/gconst');
  var route = require('express').Router();
  var multer = require('../config/multer');
  var fs = require('fs');
  var path = require('path');
  var User     = require('../config/mongodb/models/User');
  // var rootPath = require('app-root-path');

  // profile.ejs
  route.post('/photoUp', multer.single('userPhoto'), function(req, res){
    console.log(req.file)
    var filepath = path.join(gConst.DIR_UPLOAD_PROFILE, req.file.filename);
    User.findOneAndUpdate({userId:req.user.userId},
        { $set: {picture: filepath}}, { new: true })
    .exec(function (err,user) {
        if(err) return res.json({success:false, message:err});
        console.log('[profile update] photoUp Success User = ' + JSON.stringify(user) );
        res.send(filepath);
    });

  });

  // add.ejs
  route.post('/upImage', multer.single('imgFile'), function(req, res){
    console.log(req.file)
    var filepath = path.join(gConst.DIR_UPLOAD_POSTPIC,req.file.filename);
    res.send(filepath);
  });

  route.delete('/delete', function(req, res){
    var filepath = req.body.uploads;
    // console.log(filepath);
    // console.log(__dirname);
    // console.log(path.join(rootPath.path, gConst.DIR_PUBLIC_ROOT  + filepath));
    fs.unlink( path.join(gConst.DIR_PUBLIC_ROOT,filepath) , function (err) {
      if (err) throw err;

      res.send('File Delete Completed');
      console.log('successfully deleted  - ' + filepath);
    });

  });

  // file.ejs
  route.get('/fileio', function(req, res){
    console.log(req.file)
    res.render('board/file');
  });

  route.post('/multer', multer.single('userfile'), function(req, res){
    console.log(req.file)
    res.send('Uploaded : '+req.file.filename);
  });

  route.post('/upImageMulti', multer.array('userfile'), function(req, res){
    console.log(req.files)
    res.send({"retVal":1,"retMsg":"SUCCESS"});
  });

  // Download file
  route.get('/download/:file(*)', function(req, res){
    var fileName = req.params.file;
    console.log('download filename = ' + fileName)
    var file = path.join(gConst.DIR_PUBLIC_ROOT, 'images', fileName);
    res.download(file);
  });

  route.get('/sendfile/:file(*)', function(req, res){
    var fileName = req.params.file;
    console.log('sendfile filename = ' +fileName)
    var file = path.resolve(gConst.DIR_PUBLIC_ROOT, 'images', fileName);
    res.sendFile(file);
  });

  return route;
}
