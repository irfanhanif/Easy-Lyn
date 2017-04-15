function Pengguna(data){
  var connect = require('../Model/connect');
  var Sequelize = require('sequelize');

  var connect_to = new connect();
  this.Pengguna = connect_to.sequelize.define('pengguna', {
    email_pengguna: {
      type: Sequelize.STRING,
      primaryKey: true
    }
  }, {
    timestamps: false,
    paranoid: true,
    underscored: true,
    freezeTableName: true,
    tableName: 'pengguna'
  });
}

Pengguna.prototype.checkEmailAndPassword = function(email, password){
  var fungsi = require('../Model/fungsi');
  var Fungsi = new fungsi();

  this.Pengguna.belongsTo(Fungsi.Fungsi, {foreignKey: 'kode_fungsi'});

  return this.Pengguna.findAll({
    include: [{
      model: Fungsi.Fungsi
    }],
    attributes: ['email_pengguna', 'password_pengguna', 'kode_fungsi'],
    where: {
      email_pengguna: email,
      password_pengguna: password
    }
  });
}

module.exports = Pengguna;
