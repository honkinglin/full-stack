<?php

namespace app\exception;

use app\exception\BaseException;

class SuccessMessage extends BaseException
{
  public $code = 201;
  public $msg = 'ok';
  public $errorCode = 0;
}
