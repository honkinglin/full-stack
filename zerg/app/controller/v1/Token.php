<?php

namespace app\controller\v1;

use think\Exception;
use app\validate\TokenGet;
use app\validate\AppTokenGet;
use app\service\UserToken;
use think\facade\Config;
use app\service\AppToken;

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

  public function verifyToken($token = '')
  {
    if (!$token) {
      throw new Exception('token不允许为空');
    }
    $valid = UserToken::verifyToken($token);
    return json([
      'isValid' => $valid
    ]);
  }

  // 获取app令牌
  public function getAppToken($ac = '', $se = '')
  {
    (new AppTokenGet())->goCheck();
    $app = new AppToken();
    $token = $app->get($ac, $se);
    return json([
      'token' => $token
    ], 200);
  }
}
