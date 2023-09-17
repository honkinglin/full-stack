<?php
declare (strict_types = 1);

namespace app\model;

use app\model\BaseModel;

/**
 * @mixin \think\Model
 */
class Order extends BaseModel
{
    protected $hidden = ['user_id', 'delete_time', 'update_time'];

    protected $autoWriteTimestamp = true;

    public function getSnapItemsAttr($value)
    {
        if (empty($value)) {
            return null;
        }
        return json_decode($value);
    }

    public function getSnapAddressAttr($value)
    {
        if (empty($value)) {
            return null;
        }
        return json_decode($value);
    }

    public static function getSummaryByUser($uid, $page = 1, $size = 15)
    {
        $pagingData = self::where('user_id', '=', $uid)
            ->order('create_time', 'desc')
            ->paginate((int)$size, false, ['page' => (int)$page]);
        return $pagingData;
    }
}
