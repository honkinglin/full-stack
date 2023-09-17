<?php
declare (strict_types = 1);

namespace app\model;

use app\model\BaseModel;

/**
 * @mixin \think\Model
 */
class OrderProduct extends BaseModel
{
    protected $hidden = ['id', 'order_id', 'delete_time'];

    public function product()
    {
        return $this->belongsTo('Product', 'product_id', 'id');
    }
}
