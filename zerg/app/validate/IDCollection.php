<?php

namespace app\validate;

use app\validate\BaseValidate;

class IDCollection extends BaseValidate
{
  protected $rule = [
    'ids' => 'require|checkIDs'
  ];

  protected $message = [
    'ids' => 'ids 必须是以逗号分隔的多个正整数'
  ];

  protected function checkIDs($value)
  {
    $values = explode(',', $value);
    if (empty($values)) {
      return false;
    }
    foreach ($values as $id) {
      if (!$this->isPositiveInteger($id)) {
        return false;
      }
    }
    return true;
  }
}
