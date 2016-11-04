module.exports = function(){
  var route = require('express').Router();
  var multer = require('../config/multer')();
  var fs = require('fs');
  var path = require('path');

  route.get('/view', function(req, res){
    res.render('board/upload');
  });

  route.post('/multer', multer.single('userfile'), function(req, res){
    console.log(req.file)
    res.send('Uploaded : '+req.file.filename);
  });

  route.get('/download/:file(*)', function(req, res){
    var fileName = req.params.file;
    console.log('download filename = ' + fileName)
    var file = 'public/images/' + fileName;
    res.download(file);
  });

  route.get('/sendfile/:file(*)', function(req, res){
    var fileName = req.params.file;
    console.log(fileName)
    var file = path.resolve( 'public/images/' , fileName);
    res.sendFile(file);
  });

// bodyparser호환이 불가능. express.bodyparser를 사용해야함
  // route.post('/stream', function(req, res){
  //   console.log(req.file)
  //   // bodyParser 없을 경우 req.files 에러 발생.
  //   fs.readFile(req.files.userfile.path, function (error, data) {
  //   // 저장할 파일 경로를 지정 합니다.
  //   var filePath = 'uploads/' + req.files.userfile.name;
  //   // 파일 저장 및 에러처리
  //     fs.writeFile(filePath, data, function (error) {
  //       if (error) {
  //       throw err;
  //       } else {
  //       res.send('Uploaded : '+req.file.filename);
  //       }
  //     });
  //   });
  //
  // });

  return route;
}
