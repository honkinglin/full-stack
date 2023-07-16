const Sequelize = require("sequelize");

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

module.exports = {
  sequelize,
};
