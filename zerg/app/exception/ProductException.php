<?php

namespace app\exception;

use app\exception\BaseException;

class ProductException extends BaseException
{
  public $code = 404;
  public $msg = '指定的商品不存在，请检查商品ID';
  public $errorCode = 20000;
}