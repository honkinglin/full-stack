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

    public function imgs() {
        return $this->hasMany('ProductImage', 'product_id', 'id');
    }

    public function properties() {
        return $this->hasMany('ProductProperty', 'product_id', 'id');
    }

    public static function getMostRecent($count)
    {
        $products = self::limit($count)->order('create_time desc')->select();
        return $products;
    }

    public static function getProductsByCategoryID($categoryID)
    {
        $products = self::where('category_id', '=', $categoryID)->select();
        return $products;
    }

    public static function getProductDetail($id)
    {
        $product = self::with([
            'imgs' => function ($query) {
                $query->with(['imgUrl'])->order('order', 'asc');
            },
            'properties'
        ])
            ->find($id);
        return $product;
    }
}
