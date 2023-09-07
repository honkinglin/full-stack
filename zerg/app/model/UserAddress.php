<?php
declare (strict_types = 1);

namespace app\model;

use app\model\BaseModel;

class UserAddress extends BaseModel
{
    protected $hidden = ['id', 'delete_time', 'user_id'];

    public function user()
    {
        return $this->belongsTo('User', 'user_id', 'id');
    }
}
