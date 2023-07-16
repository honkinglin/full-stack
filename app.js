const Koa = require('koa');
const { bodyParser } = require("@koa/bodyparser");
const InitManager = require('./core/init');
const catchError = require('./middlewares/exception');

require('./models/user');

const app = new Koa();
app.use(bodyParser());
app.use(catchError);
InitManager.initCore(app);

app.listen(3000, () => {
  console.log('server is running at http://localhost:3000');
});