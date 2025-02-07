package com.fullstack.mall.service.impl;

import com.fullstack.mall.common.Constant;
import com.fullstack.mall.exception.MallException;
import com.fullstack.mall.exception.MallExceptionEnum;
import com.fullstack.mall.model.dao.CartMapper;
import com.fullstack.mall.model.dao.ProductMapper;
import com.fullstack.mall.model.pojo.Cart;
import com.fullstack.mall.model.pojo.Product;
import com.fullstack.mall.model.vo.CartVO;
import com.fullstack.mall.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartServiceImpl implements CartService {
    @Autowired
    CartMapper cartMapper;

    @Autowired
    ProductMapper productMapper;

    @Override
    public List<CartVO> list(Integer userId) {
        List<CartVO> cartVOS = cartMapper.selectList(userId);
        for (CartVO cartVO : cartVOS) {
            cartVO.setTotalPrice(cartVO.getPrice() * cartVO.getQuantity());
        }
        return cartVOS;
    }

    @Override
    public List<CartVO> add(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);
        // 判断商品是否已经在购物车里
        // 如果已经在购物车里，则更新商品数量
        // 如果不在购物车里，则新增一个购物车记录
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 不在购物车里，需要新增一个
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductId(productId);
            cart.setQuantity(count);
            cart.setSelected(Constant.Cart.CHECKED);
            cartMapper.insertSelective(cart);
        } else {
            // 在购物车里，需要更新数量
            count = cart.getQuantity() + count;
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setQuantity(count);
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    private void validProduct(Integer productId, Integer count) {
        Product product = productMapper.selectByPrimaryKey(productId);
        if (product == null || product.getStatus().equals(Constant.SaleStatus.NOT_SALE)) {
            throw new MallException(MallExceptionEnum.NOT_SALE);
        }
        // 判断商品库存
        if (count > product.getStock()) {
            throw new MallException(MallExceptionEnum.NOT_ENOUGH);
        }
    }

    @Override
    public List<CartVO> update(Integer userId, Integer productId, Integer count) {
        validProduct(productId, count);

        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 不在购物车里，无法更新
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        } else {
            // 在购物车里，需要更新数量
            Cart cartNew = new Cart();
            cartNew.setId(cart.getId());
            cartNew.setUserId(cart.getUserId());
            cartNew.setProductId(cart.getProductId());
            cartNew.setQuantity(count);
            cartNew.setSelected(Constant.Cart.CHECKED);
            cartMapper.updateByPrimaryKeySelective(cartNew);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> delete(Integer userId, Integer productId) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 不在购物车里，无法删除
            throw new MallException(MallExceptionEnum.DELETE_FAILED);
        } else {
            // 在购物车里，可以删除
            cartMapper.deleteByPrimaryKey(cart.getId());
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectOrNot(Integer userId, Integer productId, Integer selected) {
        Cart cart = cartMapper.selectCartByUserIdAndProductId(userId, productId);
        if (cart == null) {
            // 不在购物车里，无法选择
            throw new MallException(MallExceptionEnum.UPDATE_FAILED);
        } else {
            // 在购物车里，可以选择
            cartMapper.selectOrNot(userId, productId, selected);
        }
        return this.list(userId);
    }

    @Override
    public List<CartVO> selectAllOrNot(Integer userId, Integer selected) {
        cartMapper.selectOrNot(userId, null, selected);
        return this.list(userId);
    }
}
