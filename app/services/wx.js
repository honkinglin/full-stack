const axios = require("axios");
const util = require("util");

const { User } = require("../models/user");
const { Auth } = require("../../middlewares/auth");
const { generateToken } = require("../../core/util");

class WXManager {
  static async codeToToken(code) {
    // 1. code 换取 openid
    // 2. openid 换取 token
    // 3. token 存入数据库
    // 4. 返回 token
    const url = util.format(
      global.config.wx.loginUrl,
      global.config.wx.appId,
      global.config.wx.appSecret,
      code
    );
    const result = await axios.get(url);
    if (result.status !== 200) {
      throw new global.errs.AuthFailed("openid获取失败");
    }
    const { errcode, errmsg } = result.data;
    if (errcode) {
      throw new global.errs.AuthFailed("openid获取失败：" + errmsg);
    }
    const { openid, session_key } = result.data;
    const user = await User.getUserByOpenid(openid);
    if (!user) {
      await User.registerByOpenid(openid);
    }

    return generateToken(openid, Auth.USER);
  }
}

module.exports = {
  WXManager,
};
