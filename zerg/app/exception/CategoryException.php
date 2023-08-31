<?php

namespace app\exception;

use app\exception\BaseException;

class CategoryException extends BaseException
{
  public $code = 404;
  public $msg = '指定的类目不存在，请检查类目ID';
  public $errorCode = 50000;
}
