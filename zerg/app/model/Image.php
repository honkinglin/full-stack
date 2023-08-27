<?php
declare (strict_types = 1);

namespace app\model;

use app\model\BaseModel;

/**
 * @mixin \think\Model
 */
class Image extends BaseModel
{
    protected $hidden = ['id', 'from', 'update_time', 'delete_time'];

    protected function getUrlAttr($value, $data)
    {
        return $this->prefixImgUrl($value, $data);
    }
}
