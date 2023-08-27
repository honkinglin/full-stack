<?php

namespace app\validate;

use app\validate\BaseValidate;

class Count extends BaseValidate
{
  protected $rule = [
    'count' => 'isPositiveInteger|between:1,15'
  ];

  protected $message = [
    'count' => 'count 必须是正整数，且必须在 1 到 15 之间'
  ];
}
