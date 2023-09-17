<?php

namespace app\service;

use app\model\ThirdApp;
use think\Exception;
use app\exception\TokenException;

class AppToken extends Token
{

  public function get($ac, $se)
  {
    $app = ThirdApp::check($ac, $se);
    if (!$app) {
      throw new Exception('授权失败');
    } else {
      $scope = $app->scope;
      $uid = $app->id;
      $values = [
        'scope' => $scope,
        'uid' => $uid
      ];
      $token = $this->saveToCache($values);
      return $token;
    }
  }

  private function saveToCache($values)
  {
    $token = self::generateToken();
    $expire_in = config('setting.token_expire_in');
    $result = cache($token, json_encode($values), $expire_in);
    if (!$result) {
      throw new TokenException([
        'msg' => '服务器缓存异常',
        'errorCode' => 10005
      ]);
    }
    return $token;
  }
}
