package com.example.reggie_take_out.mapper;

import com.example.reggie_take_out.entity.Category;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 80954
* @description 针对表【category(菜品及套餐分类)】的数据库操作Mapper
* @createDate 2023-05-04 10:12:11
* @Entity com.example.reggie_take_out.entity.Category
*/
@Mapper
public interface CategoryMapper extends BaseMapper<Category> {

}




