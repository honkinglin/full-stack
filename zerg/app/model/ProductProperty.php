<?php
declare (strict_types = 1);

namespace app\model;

use app\model\BaseModel;

class ProductProperty extends BaseModel
{
    protected $hidden = ['id', 'delete_time', 'product_id'];
}
