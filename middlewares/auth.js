const jwt = require("jsonwebtoken");
const basicAuth = require("basic-auth");

class Auth {
  constructor() {}

  get m() {
    return async (ctx, next) => {
      const userToken = basicAuth(ctx.req);
      let errMsg = "token不合法";

      if (!userToken || !userToken.name) {
        throw new global.errs.Forbidden();
      }
      try {
        const decode = jwt.verify(
          userToken.name,
          global.config.security.secretKey
        );
        ctx.auth = {
          uid: decode.uid,
          scope: decode.scope,
        };
      } catch (error) {
        if (error.name === "TokenExpiredError") {
          errMsg = "token已过期";
        }

        throw new global.errs.Forbidden(errMsg);
      }

      await next();
    };
  }
}

module.exports = {
  Auth,
};
