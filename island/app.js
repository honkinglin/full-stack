const Koa = require("koa");
const path = require("path");
const { bodyParser } = require("@koa/bodyparser");
const InitManager = require("./core/init");
const catchError = require("./middlewares/exception");
const static = require("koa-static");

require("dotenv").config({ path: path.resolve(__dirname, ".env") });

const app = new Koa();
app.use(bodyParser());
app.use(catchError);
app.use(static(path.join(__dirname, "./static")));

InitManager.initCore(app);

app.listen(3000, () => {
  console.log("server is running at http://localhost:3000");
});
