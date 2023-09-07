<?php

namespace app\validate;

use app\validate\BaseValidate;

class AddressNew extends BaseValidate
{
  protected $rule = [
    'name' => 'require|isNotEmpty',
    'mobile' => 'require|isMobile',
    'province' => 'require|isNotEmpty',
    'city' => 'require|isNotEmpty',
    'country' => 'require|isNotEmpty',
    'detail' => 'require|isNotEmpty'
  ];

  protected $message = [
    'name' => '收货人姓名不能为空',
    'mobile' => "手机号码格式不正确",
    'province' => '省份不能为空',
    'city' => '城市不能为空',
    'country' => '区县不能为空',
    'detail' => '详细地址不能为空'
  ];
}