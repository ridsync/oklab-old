var roomModel   =require('../mongodb/models/Room');
var User = require('../mongodb/models/User');

var create = function (data, callback){
	var newRoom = new roomModel(data);
	newRoom.save(callback);
};

var find = function (data, callback){
	roomModel.find(data, callback);
}

var findOne = function (data, callback){
	roomModel.findOne(data, callback);
}

var findById = function (id, callback){
	roomModel.findById(id, callback);
}

var findByIdAndUpdate = function(id, data, callback){
	roomModel.findByIdAndUpdate(id, data, { new: true }, callback);
}

/**
 * Add a user along with the corresponding socket to the passed room
 *
 */
var addUser = function(room, socket, callback){

	// Get current user's id
	var user = socket.request.session.passport.user;

	// Push a new connection object(i.e. {userId + socketId})
	var conn = { userId: user.userId, socketId: socket.id};
	room.connections.push(conn);
	room.save(callback);
}

/**
 * Get all users in a room
 *
 */
var getUsers = function(room, socket, callback){

	var users = [], vis = {}, cunt = 0;
	var userId = socket.request.session.passport.user.userId;

	// Loop on room's connections, Then:
	room.connections.forEach(function(conn){

		// 1. Count the number of connections of the current user(using one or more sockets) to the passed room.
		if(conn.userId === userId){
			cunt++;
		}

		// 2. Create an array(i.e. users) contains unique users' ids
		if(!vis[conn.userId]){
			users.push(conn.userId);
		}
		vis[conn.userId] = true;
	});

	// Loop on each user id, Then:
	// Get the user object by id, and assign it to users array.
	// So, users array will hold users' objects instead of ids.
	users.forEach(function(userId, i){
		User.findOne({userId:userId}, function(err, user){
			if (err) { return callback(err); }
			if(user) users[i] = user;
			if(i + 1 === users.length){
				console.log('[Room] conn forEach users.length = ' + users.length)
				return callback(null, users, cunt);
			}
		});
	});
}

/**
 * Remove a user along with the corresponding socket from a room
 *
 */
var removeUser = function(socket, callback){

	// Get current user's id
	var userId = socket.request.session.passport.user.userId;

	find(function(err, rooms){
		if(err) { return callback(err); }

		// Loop on each room, Then:
		rooms.every(function(room){
			var pass = true, cunt = 0, target = 0;

			// For every room,
			// 1. Count the number of connections of the current user(using one or more sockets).
			room.connections.forEach(function(conn, i){
				if(conn.userId === userId){
					cunt++;
				}
				if(conn.socketId === socket.id){
					pass = false, target = i;
				}
			});

			// 2. Check if the current room has the disconnected socket,
			// If so, then, remove the current connection object, and terminate the loop.
			if(!pass) {
				room.connections.id(room.connections[target]._id).remove();
				room.save(function(err){
					callback(err, room, userId, cunt);
				});
			}

			return pass;
		});
	});
}

module.exports = {
	create,
	find,
	findOne,
	findById,
	addUser,
	getUsers,
	removeUser
};
