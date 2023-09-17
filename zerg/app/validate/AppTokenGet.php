<?php

namespace app\validate;

use app\validate\BaseValidate;

class AppTokenGet extends BaseValidate
{
  protected $rule = [
    'ac' => 'require|isNotEmpty',
    'se' => 'require|isNotEmpty'
  ];

  protected $message = [
    'ac' => '缺少必要参数',
    'se' => '缺少必要参数'
  ];
}
