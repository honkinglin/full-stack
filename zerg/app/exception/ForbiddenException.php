<?php

namespace app\exception;

use app\exception\BaseException;

class ForbiddenException extends BaseException
{
  public $code = 403;
  public $msg = '权限不够';
  public $errorCode = 10001;
}
