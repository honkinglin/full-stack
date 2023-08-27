<?php

declare(strict_types=1);

namespace app\model;

use app\model\BaseModel;

/**
 * @mixin \think\Model
 */
class Product extends BaseModel
{
    protected $hidden = ['update_time', 'delete_time', 'pivot', 'from', 'category_id', 'img_id', 'summary'];

    public function getMainImgUrlAttr($value, $data)
    {
        return $this->prefixImgUrl($value, $data);
    }

    public static function getMostRecent($count)
    {
        $products = self::limit($count)->order('create_time desc')->select();
        return $products;
    }
}
