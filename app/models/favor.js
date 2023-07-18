const { Sequelize, Model } = require("sequelize");
const { sequelize } = require("../../core/db");

const { Art } = require("../models/art");

class Favor extends Model {
  static async like(art_id, type, uid) {
    // 1. 添加记录
    // 2. classic fav_nums
    // 数据库事务
    // ACID
    // 原子性
    // 一致性
    // 隔离性
    // 持久性
    const favor = await Favor.findOne({
      where: {
        art_id,
        type,
        uid,
      },
    });
    if (favor) {
      throw new global.errs.LikeError();
    }

    return sequelize.transaction(async (t) => {
      console.log(uid, art_id, type);
      await Favor.create(
        {
          uid,
          art_id,
          type,
        },
        { transaction: t }
      );
      const art = await Art.getData(art_id, type);
      await art.increment("fav_nums", { by: 1, transaction: t });
    });
  }

  static async disLike(art_id, type, uid) {
    const favor = await Favor.findOne({
      where: {
        art_id,
        type,
        uid,
      },
    });
    if (!favor) {
      throw new global.errs.DislikeError();
    }
    return sequelize.transaction(async (t) => {
      await favor.destroy({
        force: true, // false 软删除
        transaction: t,
      });
      const art = await Art.getData(art_id, type);
      await art.decrement("fav_nums", { by: 1, transaction: t });
    });
  }
}

Favor.init(
  {
    uid: Sequelize.STRING,
    art_id: Sequelize.INTEGER,
    type: Sequelize.INTEGER,
  },
  {
    sequelize,
    tableName: "favor",
  }
);

module.exports = {
  Favor,
};
