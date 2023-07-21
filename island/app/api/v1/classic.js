const Router = require("koa-router");

const { LikeValidator } = require("../../validators/validator");
const { Auth } = require("../../../middlewares/auth");
const { Flow } = require("../../models/flow");
const { Favor } = require("../../models/favor");
const { Art } = require("../../models/art");

const router = new Router({
  prefix: "/v1/classic",
});

router.get("/latest", new Auth().m, async (ctx, next) => {
  const flow = await Flow.findOne({
    order: [["index", "DESC"]],
  });
  const art = await Art.getData(flow.art_id, flow.type);
  art.setDataValue("index", flow.index);
  ctx.body = art;
});

router.get("/:type/:id", new Auth().m, async (ctx) => {
  const v = await new LikeValidator().validate(ctx);
  const id = v.get("path.id");
  const type = parseInt(v.get("path.type"));
  const artDetail = await new Art(id, type).getDetail(ctx.auth.uid);

  artDetail.art.setDataValue("like_status", artDetail.like_status);
  ctx.body = artDetail.art;
});

router.get("/:type/:id/favor", new Auth().m, async (ctx) => {
  const v = await new LikeValidator().validate(ctx);
  const id = v.get("path.id");
  const type = parseInt(v.get("path.type"));
  const artDetail = await new Art(id, type).getDetail(ctx.auth.uid);

  ctx.body = {
    fav_nums: artDetail.art.fav_nums,
    like_status: artDetail.like_status,
  };
});

router.get("/favor", new Auth().m, async (ctx) => {
  ctx.body = await Favor.getMyClassicFavors(ctx.auth.uid);
});

module.exports = router;
