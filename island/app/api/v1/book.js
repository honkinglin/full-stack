const Router = require("koa-router");
const { HotBook } = require("../../models/hot-book");
const {
  PositiveIntegerValidator,
  SearchValidator,
  AddShortCommentValidator,
} = require("../../validators/validator");
const { Book } = require("../../models/book");
const { Comment } = require("../../models/comment");
const { Auth } = require("../../../middlewares/auth");

const router = new Router({
  prefix: "/v1/book",
});

router.get("/hot_list", async (ctx, next) => {
  const books = await HotBook.getAll();
  ctx.body = books;
});

router.get("/:id/detail", async (ctx, next) => {
  const v = await new PositiveIntegerValidator().validate(ctx);
  ctx.body  = await Book.detail(v.get("path.id"));
});

router.get("/search", async (ctx, next) => {
  const v = await new SearchValidator().validate(ctx);
  const result = await Book.searchFromYuShu(
    v.get("query.q"),
    v.get("query.start"),
    v.get("query.count")
  );
  ctx.body = result;
});

router.get("/favor/count", new Auth().m, async (ctx, next) => {
  const count = await Book.getMyFavorBookCount(ctx.auth.uid);
  ctx.body = {
    count,
  };
});

router.get("/:book_id/favor", new Auth().m, async (ctx, next) => {
  const v = await new PositiveIntegerValidator().validate(ctx, {
    id: "book_id",
  });
  const favor = await Favor.getBookFavor(ctx.auth.uid, v.get("path.book_id"));
  ctx.body = favor;
});

router.post("/add/short_comment", new Auth().m, async (ctx, next) => {
  const v = await new AddShortCommentValidator().validate(ctx, {
    id: "book_id",
  });
  await Comment.addComment(v.get("body.book_id"), v.get("body.content"));
  throw new global.errs.Success();
});

router.get("/:book_id/short_comment", new Auth().m, async (ctx, next) => {
  const v = await new PositiveIntegerValidator().validate(ctx, {
    id: "book_id",
  });
  const book_id = v.get("path.book_id");
  const comments = await Comment.getComments(book_id);
  ctx.body = {
    comments,
    book_id,
  };
});

router.get("/hot_keyword", async (ctx) => {
  ctx.body = {
    hot: [
      "Python",
      "哈利·波特",
      "村上春树",
      "东野圭吾",
      "白夜行",
      "韩寒",
      "金庸", 
      "王小波",
    ],
  };
});

module.exports = router;
