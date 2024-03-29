<?php
// +----------------------------------------------------------------------
// | ThinkPHP [ WE CAN DO IT JUST THINK ]
// +----------------------------------------------------------------------
// | Copyright (c) 2006~2018 http://thinkphp.cn All rights reserved.
// +----------------------------------------------------------------------
// | Licensed ( http://www.apache.org/licenses/LICENSE-2.0 )
// +----------------------------------------------------------------------
// | Author: liu21st <liu21st@gmail.com>
// +----------------------------------------------------------------------
use think\facade\Route;

Route::get('api/:version/banner/:id', ':version.Banner/getBanner');

Route::get('api/:version/theme', ':version.Theme/getSimpleList');

Route::get('api/:version/theme/:id', ':version.Theme/getComplexOne');

Route::get('api/:version/product/recent', ':version.Product/getRecent');
Route::get('api/:version/product/by_category', ':version.Product/getAllInCategory');
Route::get('api/:version/product/:id', ':version.Product/getOne');

Route::get('api/:version/category/all', ':version.Category/getAllCategories');

Route::post('api/:version/token/user', ':version.Token/getToken');
Route::post('api/:version/token/verify', ':version.Token/verifyToken');
Route::post('api/:version/token/app', ':version.Token/getAppToken');

Route::post('api/:version/address', ':version.Address/createOrUpdateAddress');

Route::post('api/:version/order', ':version.Order/placeOrder');
Route::get('api/:version/order/by_user', ':version.Order/getSummaryByUser');
Route::get('api/:version/order/:id', ':version.Order/getDetail');
Route::get('api/:version/order/paginate', ':version.Order/getSummary');

Route::post('api/:version/pay/pay_order', ':version.Pay/getPreOrder');
Route::post('api/:version/pay/notify', ':version.Pay/receiveNotify');
