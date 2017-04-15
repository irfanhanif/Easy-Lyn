function Passenger(){
  this.pengguna = require('../Model/pengguna');
}

Passenger.prototype.loginCheck = function(res, req){
  var user = new this.pengguna();
  user.checkEmailAndPassword(req.email, req.password).then(function(result){
    if(result.length > 0){
      var ret = {
        "status": "success",
        "fungsi": result[0].dataValues.fungsi.kode_fungsi
      };
      res.send(ret);
    }
    else{
      var ret = {"status": "failed"};
      res.send(ret);
    }
  });
}

module.exports = Passenger;
