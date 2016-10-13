module.exports = function(){
  var multer = require('multer');
  var _storage = multer.diskStorage({
    destination: function (req, file, cb) {
      cb(null, 'uploads/')
    },
    filename: function (req, file, cb) {
      console.log(req);
      cb(null, file.originalname + "_" + new Date().toDateString());
    }
  })
  var upload = multer({ storage: _storage })
  return upload;
}
