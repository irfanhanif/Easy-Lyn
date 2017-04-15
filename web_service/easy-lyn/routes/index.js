var express = require('express');
var router = express.Router();
var Passenger = require('../Controller/passenger');

/* GET home page. */
router.get('/', function(req, res, next) {
  res.render('index', { title: 'Express' });
});

router.post('/login', function(req, res, next){
  var passenger = new Passenger();
  result = passenger.loginCheck(res, req.body);
});

module.exports = router;
