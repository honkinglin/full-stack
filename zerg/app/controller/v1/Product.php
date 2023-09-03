<?php

namespace app\controller\v1;

use think\Validate;
use app\validate\Count;
use app\model\Product as ProductModel;
use app\exception\ProductException;
use app\validate\IDMustBePositiveInt;

class Product extends Validate
{
  public function getRecent($count = 15)
  {
    (new Count())->goCheck();

    $products = ProductModel::getMostRecent($count);
    if ($products->isEmpty()) {
      throw new ProductException();
    }
    return json($products, 200);
  }

  public function getAllInCategory($id)
  {
    (new IDMustBePositiveInt())->goCheck();
    $category = ProductModel::getProductsByCategoryID($id);
    if ($category->isEmpty()) {
      throw new ProductException();
    }
    return json($category, 200);
  }

  public function getOne($id)
  {
    (new IDMustBePositiveInt())->goCheck();
    $product = ProductModel::getProductDetail($id);
    if (!$product) {
      throw new ProductException();
    }
    return json($product, 200);
  }
}
