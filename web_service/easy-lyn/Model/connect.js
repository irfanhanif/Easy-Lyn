function Connect(){
  var Sequelize = require('sequelize');
  this.sequelize = new Sequelize('easy-lyn', 'root', '', {
    host: 'localhost',
    dialect: 'mysql'
  });
}

module.exports = Connect;
