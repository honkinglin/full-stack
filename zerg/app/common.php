<?php
// 应用公共文件

/**
 * @param string $url get请求地址
 * @return int $httpCode 返回状态码
 * @return mixed $res 返回结果
 */
function curl_get($url, &$httpCode = 0)
{
  $ch = curl_init();
  curl_setopt($ch, CURLOPT_URL, $url);
  curl_setopt($ch, CURLOPT_RETURNTRANSFER, 1);

  // 不做证书校验,部署在linux环境下请改为true
  curl_setopt($ch, CURLOPT_SSL_VERIFYPEER, false);
  curl_setopt($ch, CURLOPT_CONNECTTIMEOUT, 10);
  $file_contents = curl_exec($ch);
  $httpCode = curl_getinfo($ch, CURLINFO_HTTP_CODE);
  curl_close($ch);
  return $file_contents;
}

function getRandChar($length)
{
  $str = null;
  $strPol = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789abcdefghijklmnopqrstuvwxyz";
  $max = strlen($strPol) - 1;

  for ($i = 0; $i < $length; $i++) {
    $str .= $strPol[rand(0, $max)];
  }

  return $str;
}

function generateToken()
{
  // 32个字符组成一组随机字符串
  $randChars = getRandChar(32);
  // 用三组字符串，进行md5加密
  $timestamp = $_SERVER['REQUEST_TIME_FLOAT'];
  // salt 盐
  $salt = config('secure.token_salt');
  return md5($randChars . $timestamp . $salt);
}
