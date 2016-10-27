
module.exports = function(){

  // var host = process.env.MONGO_DB;
  // var host = process.env.USER;
  // var host = process.env.PASSWORD;
  // var host = process.env.INSTANCE;

  var mysql = require('mysql');
  var conn = mysql.createConnection({
    host     : '10.23.51.124',
    user     : 'root',
    password : 'gytjd66',
    database : 'o2'
  });
  conn.connect();
  return conn;
}
