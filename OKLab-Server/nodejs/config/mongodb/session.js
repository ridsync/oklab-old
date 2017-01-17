var config 	= require('../config');
var session 	= require('express-session');
var Store = require('express-session').Store;
// var MongoStore	= require('connect-mongo')(session);
var db = require('./db')();
var MongooseStore = require('mongoose-express-session')(Store);

/**
 * Initialize Session
 * Uses MongoDB-based session store
 *
 */
var init = function () {
	// if(process.env.NODE_ENV === 'production') {
				return session({
				secret: '1234DSFs@adf1234!@#$asd',
				resave: false,
				saveUninitialized: true,
				store: new MongooseStore({connection: db}),
				// store: new MongoStore({ mongooseConnection: db.mongoose.connection }),
				cookie: { maxAge: 9 * 60 * 1000 }
			});
  // } else {
		// return session({
		// 	secret: config.sessionSecret,
		// 	resave: false,
		// 	unset: 'destroy',
		// 	saveUninitialized: true
		// });
	// }
}

module.exports = init();
