const { Sequelize, Model } = require("sequelize");
const { unset } = require("lodash");

const { dbName, host, port, user, password } =
  require("../config/config").database;

const sequelize = new Sequelize(dbName, user, password, {
  host,
  port,
  dialect: "mysql",
  logger: true,
  timezone: "+08:00",
  define: {
    timestamps: true, // 创建 create_time update_time
    paranoid: true, // delete_time
    createdAt: "created_at",
    updatedAt: "updated_at",
    deletedAt: "deleted_at",
    underscored: true, // 驼峰转下划线
  },
});

sequelize.sync({
  // force: true, // true: 每次都会重新建表
});

// 全局过滤多余字段
Model.prototype.toJSON = function () {
  let data = Object.assign({}, this.dataValues);
  unset(data, "updated_at");
  unset(data, "created_at");
  unset(data, "deleted_at");

  for (key in data) {
    if (key === "image") {
      if (!data[key].startsWith("http"))
        data[key] = global.config.host + data[key];
    }
  }

  if (isArray(this.exclude)) {
    this.exclude.forEach((value) => {
      unset(data, value);
    });
  }
  return data;
};

module.exports = {
  sequelize,
};
