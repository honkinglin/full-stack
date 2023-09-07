<?php

namespace app\service;

use app\exception\TokenException;
use think\facade\Request;
use think\facade\Cache;

class Token
{
  public static function generateToken()
  {
    // 32个字符组成一组随机字符串
    $randChars = getRandChar(32);
    // 用三组字符串，进行md5加密
    $timestamp = $_SERVER['REQUEST_TIME_FLOAT'];
    // salt 盐
    $salt = config('secure.token_salt');
    return md5($randChars . $timestamp . $salt);
  }

  public static function getCurrentTokenVar($key)
  {
    // Token
    // HTTP: header
    // 获取Token
    $token = Request::header('token');
    // Token
    // Cache
    // Redis
    // key: token
    // value: uid
    // 从缓存中获取uid
    $vars = Cache::get($token);
    if (!$vars) {
      throw new TokenException();
    } else {
      if (!is_array($vars)) {
        $vars = json_decode($vars, true);
      }
      if (array_key_exists($key, $vars)) {
        return $vars[$key];
      } else {
        throw new TokenException([
          'msg' => '尝试获取的Token变量并不存在'
        ]);
      }
    }
  }

  public static function getCurrentUid()
  {
    $uid = self::getCurrentTokenVar('uid');
    return $uid;
  }
}
