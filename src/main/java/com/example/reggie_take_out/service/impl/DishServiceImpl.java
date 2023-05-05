package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.mapper.DishMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 80954
* @description 针对表【dish(菜品管理)】的数据库操作Service实现
* @createDate 2023-05-04 14:42:24
*/
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
    implements DishService{
    /**
     * 分页查询菜品
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> listDish(Integer page, Integer pageSize, String name) {
        Page<Dish> DishPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //        如果name不为空，就构造like条件
        wrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        wrapper.orderByDesc(Dish::getId);
        Page<Dish> result = this.page(DishPage, wrapper);
        List<Dish> records = DishPage.getRecords();
        return R.success(DishPage);
    }
}




