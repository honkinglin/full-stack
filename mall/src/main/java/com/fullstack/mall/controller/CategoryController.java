package com.fullstack.mall.controller;

import com.fullstack.mall.common.ApiRestResponse;
import com.fullstack.mall.common.Constant;
import com.fullstack.mall.exception.MallExceptionEnum;
import com.fullstack.mall.model.pojo.Category;
import com.fullstack.mall.model.pojo.User;
import com.fullstack.mall.model.request.AddCategoryReq;
import com.fullstack.mall.model.request.UpdateCategoryReq;
import com.fullstack.mall.model.vo.CategoryVO;
import com.fullstack.mall.service.CategoryService;
import com.fullstack.mall.service.UserService;
import com.github.pagehelper.PageInfo;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 描述：分类 Controller
 */
@RestController
public class CategoryController {
    @Autowired
    UserService userService;

    @Autowired
    CategoryService categoryService;

    /**
     * 添加分类
     *
     * @param session
     * @param addCategoryReq
     * @return
     */
    @PostMapping("/admin/category/add")
    public ApiRestResponse addCategory(HttpSession session, @Valid @RequestBody AddCategoryReq addCategoryReq) {
        User currentUser = (User) session.getAttribute(Constant.MALL_USER);
        if (currentUser == null) {
            return ApiRestResponse.error(MallExceptionEnum.NEED_LOGIN);
        }
        if (userService.checkAdminRole(currentUser)) {
            categoryService.add(addCategoryReq);
            return ApiRestResponse.success();
        } else {
            return ApiRestResponse.error(MallExceptionEnum.NEED_ADMIN);
        }
    }

    @PostMapping("/admin/category/update")
    public ApiRestResponse updateCategory(@Valid @RequestBody UpdateCategoryReq updateCategoryReq,
                                          HttpSession session) {
        Category category = new Category();
        BeanUtils.copyProperties(updateCategoryReq, category);
        categoryService.update(category);
        return ApiRestResponse.success();
    }

    @PostMapping("/admin/category/delete")
    public ApiRestResponse deleteCategory(@RequestParam("id") Integer id) {
        categoryService.delete(id);
        return ApiRestResponse.success();
    }

    @GetMapping("/admin/category/list")
    public ApiRestResponse listCategoryForAdmin(@RequestParam("pageNum") Integer pageNum,
                                                @RequestParam("pageSize") Integer pageSize) {
        PageInfo pageInfo = categoryService.listForAdmin(pageNum, pageSize);
        return ApiRestResponse.success(pageInfo);
    }

    @GetMapping("/category/list")
    public ApiRestResponse listCategoryForCustomer() {
        List<CategoryVO> categoryVOS = categoryService.listCategoryForCustomer(0);
        return ApiRestResponse.success(categoryVOS);
    }
}
