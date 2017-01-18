var multer = require('multer');
var gConst = require('./gconst');
var path = require('path');
var moment = require('moment');

var init = function () {
    var _storage = multer.diskStorage({
      destination: function (req, file, cb) {
        var strPath = req.path;
        // TODO multer는 각 디렉토리 미리 생성이 필요하네...
        if( strPath.includes('photoUp') ){ // profile img
            strPath = gConst.DIR_UPLOAD_PROFILE;
        } else if( strPath.includes('upImage') ){ // post img
            strPath = gConst.DIR_UPLOAD_POSTPIC;
        } else {
            strPath = gConst.DIR_UPLOAD;
        }
        cb(null, path.join(gConst.DIR_PUBLIC_ROOT, strPath) )
      },
      filename: function (req, file, cb) {
        console.log(file);
        var strPath = req.path;
        // TODO multer는 각 디렉토리 미리 생성이 필요하네...
        if( strPath.includes('photoUp') ){ // profile img
              strPath = 'profile_' + req.user.userId + path.extname(file.originalname);
        } else if( strPath.includes('upImage') ){ // post img
            strPath = 'post_' + moment.format('YYMMDDHHmmss') + path.extname(file.originalname);
        } else {
            strPath = moment().format('YYMMDDHHmmss') + "_" + file.originalname;
        }
        cb(null, strPath );
      }
    })
   //  var maxSize = 2 * 1000 * 1000;
   //  var upload = multer({
   //    storage: _storage ,
   //    limits: { fileSize: maxSize }
   // })
   var upload = multer({ storage: _storage })
    return upload;
  };

module.exports = init();
