package com.fullstack.mall.service.impl;

import com.fullstack.mall.common.Constant;
import com.fullstack.mall.exception.MallException;
import com.fullstack.mall.exception.MallExceptionEnum;
import com.fullstack.mall.filter.UserFilter;
import com.fullstack.mall.model.dao.CartMapper;
import com.fullstack.mall.model.dao.OrderItemMapper;
import com.fullstack.mall.model.dao.OrderMapper;
import com.fullstack.mall.model.dao.ProductMapper;
import com.fullstack.mall.model.pojo.Order;
import com.fullstack.mall.model.pojo.OrderItem;
import com.fullstack.mall.model.pojo.Product;
import com.fullstack.mall.model.pojo.User;
import com.fullstack.mall.model.query.OrderStatisticsQuery;
import com.fullstack.mall.model.request.CreateOrderReq;
import com.fullstack.mall.model.vo.CartVO;
import com.fullstack.mall.model.vo.OrderItemVO;
import com.fullstack.mall.model.vo.OrderStatisticsVO;
import com.fullstack.mall.model.vo.OrderVO;
import com.fullstack.mall.service.CartService;
import com.fullstack.mall.service.OrderService;
import com.fullstack.mall.service.UserService;
import com.fullstack.mall.utils.OrderCodeFactory;
import com.fullstack.mall.utils.QRCodeGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.zxing.WriterException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 订单Service实现类
 */
@Service
public class OrderServiceImpl implements OrderService {
    @Autowired
    CartService cartService;

    @Autowired
    ProductMapper productMapper;

    @Autowired
    CartMapper cartMapper;

    @Autowired
    OrderMapper orderMapper;

    @Autowired
    OrderItemMapper orderItemMapper;

    @Autowired
    UserService userService;

    @Value("${file.upload.uri}")
    String uri;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public String create(CreateOrderReq createOrderReq) {
        // 拿到用户ID
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        // 从购物车查找已经勾选的商品
        List<CartVO> cartVOList = cartService.list(userId);
        ArrayList<CartVO> cartVOListTemp = new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            if (cartVO.getSelected().equals(Constant.Cart.CHECKED)) {
                cartVOListTemp.add(cartVO);
            }
        }
        cartVOList = cartVOListTemp;
        // 如果购物车已经勾选的商品为空，报错
        if (cartVOList.isEmpty()) {
            throw new MallException(MallExceptionEnum.CART_EMPTY);
        }
        // 判断商品是否存在，上下架状态，库存
        validSaleStatusAndStock(cartVOList);
        // 把购物车对象转换为订单item对象
        List<OrderItem> orderItemList = cartVOListToOrderItemList(cartVOList);
        // 扣库存
        for (OrderItem orderItem : orderItemList) {
            Product product = productMapper.selectByPrimaryKey(orderItem.getProductId());
            int stock = product.getStock() - orderItem.getQuantity();
            if (stock < 0) {
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
            product.setStock(stock);
            productMapper.updateByPrimaryKeySelective(product);
        }
        // 把购物车中的勾选商品删除
        clearCart(cartVOList);
        // 生成订单
        Order order = new Order();
        // 生成订单号，有一定的规则
        String orderNo = OrderCodeFactory.getOrderCode(Long.valueOf(userId));
        order.setOrderNo(orderNo);
        order.setUserId(userId);
        order.setTotalPrice(totalPrice(orderItemList));
        order.setReceiverMobile(createOrderReq.getReceiverMobile());
        order.setReceiverName(createOrderReq.getReceiverName());
        order.setReceiverAddress(createOrderReq.getReceiverAddress());
        order.setOrderStatus(Constant.OrderStatusEnum.NO_PAY.getCode());
        order.setPostage(0);
        order.setPaymentType(1);
        // 插入到order表
        orderMapper.insertSelective(order);
        // 循环保存每个商品到order_item表
        for (OrderItem orderItem : orderItemList) {
            orderItem.setOrderNo(order.getOrderNo());
            orderItemMapper.insertSelective(orderItem);
        }
        // 把结果返回
        return orderNo;
    }

    private Integer totalPrice(List<OrderItem> orderItemList) {
        Integer totalPrice = 0;
        for (OrderItem orderItem : orderItemList) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
    }

    private void clearCart(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
            cartMapper.deleteByPrimaryKey(cartVO.getId());
        }
    }

    private List<OrderItem> cartVOListToOrderItemList(List<CartVO> cartVOList) {
        List<OrderItem> orderItemList = new ArrayList<>();
        for (CartVO cartVO : cartVOList) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProductId(cartVO.getProductId());
            orderItem.setProductName(cartVO.getProductName());
            orderItem.setProductImg(cartVO.getProductImage());
            orderItem.setUnitPrice(cartVO.getPrice());
            orderItem.setQuantity(cartVO.getQuantity());
            orderItem.setTotalPrice(cartVO.getTotalPrice());
            orderItemList.add(orderItem);
        }
        return orderItemList;
    }

    private void validSaleStatusAndStock(List<CartVO> cartVOList) {
        for (CartVO cartVO : cartVOList) {
            Product product = productMapper.selectByPrimaryKey(cartVO.getProductId());
            // 判断商品是否存在，商品上下架状态，库存
            if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
                throw new MallException(MallExceptionEnum.NOT_SALE);
            }
            // 判断商品库存
            if (product.getStock() < cartVO.getQuantity()) {
                throw new MallException(MallExceptionEnum.NOT_ENOUGH);
            }
        }
    }

    @Override
    public PageInfo listForCustomer(Integer pageNum, Integer pageSize) {
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectForCustomer(userId);
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    private List<OrderVO> orderListToOrderVOList(List<Order> orderList) {
        List<OrderVO> orderVOList = new ArrayList<>();
        for (int i = 0; i < orderList.size(); i++) {
            Order order = orderList.get(i);
            OrderVO orderVO = getOrderVO(order);
            orderVOList.add(orderVO);
        }
        return orderVOList;
    }

    @Override
    public OrderVO detail(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        if (!order.getUserId().equals(userId)) {
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        OrderVO orderVO = getOrderVO(order);
        return orderVO;
    }

    private OrderVO getOrderVO(Order order) {
        OrderVO orderVO = new OrderVO();
        BeanUtils.copyProperties(order, orderVO);
        // 获取订单对应的orderItemVOList
        List<OrderItem> orderItemListTemp = orderItemMapper.selectByOrderNo(order.getOrderNo());
        List<OrderItemVO> orderItemVOList = new ArrayList<>();
        for (OrderItem orderItem : orderItemListTemp) {
            OrderItemVO orderItemVO = new OrderItemVO();
            BeanUtils.copyProperties(orderItem, orderItemVO);
            orderItemVOList.add(orderItemVO);
        }
        orderVO.setOrderItemVOList(orderItemVOList);
        orderVO.setOrderStatusName(Constant.OrderStatusEnum.codeOf(orderVO.getOrderStatus()).getValue());
        return orderVO;
    }

    @Override
    public void cancel(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 查不到订单，报错
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        // 如果订单不属于当前用户，报错
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        if (!order.getUserId().equals(userId)) {
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        // 已经付款，不能取消
        if (!order.getOrderStatus().equals(Constant.OrderStatusEnum.NO_PAY.getCode())) {
            throw new MallException(MallExceptionEnum.CANCEL_WRONG_ORDER_STATUS);
        }
        // 只有未付款的订单可以取消，已付款的订单要走退货流程
        order.setOrderStatus(Constant.OrderStatusEnum.CANCELED.getCode());
        order.setEndTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public String qrcode(String orderNo) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String address = uri;
        String payUrl = "http://" + address + "/pay?orderNo=" + orderNo;
        try {
            QRCodeGenerator.generateQRCodeImage(payUrl, 350, 350, Constant.FILE_UPLOAD_DIR + orderNo + ".png");
        } catch (IOException | WriterException e) {
            e.printStackTrace();
        }
        String pngAddress = "http://" + address + "/images/" + orderNo + ".png";
        return pngAddress;
    }

    @Override
    public PageInfo listForAdmin(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Order> orderList = orderMapper.selectAllForAdmin();
        List<OrderVO> orderVOList = orderListToOrderVOList(orderList);
        PageInfo pageInfo = new PageInfo<>(orderList);
        pageInfo.setList(orderVOList);
        return pageInfo;
    }

    @Override
    public void pay(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 查不到订单，报错
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        // 如果订单不属于当前用户，报错
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        if (!order.getUserId().equals(userId)) {
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        // 已经付款，不能重复支付
        if (!order.getOrderStatus().equals(Constant.OrderStatusEnum.NO_PAY.getCode())) {
            throw new MallException(MallExceptionEnum.PAY_WRONG_ORDER_STATUS);
        }
        // 只有未付款的订单可以支付
        order.setOrderStatus(Constant.OrderStatusEnum.PAID.getCode());
        order.setPayTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public void deliver(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 查不到订单，报错
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        // 已经付款，才能发货
        if (!order.getOrderStatus().equals(Constant.OrderStatusEnum.PAID.getCode())) {
            throw new MallException(MallExceptionEnum.DELIVER_WRONG_ORDER_STATUS);
        }
        // 只有已付款的订单可以发货
        order.setOrderStatus(Constant.OrderStatusEnum.DELIVERED.getCode());
        order.setDeliveryTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public void finish(String orderNo) {
        Order order = orderMapper.selectByOrderNo(orderNo);
        // 查不到订单，报错
        if (order == null) {
            throw new MallException(MallExceptionEnum.NO_ORDER);
        }
        // 如果是普通用户，只能是自己的订单
        User currentUser = UserFilter.userThreadLocal.get();
        if (!userService.checkAdminRole(currentUser) && !order.getUserId().equals(currentUser.getId())) {
            throw new MallException(MallExceptionEnum.NOT_YOUR_ORDER);
        }
        // 已经发货，才能完成
        if (!order.getOrderStatus().equals(Constant.OrderStatusEnum.DELIVERED.getCode())) {
            throw new MallException(MallExceptionEnum.FINISH_WRONG_ORDER_STATUS);
        }
        // 只有已发货的订单可以完成
        order.setOrderStatus(Constant.OrderStatusEnum.TRADE_SUCCESS.getCode());
        order.setEndTime(new Date());
        orderMapper.updateByPrimaryKeySelective(order);
    }

    @Override
    public List<OrderStatisticsVO> statistics(Date startDate, Date endDate) {
        OrderStatisticsQuery orderStatisticsQuery = new OrderStatisticsQuery();
        orderStatisticsQuery.setStartDate(startDate);
        orderStatisticsQuery.setEndDate(endDate);
        return orderMapper.selectOrderStatistics(orderStatisticsQuery);
    }
}
