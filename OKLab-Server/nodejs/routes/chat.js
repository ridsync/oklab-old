module.exports = function(){
  var Mongoose = require('mongoose');
  var Route =  require('express').Router();
  var User = require('../config/chat/user');
  var Room = require('../config/chat/room');

  Route.get('/', function(req, res, next) {
    if(isLoggedIn(req,res)){
  		res.redirect('/chat/rooms');
  	} else {
      	res.render('auth/notauth');
    }
  });

  // Rooms
  Route.get('/rooms', isAuthenticated , function(req, res, next) {
  	Room.find(function(err, rooms){
  		if(err) throw err;
      console.log("[chat] chatRoom rooms" + rooms);
  		res.render('chat/rooms', { rooms });
  	});
  });

  // Chat Room
  Route.get('/roomId/:roomId', isAuthenticated, function(req, res, next) {
  	var roomId = req.params.roomId;
    console.log("[chat] chatRoom roomId " + roomId);
  	Room.findById(roomId, function(err, room){
  		if(err) throw err;
  		if(!room){
  			return next();
  		}
  		res.render('chat/chatroom', { user: req.user, room: room });
  	});

  });

  function isLoggedIn(req, res) {
    if (req.isAuthenticated()){
      return true;
    }
    return false;
  }

  function isAuthenticated(req, res, next) {
    if (req.isAuthenticated()){
      return next();
    }
    res.redirect('/auth/notAuth');
  }

  return Route;
}
