var mongoose = require('mongoose');

var postSchema = mongoose.Schema({
  postType: {type:String, required:true}, // notice , normal , gallery
  title: {type:String, required:true},
  body: {type:String, required:true},
  imgPath: String ,
  author: {type:mongoose.Schema.Types.ObjectId, ref:'user', required:true},
  createdAt: {type:Date, default:Date.now},
  updatedAt: Date ,
  hits: {type:Number, required:true},
  comments: [{type:mongoose.Schema.Types.ObjectId, ref:'reply'}]
});

postSchema.methods.getCreatedDate = function () {
  var date = this.createdAt;
  return date.getFullYear() + "-" + get2digits(date.getMonth()+1)+ "-" + get2digits(date.getDate());
};

postSchema.methods.getCreatedTime = function () {
  var date = this.createdAt;
  return get2digits(date.getHours()) + ":" + get2digits(date.getMinutes())+ ":" + get2digits(date.getSeconds());
};
function get2digits(num){
  return ("0" + num).slice(-2);
}
var Post = mongoose.model('post',postSchema);
module.exports = Post;
