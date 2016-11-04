var mongoose = require('mongoose');
var bcrypt   = require("bcrypt-nodejs");

var userSchema = mongoose.Schema({
  userId: {type:String, required:true, unique:true},
  password: {type:String, required:true},
  userName: {type:String, required:true},
  email: {type:String, required:true},
  phone: {type:String},
  userLevel: {type:Number, required:true},
  createdAt: {type:Date, default:Date.now},
  expireAt: {type:Date, default:Date.now}
});
userSchema.pre("save", hashPassword);
userSchema.pre("findOneAndUpdate", function hashPassword(next){
  console.log(this._update);
  var user = this._update;
  if(!user.newPassword){
    delete user.password;
    return next();
  } else {
    user.password = bcrypt.hashSync(user.newPassword);
    return next();
  }
});

userSchema.methods.authenticate = function (password) {
  var user = this;
  return bcrypt.compareSync(password,user.password);
};
userSchema.methods.hash = function (password) {
  return bcrypt.hashSync(password);
};
var User = mongoose.model('user',userSchema);

module.exports = User;

function hashPassword(next){
  console.log("hi");
  var user = this;
  if(!user.isModified("password")){
    return next();
  } else {
    user.password = bcrypt.hashSync(user.password);
    return next();
  }
}
