<?php

namespace app\model;

use think\facade\Db;
use app\model\BaseModel;

class Banner extends BaseModel
{
  protected $hidden = ['update_time', 'delete_time'];

  // 定义关联方法
  public function items()
  {
    return $this->hasMany('BannerItem', 'banner_id', 'id');
  }

  // 默认的表名是类名的复数形式，可以通过设置 protected $table 来指定表名
  // protected $table = 'banner';

  public static function getBannerByID($id)
  {
    // with 可以传数组，也可以传字符串，嵌套关联关系用 . 分割
    $banner = self::with(['items', 'items.img'])->find($id);
    return $banner;

    // // 1. 使用原生 sql 查询
    // // $result = Db::query('select * from banner_item where banner_id=?', [$id]);
    // // return $result;

    // // 2. 使用闭包查询
    // // $result = Db::table('banner_item')->where(function ($query) use ($id) {
    // //   $query->where('banner_id', '=', $id);
    // // })->select();

    // // 3. 使用链式查询
    // $result = Db::table('banner_item')->where('banner_id', '=', $id)->select();
    // return $result;
  }
}
