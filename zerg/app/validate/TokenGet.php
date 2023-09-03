<?php

namespace app\validate;

use app\validate\BaseValidate;

class TokenGet extends BaseValidate
{

  protected $rule = [
    'code' => 'require|isNotEmpty'
  ];

  protected $message = [
    'code' => '没有code还想获取Token，做梦哦'
  ];
}