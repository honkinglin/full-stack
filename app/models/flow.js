const { Sequelize, Model } = require("sequelize");
const { sequelize } = require("../../core/db");

class Flow extends Model {}

Flow.init(
  {
    index: Sequelize.INTEGER,
    art_id: Sequelize.INTEGER,
    type: Sequelize.INTEGER,
    // type: 100 电影
    // type: 200 音乐
    // type: 300 句子
    // type: 400 书籍
  },
  {
    sequelize,
    tableName: "flow",
  }
);

module.exports = {
  Flow,
};
