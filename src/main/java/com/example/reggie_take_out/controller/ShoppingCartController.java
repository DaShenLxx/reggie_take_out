package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.ShoppingCart;
import com.example.reggie_take_out.service.ShoppingCartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 购物车(ShoppingCart)表控制层
 *
 * @author makejava
 * @since 2023-05-12 16:52:40
 */
@RestController
@RequestMapping("shoppingCart")
public class ShoppingCartController {
    /**
     * 服务对象
     */
    @Resource
    private ShoppingCartService shoppingCartService;
    @Autowired
    private HttpSession session;

    //添加到购物车(在购物车点击加号也是这个接口)
    @PostMapping("add")
    public R<ShoppingCart> add(@RequestBody ShoppingCart shoppingCart) {
        //获取当前用户的id
        Long currentId = BaseContext.getCurrentId();
        shoppingCart.setUserId(currentId);

//        查询当前发送的菜品或者套餐是否在该用户的购物车中
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, shoppingCart.getUserId());
        Long dishId = shoppingCart.getDishId();

//        判断dishid是否为空，为空则发送的是套餐
        if (dishId != null) {
            wrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(wrapper);
        if (shoppingCart1 != null) {
            //如果存在，则更新数量
            shoppingCart1.setNumber(shoppingCart1.getNumber() + 1);
            shoppingCartService.updateById(shoppingCart1);
        } else {
            //如果不存在，则添加
            shoppingCart.setNumber(1);
            shoppingCartService.save(shoppingCart);
            shoppingCart1 = shoppingCart;
        }

        return R.success(shoppingCart1);

    }

//    查看购物车
    @GetMapping("list")
    public R<List<ShoppingCart>> shoppinglist() {
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        wrapper.orderByAsc(ShoppingCart::getCreateTime);

//
//        查询当前用户的购物车中数量大于0的商品
        wrapper.gt(ShoppingCart::getNumber, 0);
//
        List<ShoppingCart> list = shoppingCartService.list(wrapper);
        return R.success(list);
    }


//    减少商品，在购物车点击减号
    @PostMapping("sub")
    public R<String> reduce(@RequestBody ShoppingCart shoppingCart) {
        //获取当前用户的id
        Long userId = BaseContext.getCurrentId();
        LambdaQueryWrapper<ShoppingCart> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ShoppingCart::getUserId, userId);
        Long dishId = shoppingCart.getDishId();
        if (dishId != null){
            wrapper.eq(ShoppingCart::getDishId, dishId);
        } else {
            wrapper.eq(ShoppingCart::getSetmealId, shoppingCart.getSetmealId());
        }
        ShoppingCart shoppingCart1 = shoppingCartService.getOne(wrapper);
        shoppingCart1.setNumber(shoppingCart1.getNumber() - 1);
        this.shoppingCartService.updateById(shoppingCart1);
        return R.success("减少成功");
    }


    /**
     * 清空购物车
     * @return
     */
    @DeleteMapping("/clean")
    public R<String> clean(){

        LambdaQueryWrapper<ShoppingCart> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ShoppingCart::getUserId,BaseContext.getCurrentId());
        shoppingCartService.remove(queryWrapper);
        return R.success("清空购物车成功");

    }

}

