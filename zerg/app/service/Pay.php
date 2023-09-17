<?php

namespace app\service;

use app\exception\TokenException;
use think\Exception;
use app\service\Order as OrderService;
use app\model\Order as OrderModel;
use app\service\Token;
use app\enum\OrderStatusEnum;
use app\exception\OrderException;
use think\facade\Log;

require_once 'app/extend/WxPay/WxPay.Api.php';

class Pay
{
  private $orderID;
  private $orderNO;

  function __construct($orderID)
  {
    if (!$orderID) {
      throw new Exception('订单号不允许为NULL');
    }
    $this->orderID = $orderID;
  }

  public function pay()
  {
    // 1. 检测订单号是否存在
    // 2. 检测订单号是否和当前用户匹配
    // 3. 检测订单是否已经支付过
    // 4. 进行库存量检测
    // 5. 更新订单状态
    // 6. 减库存
    // 7. 返回结果
    $this->checkOrderValid();
    $orderService = new OrderService();
    $status = $orderService->checkOrderStock($this->orderID);
    if (!$status['pass']) {
      return $status;
    }
    return $this->makeWxPreOrder($status['orderPrice']);
  }

  private function makeWxPreOrder($orderPrice)
  {
    $openid = Token::getCurrentTokenVar('openid');
    if (!$openid) {
      throw new TokenException();
    }
    $wxOrderData = new \WxPayUnifiedOrder();
    $wxOrderData->SetOut_trade_no($this->orderNO);
    $wxOrderData->SetTrade_type('JSAPI');
    $wxOrderData->SetTotal_fee($orderPrice * 100);
    $wxOrderData->SetBody('零食商贩');
    $wxOrderData->SetOpenid($openid);
    $wxOrderData->SetNotify_url(config('secure.pay_back_url'));
    return $this->getPaySignature($wxOrderData);
  }

  private function getPaySignature($wxOrderData)
  {
    $wxOrder = \WxPayApi::unifiedOrder($wxOrderData);
    if ($wxOrder['return_code'] != 'SUCCESS' || $wxOrder['result_code'] != 'SUCCESS') {
      Log::record($wxOrder, 'error');
      Log::record('获取预支付订单失败', 'error');
    }
    // prepay_id
    $this->recordPreOrder($wxOrder);
    $signature = $this->sign($wxOrder);
    return $signature;
  }

  private function recordPreOrder($wxOrder)
  {
    OrderModel::where('id', '=', $this->orderID)
      ->update(['prepay_id' => $wxOrder['prepay_id']]);
  }

  private function sign($wxOrder)
  {
    $jsApiPayData = new \WxPayJsApiPay();
    $jsApiPayData->SetAppid(config('wx.app_id'));
    $jsApiPayData->SetTimeStamp((string)time());
    $rand = md5(time() . mt_rand(0, 1000));
    $jsApiPayData->SetNonceStr($rand);
    $jsApiPayData->SetPackage('prepay_id=' . $wxOrder['prepay_id']);
    $jsApiPayData->SetSignType('md5');
    $sign = $jsApiPayData->MakeSign();
    $rawValues = $jsApiPayData->GetValues();
    $rawValues['paySign'] = $sign;
    unset($rawValues['appId']);
    return $rawValues;
  }

  private function checkOrderValid()
  {
    $order = OrderModel::where('id', '=', $this->orderID)
      ->find();
    if (!$order) {
      throw new OrderException();
    }
    if (!Token::isValidOperate($order->user_id)) {
      throw new TokenException([
        'msg' => '订单与用户不匹配',
        'errorCode' => 10003
      ]);
    }
    if ($order->status != OrderStatusEnum::UNPAID) {
      throw new OrderException([
        'msg' => '订单已支付过啦',
        'errorCode' => 80003,
        'code' => 400
      ]);
    }
    $this->orderNO = $order->order_no;
    return true;
  }
}
