<?php

namespace app\service;

use app\model\Product;
use app\exception\OrderException;
use app\model\UserAddress;
use app\exception\UserException;
use app\model\Order as OrderModel;
use app\model\OrderProduct;

class Order
{
  // 订单的商品列表，也就是客户端传递过来的 products 参数
  protected $oProducts;
  // 真实的商品信息（包括库存量）
  protected $products;
  protected $uid;

  public function place($uid, $oProducts)
  {
    // oProducts 和 products 作对比
    // products 从数据库中查询出来
    $this->oProducts = $oProducts;
    $this->products = $this->getProductsByOrder($oProducts);
    $this->uid = $uid;
    $status = $this->getOrderStatus();
    if (!$status['pass']) {
      $status['order_id'] = -1;
      return $status;
    }
    // 开始创建订单
    $orderSnap = $this->snapOrder($status);
    $order = $this->createOrder($orderSnap);
    $order['pass'] = true;
    return $order;
  }

  public function getOrderStatus()
  {
    $status = [
      'pass' => true,
      'orderPrice' => 0,
      'totalCount' => 0,
      'pStatusArray' => []
    ];
    foreach ($this->oProducts as $oProduct) {
      $pStatus = $this->getProductStatus($oProduct['product_id'], $oProduct['count'], $this->products);
      if (!$pStatus['haveStock']) {
        $status['pass'] = false;
      }
      $status['orderPrice'] += $pStatus['totalPrice'];
      $status['totalCount'] += $pStatus['count'];
      array_push($status['pStatusArray'], $pStatus);
    }
    return $status;
  }

  public function getProductStatus($oPID, $oCount, $products)
  {
    $pIndex = -1;
    $pStatus = [
      'id' => null,
      'haveStock' => false,
      'count' => 0,
      'name' => '',
      'totalPrice' => 0
    ];
    for ($i = 0; $i < count($products); $i++) {
      if ($oPID == $products[$i]['id']) {
        $pIndex = $i;
      }
    }
    if ($pIndex == -1) {
      // 客户端传递的 product_id 有可能根本不存在
      throw new OrderException([
        'msg' => 'id 为 ' . $oPID . ' 的商品不存在，创建订单失败'
      ]);
    } else {
      $product = $products[$pIndex];
      $pStatus['id'] = $product['id'];
      $pStatus['count'] = $oCount;
      $pStatus['name'] = $product['name'];
      $pStatus['totalPrice'] = $product['price'] * $oCount;
      if ($product['stock'] - $oCount >= 0) {
        $pStatus['haveStock'] = true;
      }
    }
    return $pStatus;
  }

  // 生成订单快照
  public function snapOrder($status)
  {
    $snap = [
      'orderPrice' => 0,
      'totalCount' => 0,
      'pStatus' => [],
      'snapAddress' => null,
      'snapName' => '',
      'snapImg' => ''
    ];
    $snap['orderPrice'] = $status['orderPrice'];
    $snap['totalCount'] = $status['totalCount'];
    $snap['pStatus'] = $status['pStatusArray'];
    $snap['snapAddress'] = json_encode($this->getUserAddress());
    $snap['snapName'] = $this->products[0]['name'];
    $snap['snapImg'] = $this->products[0]['main_img_url'];
    if (count($this->products) > 1) {
      $snap['snapName'] .= '等';
    }
    return $snap;
  }

  public static function makeOrderNo()
  {
    $yCode = ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J'];
    $orderSn = $yCode[intval(date('Y')) - 2017] . strtoupper(dechex(date('m'))) . date('d') . substr(time(), -5) . substr(microtime(), 2, 5) . sprintf('%02d', rand(0, 99));
    return $orderSn;
  }

  // 创建订单
  private function createOrder($snap)
  {
    try {
      $orderNo = self::makeOrderNo();
      $order = new OrderModel();
      $order->user_id = $this->uid;
      $order->order_no = $orderNo;
      $order->total_price = $snap['orderPrice'];
      $order->total_count = $snap['totalCount'];
      $order->snap_img = $snap['snapImg'];
      $order->snap_name = $snap['snapName'];
      $order->snap_address = $snap['snapAddress'];
      $order->snap_items = json_encode($snap['pStatus']);
      $order->save();

      $orderID = $order->id;
      $create_time = $order->create_time;
      foreach ($this->oProducts as &$p) {
        $p['order_id'] = $orderID;
      }
      $orderProduct = new OrderProduct();
      $orderProduct->saveAll($this->oProducts);

      return [
        'order_no' => $orderNo,
        'order_id' => $orderID,
        'create_time' => $create_time
      ];
    } catch (\Exception $e) {
      throw $e;
    }
  }

  private function getUserAddress()
  {
    $userAddress = UserAddress::where('user_id', '=', $this->uid)
      ->find();
    if (!$userAddress) {
      throw new UserException([
        'msg' => '用户收货地址不存在，下单失败',
        'errorCode' => 60001
      ]);
    }
    return $userAddress->toArray();
  }

  // 根据订单信息查找真实的商品信息
  private function getProductsByOrder($oProducts)
  {
    // 为了避免循环查询数据库，我们可以把查询出来的数据进行整理
    $oPIDs = [];
    foreach ($oProducts as $item) {
      array_push($oPIDs, $item['product_id']);
    }
    $products = Product::select($oPIDs)
      ->visible(['id', 'price', 'stock', 'name', 'main_img_url'])
      ->toArray();
    return $products;
  }
}
