<?php

namespace app\exception;

use app\exception\BaseException;

class UserException extends BaseException
{
  public $code = 404;
  public $msg = '指定的用户不存在，请检查用户ID';
  public $errorCode = 60000;
}