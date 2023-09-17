<?php

namespace app\validate;

use app\validate\BaseValidate;
use app\exception\ParameterException;

class OrderPlace extends BaseValidate
{
  protected $rule = [
    'products' => 'checkProducts'
  ];

  protected $singleRule = [
    'product_id' => 'require|isPositiveInteger',
    'count' => 'require|isPositiveInteger'
  ];

  protected $message = [
    'products' => 'products 参数必须是数组，且每个数组元素必须是由 product_id 和 count 组成的键值对'
  ];

  protected function checkProducts($values)
  {
    if (!is_array($values)) {
      return false;
    }
    if (empty($values)) {
      return false;
    }
    foreach ($values as $value) {
      $this->checkProduct($value);
    }
    return true;
  }

  protected function checkProduct($value)
  {
    $validate = new BaseValidate($this->singleRule);
    $result = $validate->check($value);
    if (!$result) {
      throw new ParameterException([
        'msg' => '商品列表参数错误',
      ]);
    }
  }
}
