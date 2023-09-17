<?php

namespace app\exception;

use app\exception\BaseException;

class OrderException extends BaseException
{
  public $code = 404;
  public $msg = '指定的订单不存在，请检查订单ID';
  public $errorCode = 80000;
}
