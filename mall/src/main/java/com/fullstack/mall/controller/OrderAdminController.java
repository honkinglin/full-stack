package com.fullstack.mall.controller;

import com.fullstack.mall.common.ApiRestResponse;
import com.fullstack.mall.model.vo.OrderStatisticsVO;
import com.fullstack.mall.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

/**
 * 订单后台管理Controller
 */
@RestController
@RequestMapping("/admin/order")
public class OrderAdminController {
    @Autowired
    OrderService orderService;

    @GetMapping("/list")
    public ApiRestResponse listForAdmin(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        return ApiRestResponse.success(orderService.listForAdmin(pageNum, pageSize));
    }

    @PostMapping("/delivered")
    public ApiRestResponse delivered(@RequestParam String orderNo) {
        orderService.deliver(orderNo);
        return ApiRestResponse.success();
    }

    @GetMapping("statistics")
    public ApiRestResponse statistics(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate, @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        List<OrderStatisticsVO> statistics = orderService.statistics(startDate, endDate);
        return ApiRestResponse.success(statistics);
    }
}
