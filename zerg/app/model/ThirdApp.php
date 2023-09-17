<?php

declare(strict_types=1);

namespace app\model;

use app\model\BaseModel;

/**
 * @mixin \think\Model
 */
class ThirdApp extends BaseModel
{
    public static function check($ac, $se)
    {
        $app = self::where('app_id', '=', $ac)
            ->where('app_secret', '=', $se)
            ->find();
        return $app;
    }

    public function user()
    {
        return $this->belongsTo('User', 'user_id', 'id');
    }

    public static function getByAppID($appID)
    {
        $app = self::where('app_id', '=', $appID)
            ->find();
        return $app;
    }

    public static function getAccount($ac)
    {
        $app = self::where('app_id', '=', $ac)
            ->find();
        return $app;
    }

    public static function getAppSecret($appID)
    {
        $app = self::where('app_id', '=', $appID)
            ->find();
        return $app->app_secret;
    }

    public static function getAppID($appID)
    {
        $app = self::where('app_id', '=', $appID)
            ->find();
        return $app->app_id;
    }

    public static function getUID($appID)
    {
        $app = self::where('app_id', '=', $appID)
            ->find();
        return $app->user_id;
    }

    public static function getScope($appID)
    {
        $app = self::where('app_id', '=', $appID)
            ->find();
        return $app->scope;
    }

    public static function getScopeByID($id)
    {
        $app = self::where('id', '=', $id)
            ->find();
        return $app->scope;
    }

    public static function getScopeByAppID($appID)
    {
        $app = self::where('app_id', '=', $appID)
            ->find();
        return $app->scope;
    }

    public static function getScopeByAppIDAndAppSecret($appID, $appSecret)
    {
        $app = self::where('app_id', '=', $appID)
            ->where('app_secret', '=', $appSecret)
            ->find();
        return $app->scope;
    }

    public static function getScopeByAppIDAndAppSecretAndUID($appID, $appSecret, $uid)
    {
        $app = self::where('app_id', '=', $appID)
            ->where('app_secret', '=', $appSecret)
            ->where('user_id', '=', $uid)
            ->find();
        return $app->scope;
    }
}
