<?php

namespace app\controller\v1;

use app\validate\IDMustBePositiveInt;
use app\service\Pay as PayService;
use app\service\WxNotify;

class Pay
{
  protected $middleware = [
    'app\middleware\CheckExclusiveScope' => ['only' => 'getPreOrder']
  ];

  public function getPreOrder($id)
  {
    (new IDMustBePositiveInt())->goCheck();
    $pay = new PayService($id);
    return $pay->pay();
  }

  public function receiveNotify() {
    // 通知频率为15/15/30/180/1800/1800/1800/1800/3600,单位:秒
    // 1.检查库存量,超卖
    // 2.更新这个订单的status状态
    // 3.减库存
    // 如果成功处理,我们返回微信成功处理的信息,否则,我们需要返回没有成功处理
    // 特点:post;xml格式;不会携带参数
    $notify = new WxNotify();
    $notify->Handle();
  }
}
