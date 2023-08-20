<?php

namespace app\controller;

use app\BaseController;
use app\Request;

class Index extends BaseController
{
    public function index()
    {
        $p = 'hello';
        return '<style>*{ padding: 0; margin: 0; }</style><iframe src="https://www.thinkphp.cn/welcome?version=' . \think\facade\App::version() . '" width="100%" height="100%" frameborder="0" scrolling="auto"></iframe>';
    }

    public function hello()
    {
        // $id = $this->request->param('id');
        // $name = $this->request->param('name');
        $all = $this->request->param();
        // $all = input();
        // $all = $this->request->route();
        // $all = $this->request->get();
        // $all = $this->request->post();

        echo var_dump($all);

        // echo $id;
        // echo '|';
        // echo $name;
        // return 'hello,' . $id;
    }
}
