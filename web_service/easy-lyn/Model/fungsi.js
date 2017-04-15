function Fungsi(data){
  var connect = require('../Model/connect');
  var Sequelize = require('sequelize');

  var connect_to = new connect();
  this.Fungsi = connect_to.sequelize.define('fungsi', {
    kode_fungsi: {
      type: Sequelize.STRING,
      primaryKey: true
    }
  }, {
    timestamps: false,
    paranoid: true,
    underscored: true,
    freezeTableName: true,
    tableName: 'fungsi'
  });
}

module.exports = Fungsi;
