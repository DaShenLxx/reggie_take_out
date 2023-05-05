package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 80954
* @description 针对表【dish(菜品管理)】的数据库操作Service
* @createDate 2023-05-04 14:42:24
*/
public interface DishService extends IService<Dish> {
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<Page> listDish (Integer page, Integer pageSize, String name);
}
