<?php

namespace app\validate;

use app\validate\BaseValidate;

class IDMustBePositiveInt extends BaseValidate
{

  protected $rule = [
    'id' => 'require|isPositiveInteger'
  ];

  // 自定义验证规则
  protected function isPositiveInteger($value, $rule = '', $data = '', $field = '')
  {
    if (is_numeric($value) && is_int($value + 0) && ($value + 0) > 0) {
      return true;
    } else {
      return $field . '必须是正整数';
    }
  }

  protected $message = [
    'id' => 'id 必须是正整数'
  ];
}
