<?php

namespace app\controller\v1;

use think\Validate;
use app\validate\Count;
use app\model\Product as ProductModel;
use app\exception\ProductException;

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
}
