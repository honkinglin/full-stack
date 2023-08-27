<?php

namespace app\validate;

use app\validate\BaseValidate;

class IDMustBePositiveInt extends BaseValidate
{

  protected $rule = [
    'id' => 'require|isPositiveInteger'
  ];

  protected $message = [
    'id' => 'id 必须是正整数'
  ];
}
