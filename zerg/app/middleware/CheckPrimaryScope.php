<?php

namespace app\middleware;

use app\service\Token as TokenService;
use app\enum\ScopeEnum;
use app\exception\ForbiddenException;
use app\exception\TokenException;

class CheckPrimaryScope
{
  public function handle($request, \Closure $next)
  {
    $result = $this->checkPrimaryScope();
    if ($result) {
      return $next($request);
    }
  }

  // 用户和CMS管理员都可以访问的权限
  protected function checkPrimaryScope()
  {
    $scope = TokenService::getCurrentTokenVar('scope');
    if ($scope) {
      if ($scope >= ScopeEnum::User) {
        return true;
      } else {
        throw new ForbiddenException();
      }
    } else {
      throw new TokenException();
    }
  }
}
