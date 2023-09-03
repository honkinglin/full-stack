<?php

namespace app\exception;

use app\exception\BaseException;

class TokenException extends BaseException
{
  public $code = 401;
  public $msg = 'Token已过期或无效Token';
  public $errorCode = 10001;
}
