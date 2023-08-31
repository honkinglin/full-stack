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

Route::get('api/:version/category/all', ':version.Category/getAllCategories');

// Route::get('banner/:id', 'banner/getBanner');

// Route::get('think', function () {
//     return 'hello,ThinkPHP6!';
// });

// Route::get('hello/:id', 'index/hello');

// // Route::get('shop/:id', 'index/shop/test/hello');
