<?php

namespace app\service;

use app\enum\ScopeEnum;
use think\facade\Config;
use think\Exception;
use app\service\Token as TokenService;
use app\exception\WeChatException;
use app\exception\TokenException;
use app\model\User as UserModel;

class UserToken
{
  protected $code;
  protected $wxAppID;
  protected $wxAppSecret;
  protected $wxLoginUrl;

  function __construct($code, $wxAppID, $wxAppSecret)
  {
    $this->code = $code;
    $this->wxAppID = $wxAppID;
    $this->wxAppSecret = $wxAppSecret;
    $this->wxLoginUrl = sprintf(
      Config::get('wx.login_url'),
      $this->wxAppID,
      $this->wxAppSecret,
      $this->code
    );
  }

  public function get()
  {
    $result = curl_get($this->wxLoginUrl);
    $wxResult = json_decode($result, true);
    if (empty($wxResult)) {
      throw new Exception('获取session_key及openID时异常，微信内部错误');
    } else {
      $loginFail = array_key_exists('errcode', $wxResult);
      if ($loginFail) {
        $this->processLoginError($wxResult);
      } else {
        return $this->grantToken($wxResult);
      }
    }
    return $wxResult;
  }

  private function processLoginError($wxResult)
  {
    throw new WeChatException([
      'msg' => $wxResult['errmsg'],
      'errorCode' => $wxResult['errcode']
    ]);
  }

  private function grantToken($wxResult)
  {
    // 拿到openid
    // 数据库里看一下，这个openid是不是已经存在
    // 如果存在 则不处理，如果不存在那么新增一条user记录
    // 生成令牌，准备缓存数据，写入缓存
    // 把令牌返回到客户端去
    // key: 令牌
    // value: wxResult, uid, scope
    $openid = $wxResult['openid'];
    $user = UserModel::getByOpenID($openid);
    if ($user) {
      $uid = $user->id;
    } else {
      $uid = $this->newUser($openid);
    }
    $cachedValue = $this->prepareCachedValue($wxResult, $uid);
    $token = $this->saveToCache($cachedValue);
    return $token;
  }

  private function newUser($openid)
  {
    $user = UserModel::create([
      'openid' => $openid
    ]);
    return $user->id;
  }

  private function prepareCachedValue($wxResult, $uid)
  {
    $cachedValue = $wxResult;
    $cachedValue['uid'] = $uid;
    // scope = 16 代表App用户的权限数值
    $cachedValue['scope'] = ScopeEnum::User;
    // scope = 32 代表CMS（管理员）用户的权限数值
    // $cachedValue['scope'] = ScopeEnum::Super;
    return $cachedValue;
  }

  private function saveToCache($cachedValue)
  {
    $key = TokenService::generateToken();
    $value = json_encode($cachedValue);
    $expire_in = Config::get('setting.token_expire_in');
    // 可以更改成 redis 缓存
    $request = cache($key, $value, $expire_in);
    if (!$request) {
      throw new TokenException([
        'msg' => '服务器缓存异常',
        'errorCode' => 10005
      ]);
    }
    return $key;
  }

  public static function verifyToken($token)
  {
    $exist = cache($token);
    if ($exist) {
      return true;
    } else {
      return false;
    }
  }
}
