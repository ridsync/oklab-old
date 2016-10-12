
module.exports = function(){
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
