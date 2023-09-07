<?php

namespace app\controller\v1;

use think\Validate;
use app\validate\AddressNew;
use app\service\Token as TokenService;
use app\model\User;
use app\exception\UserException;
use app\exception\SuccessMessage;

class Address extends Validate
{
  public function createOrUpdateAddress()
  {
    $validate = new AddressNew();
    $validate->goCheck();
    // 根据Token获取uid
    // 根据uid来查找用户数据，判断用户是否存在，如果不存在抛出异常
    // 获取用户从客户端提交来的地址信息
    // 根据用户地址信息是否存在，从而判断是添加地址还是更新地址
    // 返回处理结果
    $uid = TokenService::getCurrentUid();
    $user = User::find($uid);
    if (!$user) {
      throw new UserException();
    }

    $dataArray = $validate->getDataByRule(input('post.'));

    $userAddress = $user->address();
    if (!$userAddress) {
      $user->address()->save($dataArray);
    } else {
      $user->address->save($dataArray);
    }
    return json(new SuccessMessage(), 200);
  }
}
