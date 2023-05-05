package com.example.reggie_take_out.mapper;

import com.example.reggie_take_out.entity.Dish;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 80954
* @description 针对表【dish(菜品管理)】的数据库操作Mapper
* @createDate 2023-05-04 14:42:24
* @Entity com.example.reggie_take_out.entity.Dish
*/
@Mapper
public interface DishMapper extends BaseMapper<Dish> {

}




