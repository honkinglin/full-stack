<?php

namespace app\controller\v1;

use think\Validate;

use app\validate\IDMustBePositiveInt;
use app\validate\IDCollection;
use app\model\Theme as ThemeModel;
use app\exception\ThemeException;

class Theme extends Validate
{
  /**
   * @url /theme?ids=id1,id2,id3...
   * @return 一组theme模型
   */
  public function getSimpleList($ids = '')
  {
    (new IDCollection())->goCheck();

    $ids = explode(',', $ids);
    $result = ThemeModel::with(['topicImg', 'headImg'])->select($ids);
    if ($result->isEmpty()) {
      throw new ThemeException();
    }

    return json($result, 200);
  }

  /**
   * @url /theme/:id
   * @return 一组theme模型
   */
  public function getComplexOne($id) {
    (new IDMustBePositiveInt())->goCheck();

    $theme = ThemeModel::getThemeWithProducts($id);
    if (!$theme) {
      throw new ThemeException();
    }

    return json($theme, 200);
  }
}
