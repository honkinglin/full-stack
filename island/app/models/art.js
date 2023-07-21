const { Sequelize } = require("sequelize");
const { Movie, Sentence, Music } = require("./classic");
const { flatten } = require("lodash");

class Art {
  constructor(art_id, type) {
    this.art_id = art_id;
    this.type = type;
  }

  static async getData(art_id, type, useScope = true) {
    let art = null;
    const finder = {
      where: { id: art_id },
    };
    // const scope = useScope ? "bh" : null;
    switch (type) {
      case 100:
        art = await Movie.findOne(finder);
        break;
      case 200:
        art = await Music.findOne(finder);
        break;
      case 300:
        art = await Sentence.findOne(finder);
        break;
      case 400:
        const { Book } = require("./book");
        art = await Book.findOne(finder);
        if (!art) {
          art = await Book.create({
            id: art_id,
          });
        }
        break;
      default:
        break;
    }
    return art;
  }

  static async getList(artInfoList) {
    const artInfoObj = {
      100: [],
      200: [],
      300: [],
    };
    for (let key of artInfoList) {
      artInfoObj[key.type].push(key.art_id);
    }
    const arts = [];
    for (let key in artInfoObj) {
      const ids = artInfoObj[key];
      if (ids.length === 0) {
        continue;
      }
      key = parseInt(key);
      arts.push(await Art._getListByType(ids, key));
    }
    return flatten(arts);
  }

  static async _getListByType(ids, type) {
    let arts = [];
    const finder = {
      where: {
        id: {
          [Sequelize.Op.in]: ids,
        },
      },
    };
    // const scope = "bh";
    switch (type) {
      case 100:
        arts = await Movie.findAll(finder);
        break;
      case 200:
        arts = await Music.findAll(finder);
        break;
      case 300:
        arts = await Sentence.findAll(finder);
        break;
      case 400:
        break;
      default:
        break;
    }
    return arts;
  }

  async getDetail(uid) {
    const art = await Art.getData(this.art_id, this.type);
    if (!art) {
      throw new global.errs.NotFound();
    }
    const { Favor } = require("./favor");
    const like = await Favor.userLikeIt(this.art_id, this.type, uid);
    return {
      art,
      like_status: like,
    };
  }
}

module.exports = {
  Art,
};
