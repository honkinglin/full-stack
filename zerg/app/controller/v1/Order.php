<?php

namespace app\controller\v1;

use app\validate\OrderPlace;
use app\validate\PagingParameter;
use app\validate\IDMustBePositiveInt;
use app\exception\OrderException;
use app\model\Order as OrderModel;
use app\service\Token as TokenService;
use app\service\Order as OrderService;

class Order
{
  protected $middleware = [
    'app\middleware\CheckExclusiveScope' => ['only' => 'placeOrder'],
    'app\middleware\CheckPrimaryScope' => ['only' => 'getDetail, getSummaryByUser'],
  ];

  // 用户在选择商品后，向API提交包含它所选择商品的相关信息
  // API在接收到信息后，需要检查订单相关商品的库存量
  // 有库存，把订单数据存入数据库中 = 下单成功了，返回客户端消息，告诉客户端可以支付了
  // 调用支付接口，进行支付
  // 还需要再次进行库存量检测
  // 服务器这边就可以调用微信的支付接口进行支付
  // 微信会返回给我们一个支付的结果（异步）
  // 成功：也需要进行库存量的检查
  // 成功：进行库存量的扣除，失败：返回一个支付失败的结果

  public function placeOrder()
  {
    (new OrderPlace())->goCheck();
    $products = input('post.products/a');
    $uid = TokenService::getCurrentUid();
    $order = new OrderService();
    $status = $order->place($uid, $products);
    return json($status, 200);
  }

  public function getSummaryByUser($page = 1, $size = 15)
  {
    (new PagingParameter())->goCheck();
    $uid = TokenService::getCurrentUid();
    $pagingOrders = OrderModel::getSummaryByUser($uid, $page, $size);
    if ($pagingOrders->isEmpty()) {
      return json([
        'data' => [],
        'current_page' => $pagingOrders->getCurrentPage()
      ]);
    }
    $data = $pagingOrders->hidden(['snap_items', 'snap_address', 'prepay_id'])->toArray();
    return json([
      'data' => $data,
      'current_page' => $pagingOrders->getCurrentPage()
    ]);
  }

  public function getDetail($id)
  {
    (new IDMustBePositiveInt())->goCheck();
    $orderDetail = OrderModel::find($id);
    if (!$orderDetail) {
      throw new OrderException();
    }
    return json($orderDetail->hidden(['prepay_id']));
  }

  public function getSummary($page = 1, $size = 20)
  {
    (new PagingParameter())->goCheck();
    $pagingOrders = OrderModel::getSummaryByPage($page, $size);
    if ($pagingOrders->isEmpty()) {
      return json([
        'current_page' => $pagingOrders->getCurrentPage(),
        'data' => []
      ]);
    }
    $data = $pagingOrders->hidden(['snap_items', 'snap_address'])->toArray();
    return json([
      'current_page' => $pagingOrders->getCurrentPage(),
      'data' => $data
    ]);
  }
}
