const Router = require("koa-router");
const requireDirectory = require("require-directory");

class InitManager {
  static initCore(app) {
    InitManager.app = app;
    InitManager.initLoadRouters();
    InitManager.loadHttpException();
    InitManager.loadConfig();
  }

  static initLoadRouters() {
    requireDirectory(module, `${process.cwd()}/island/app/api`, { visit: whenLoadModule });

    function whenLoadModule(obj) {
      if (obj instanceof Router) {
        InitManager.app.use(obj.routes());
      }
    }
  }

  static loadHttpException() {
    const errors = require("./http-exception");
    global.errs = errors;
  }

  static loadConfig(path = "") {
    const configPath = path || process.cwd() + "/island/config/config.js";
    const config = require(configPath);
    global.config = config;
  }
}

module.exports = InitManager;
