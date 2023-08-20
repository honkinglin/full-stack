<?php
namespace app;

use app\exception\BaseException;
use think\db\exception\DataNotFoundException;
use think\db\exception\ModelNotFoundException;
use think\exception\Handle;
use think\exception\HttpException;
use think\exception\HttpResponseException;
use think\exception\ValidateException;
use think\Response;
use Throwable;
use think\facade\Log;

/**
 * 应用异常处理类
 */
class ExceptionHandle extends Handle
{
    private $code;
    private $msg;
    private $errorCode;

    /**
     * 不需要记录信息（日志）的异常类列表
     * @var array
     */
    protected $ignoreReport = [
        HttpException::class,
        HttpResponseException::class,
        ModelNotFoundException::class,
        DataNotFoundException::class,
        ValidateException::class,
    ];

    /**
     * 记录异常信息（包括日志或者其它方式记录）
     *
     * @access public
     * @param  Throwable $exception
     * @return void
     */
    public function report(Throwable $exception): void
    {
        // 使用内置的方式记录异常日志
        parent::report($exception);
    }

    /**
     * Render an exception into an HTTP response.
     *
     * @access public
     * @param \think\Request   $request
     * @param Throwable $e
     * @return Response
     */
    public function render($request, Throwable $e): Response
    {
        // 添加自定义异常处理机制
        if ($e instanceof BaseException) {
            // 如果是自定义的异常
            $this->code = $e->code;
            $this->msg = $e->msg;
            $this->errorCode = $e->errorCode;
        } else {
            // 如果是系统异常
            if (env('APP_DEBUG')) {
                // 调试状态下需要显示TP默认的异常页面，因为TP的异常页面很容易看出问题
                return parent::render($request, $e);
            }
            $this->code = 500;
            $this->msg = '服务器内部错误, 不告诉你';
            $this->errorCode = 999;
            $this->recordErrorLog($e);
        }

        $result = [
            'msg' => $this->msg,
            'error_code' => $this->errorCode,
            'request_url' => $request->url()
        ];
        return json($result, $this->code);
    }

    private function recordErrorLog(Throwable $e)
    {
        Log::record($e->getMessage(), 'error');
    }
}
