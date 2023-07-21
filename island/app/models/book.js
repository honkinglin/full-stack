const { Sequelize, Model } = require("sequelize");
const { sequelize } = require("../../core/db");
const util = require("util");
const axios = require("axios");
const { Favor } = require("./favor");

class Book extends Model {
  static async detail(id) {
    const url = util.format(global.config.yushu.detailUrl, id);
    return axios.get(url);
  }

  static async searchFromYuShu(q, start, count, summary = 1) {
    const url = util.format(
      global.config.yushu.keywordUrl,
      encodeURI(q),
      count,
      start,
      summary
    );
    const result = await axios.get(url);
    return result.data;
  }

  static async getMyFavorBookCount(uid) {
    const count = await Favor.count({
      where: {
        uid,
        type: 400,
      },
    });
    return count;
  }

  static async getBookFavor(uid, bookID) {
    const favorNums = await Favor.count({
      where: {
        art_id: bookID,
        type: 400,
      },
    });
    const myFavor = await Favor.findOne({
      where: {
        art_id: bookID,
        uid,
        type: 400,
      },
    });
    return {
      fav_nums: favorNums,
      like_status: myFavor ? 1 : 0,
    };
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
