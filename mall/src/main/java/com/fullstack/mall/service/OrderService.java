package com.fullstack.mall.service;

import com.fullstack.mall.model.request.CreateOrderReq;
import com.fullstack.mall.model.vo.OrderStatisticsVO;
import com.fullstack.mall.model.vo.OrderVO;
import com.github.pagehelper.PageInfo;

import java.util.Date;
import java.util.List;

public interface OrderService {
    String create(CreateOrderReq createOrderReq);

    PageInfo listForCustomer(Integer pageNum, Integer pageSize);

    OrderVO detail(String orderNo);

    void cancel(String orderNo);

    String qrcode(String orderNo);

    PageInfo listForAdmin(Integer pageNum, Integer pageSize);

    void pay(String orderNo);

    void deliver(String orderNo);

    void finish(String orderNo);

    List<OrderStatisticsVO> statistics(Date startDate, Date endDate);
}
