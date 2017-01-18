module.exports = function(){
  var multer = require('multer');
  var gConst = require('./gconst');
  var path = require('path');
  
  var _storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, path.join(gConst.DIR_PUBLIC_ROOT, gConst.DIR_UPLOAD) )
    },
    filename: function (req, file, cb) {
      console.log(file);
      cb(null, file.originalname + "_" + new Date().toDateString());
    }
  })
 //  var maxSize = 2 * 1000 * 1000;
 //  var upload = multer({
 //    storage: _storage ,
 //    limits: { fileSize: maxSize }
 // })
 var upload = multer({ storage: _storage })
  return upload;
}
