package com.fullstack.mall.controller;

import com.fullstack.mall.common.ApiRestResponse;
import com.fullstack.mall.filter.UserFilter;
import com.fullstack.mall.model.pojo.User;
import com.fullstack.mall.model.vo.CartVO;
import com.fullstack.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：购物车 Controller
 */
@RestController
@RequestMapping("/cart")
public class CartController {
    @Autowired
    CartService cartService;

    @GetMapping("/list")
    public ApiRestResponse list() {
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        List<CartVO> cartVOList = cartService.list(userId);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/add")
    public ApiRestResponse add(@RequestParam Integer productId,@RequestParam Integer count) {
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        List<CartVO> cartVOList = cartService.add(userId, productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/update")
    public ApiRestResponse update(@RequestParam Integer productId, @RequestParam Integer count) {
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        List<CartVO> cartVOList = cartService.update(userId, productId, count);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/delete")
    public ApiRestResponse delete(@RequestParam Integer productId) {
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        List<CartVO> cartVOList = cartService.delete(userId, productId);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/select")
    public ApiRestResponse select(@RequestParam Integer productId, @RequestParam Integer selected) {
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        List<CartVO> cartVOList = cartService.selectOrNot(userId, productId, selected);
        return ApiRestResponse.success(cartVOList);
    }

    @PostMapping("/selectAll")
    public ApiRestResponse selectAll(@RequestParam Integer selected) {
        User currentUser = UserFilter.userThreadLocal.get();
        Integer userId = currentUser.getId();
        List<CartVO> cartVOList = cartService.selectAllOrNot(userId, selected);
        return ApiRestResponse.success(cartVOList);
    }
}
