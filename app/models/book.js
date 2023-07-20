const { Sequelize, Model } = require("sequelize");
const { sequelize } = require("../../core/db");
const util = require("util");
const axios = require("axios");

class Book extends Model {
  constructor(id) {
    super();
    this.id = id;
  }

  async detail() {
    const url = util.format(global.config.yushu.detailUrl, this.id);
    return axios.get(url);
  }
}

Book.init(
  {
    id: {
      type: Sequelize.INTEGER,
      primaryKey: true,
    },
    fav_nums: {
      type: Sequelize.INTEGER,
      defaultValue: 0,
    },
  },
  {
    sequelize,
    tableName: "book",
  }
);

module.exports = {
  Book,
};
