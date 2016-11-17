module.exports = function(){
  var multer = require('multer');
  var gConst = require('./gconst');

  var _storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, gConst.UPLOAD_DIR)
    },
    filename: function (req, file, cb) {
      console.log(file);
      cb(null, new Date().getTime() + "_" + file.originalname);
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
