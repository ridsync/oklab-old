module.exports = function(){

  var gConst = require('../gconst');
  var mongoose = require('mongoose');

  // database
  mongoose.connect(gConst.MONGODB_ADDR);
  // mongoose.connect(process.env.MONGO_DB);

  mongoose.Promise = global.Promise;

  var db = mongoose.connection;
  db.once("open",function () {
    console.log("MongoDB connected!");
  });
  db.on("error",function (err) {
    console.log("MongoDB ERROR :", err);
  });

return mongoose;
}

// module.exports = { mongoose
// };
