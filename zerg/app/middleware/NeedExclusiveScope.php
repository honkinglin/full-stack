<?php

namespace app\middleware;

use app\service\Token as TokenService;
use app\enum\ScopeEnum;
use app\exception\ForbiddenException;
use app\exception\TokenException;

class NeedExclusiveScope
{
  public function handle($request, \Closure $next)
  {
    $result = $this->checkExclusiveScope();
    if ($result) {
      return $next($request);
    }
  }

  // 只有用户才能访问的权限
  protected function checkExclusiveScope()
  {
    $scope = TokenService::getCurrentTokenVar('scope');
    if ($scope) {
      if ($scope == ScopeEnum::User) {
        return true;
      } else {
        throw new ForbiddenException();
      }
    } else {
      throw new TokenException();
    }
  }
}
