<?php

namespace app\controller\v1;

use app\exception\BannerMissException;
use think\Validate;
use think\Exception;

use app\exception\BaseException;
use app\validate\IDMustBePositiveInt;
use app\model\Banner as BannerModel;

class Banner extends Validate
{
  /**
   * 获取指定id的banner信息
   *
   * @url /banner/:id
   * @id banner id号
   * @return void
   */
  public function getBanner($id)
  {

    (new IDMustBePositiveInt())->goCheck();

    $banner = BannerModel::getBannerByID($id);
    // if (!$banner) {
    //   throw new Exception('内部错误');
    //   throw new BannerMissException();
    // }

    return $banner;
  }
}
