module.exports = function(){
  var route = require('express').Router();
  var multer = require('../config/multer')();
  var fs = require('fs');
  var path = require('path');
  // var rootPath = require('app-root-path');

  route.get('/upload', function(req, res){
    console.log(req.file)
    res.render('board/upload');
  });

  route.post('/upImage', multer.single('imgFile'), function(req, res){
    console.log(req.file)
    res.send(req.file.filename);
  });

  route.post('/upImageMulti', multer.array('userfile'), function(req, res){
    console.log(req.files)
    res.send({"retVal":1,"retMsg":"SUCCESS"});
  });

  route.delete('/delete', function(req, res){
    var filename = req.body.uploads;
    // console.log(filename);
    // console.log(__dirname);
    // console.log(path.join(rootPath.path, 'public/uploads/' + filename));
    fs.unlink(path.join('', 'public/uploads/' + filename), function (err) {
      if (err) throw err;
      console.log('successfully deleted  - ' + filename);
    });
    res.send('File Delete Completed');
  });

  return route;
}
