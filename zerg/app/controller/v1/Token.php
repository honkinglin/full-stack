<?php

namespace app\controller\v1;

use app\validate\TokenGet;
use app\service\UserToken;
use think\facade\Config;

class Token
{
  public function getToken($code = '')
  {
    (new TokenGet())->goCheck();
    $ut = new UserToken($code, Config::get('wx.app_id'), Config::get('wx.app_secret'));
    $token = $ut->get();
    return json([
      'token' => $token
    ], 200);
  }
}
