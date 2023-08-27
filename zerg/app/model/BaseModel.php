<?php
declare (strict_types = 1);

namespace app\model;

use think\facade\Config;
use think\Model;

/**
 * @mixin \think\Model
 */
class BaseModel extends Model
{
    protected function prefixImgUrl($value, $data)
    {
        $finalUrl = $value;
        if ($data['from'] == 1) {
            $finalUrl = Config::get('setting.img_prefix') . $value;
        }
        return $finalUrl;
    }
}
