package com.fullstack.mall.controller;

import com.fullstack.mall.common.ApiRestResponse;
import com.fullstack.mall.model.request.CreateOrderReq;
import com.fullstack.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/*
 * @Description: 订单Controller
 */
@RestController
@RequestMapping("/order")
public class OrderController {
    @Autowired
    OrderService orderService;

    @PostMapping("/create")
    public ApiRestResponse create(@RequestBody CreateOrderReq createOrderReq) {
        String orderNo = orderService.create(createOrderReq);
        return ApiRestResponse.success(orderNo);
    }

    @GetMapping("/list")
    public ApiRestResponse list(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return ApiRestResponse.success(orderService.listForCustomer(pageNum, pageSize));
    }

    @GetMapping("/detail")
    public ApiRestResponse detail(@RequestParam String orderNo) {
        return ApiRestResponse.success(orderService.detail(orderNo));
    }

    @PostMapping("/cancel")
    public ApiRestResponse cancel(@RequestParam String orderNo) {
        orderService.cancel(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 生成支付二维码
     */
    @GetMapping("/qrcode")
    public ApiRestResponse qrcode(@RequestParam String orderNo) {
        return ApiRestResponse.success(orderService.qrcode(orderNo));
    }

    @GetMapping("/pay")
    public ApiRestResponse pay(@RequestParam String orderNo) {
        orderService.pay(orderNo);
        return ApiRestResponse.success();
    }

    /**
     * 确认收货
     */
    @PostMapping("/finish")
    public ApiRestResponse finish(@RequestParam String orderNo) {
        orderService.finish(orderNo);
        return ApiRestResponse.success();
    }
}
