<?php

return [
  'app_id' => env('APP_ID', ''),
  'app_secret' => env('APP_SECRET', ''),
  'login_url' => 'https://api.weixin.qq.com/sns/jscode2session?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code'
];
