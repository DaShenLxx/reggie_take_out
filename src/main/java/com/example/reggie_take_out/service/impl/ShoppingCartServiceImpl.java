package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.ShoppingCart;
import com.example.reggie_take_out.service.ShoppingCartService;
import com.example.reggie_take_out.mapper.ShoppingCartMapper;
import org.springframework.stereotype.Service;

/**
* @author 80954
* @description 针对表【shopping_cart(购物车)】的数据库操作Service实现
* @createDate 2023-05-12 16:51:11
*/
@Service
public class ShoppingCartServiceImpl extends ServiceImpl<ShoppingCartMapper, ShoppingCart>
    implements ShoppingCartService{

}




