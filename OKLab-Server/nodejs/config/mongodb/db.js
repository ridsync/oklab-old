
module.exports = function(){

  // var host = process.env.MONGO_DB;
  // var host = process.env.USER;
  // var host = process.env.PASSWORD;
  // var host = process.env.INSTANCE;

var mongoose = require('mongoose');
// database
mongoose.connect('mongodb://10.23.51.124:27017/oklab_nodejs_board');
// mongoose.connect(process.env.MONGO_DB);
var db = mongoose.connection;
db.once("open",function () {
  console.log("MongoDB connected!");
});
db.on("error",function (err) {
  console.log("MongoDB ERROR :", err);
});

  return mongoose;
}
