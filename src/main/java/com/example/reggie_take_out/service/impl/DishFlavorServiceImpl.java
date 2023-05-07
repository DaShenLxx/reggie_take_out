package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.DishFlavor;
import com.example.reggie_take_out.service.DishFlavorService;
import com.example.reggie_take_out.mapper.DishFlavorMapper;
import org.springframework.stereotype.Service;

/**
* @author 80954
* @description 针对表【dish_flavor(菜品口味关系表)】的数据库操作Service实现
* @createDate 2023-05-07 00:13:18
*/
@Service
public class DishFlavorServiceImpl extends ServiceImpl<DishFlavorMapper, DishFlavor>
    implements DishFlavorService{

}




