module.exports = {
  environment: "dev",
  database: {
    dbName: "island",
    host: "127.0.0.1",
    port: 3306,
    user: "root",
    password: "1234qwer",
  },
  security: {
    secretKey: "abcdefg",
    expiresIn: 60 * 60 * 24 * 30,
  },
};
