<?php

declare(strict_types=1);

namespace app\model;

use app\model\BaseModel;

/**
 * @mixin \think\Model
 */
class Category extends BaseModel
{
    protected $hidden = ['update_time', 'delete_time', 'topic_img_id'];

    public function img()
    {
        return $this->belongsTo('Image', 'topic_img_id', 'id');
    }
}
