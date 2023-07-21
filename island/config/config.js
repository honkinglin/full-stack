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
  wx: {
    appId: process.env.appId,
    appSecret: process.env.appSecret,
    loginUrl: "https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
    // 微信小程序官方文档：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
    // 本地测试：https://developers.weixin.qq.com/miniprogram/dev/api-backend/open-api/login/auth.code2Session.html
  },
  yushu: {
    detailUrl: "http://t.yushu.im/v2/book/id/%s",
    keywordUrl: "http://t.yushu.im/v2/book/search?q=%s&count=%s&start=%s&summary=%s",
  },
  host: "http://localhost:3000/",
};
