<?php
declare (strict_types = 1);

namespace app\model;

use app\model\BaseModel;

class User extends BaseModel
{
    public static function getByOpenID($openid)
    {
        $user = self::where('openid', '=', $openid)->find();
        return $user;
    }
}
