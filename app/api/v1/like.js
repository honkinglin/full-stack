const Router = require("koa-router");
const { LikeValidator } = require("../../validators/validator");
const { Favor } = require("../../models/favor");
const { Auth } = require("../../../middlewares/auth");

const router = new Router({
  prefix: "/v1/like",
});

router.post("/", new Auth().m, async (ctx, next) => {
  const v = await new LikeValidator().validate(ctx, { id: "art_id" });
  await Favor.like(v.get("body.art_id"), v.get("body.type"), ctx.auth.uid);
  throw new global.errs.Success();
});

router.post("/cancel", new Auth().m, async (ctx, next) => {
  const v = await new LikeValidator().validate(ctx, { id: "art_id" });
  await Favor.disLike(v.get("body.art_id"), v.get("body.type"), ctx.auth.uid);
  throw new global.errs.Success();
});

module.exports = router;
