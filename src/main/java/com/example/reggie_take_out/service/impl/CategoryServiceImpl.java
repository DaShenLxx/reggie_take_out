package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.mapper.CategoryMapper;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
* @author 80954
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service实现
* @createDate 2023-05-04 10:12:11
*/
@Slf4j
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
    implements CategoryService{
    /**
     * 注入Mapper对象
     */
    @Autowired
    private DishService dishService;
    @Autowired
    private SetmealService setmealService;

    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> listCategory(Integer page, Integer pageSize, String name){
        Page<Category> categoryPage = new Page<>(page, pageSize);
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByAsc(Category::getSort);
        Page<Category> result = this.page(categoryPage, wrapper);
        List<Category> records = categoryPage.getRecords();
        return R.success(categoryPage);
    }

    /**
     * 根据ID删除分类，分类之前需要判断
     * @param id
     */
    @Override
    public R deleteCategory(Long id) {
        //判断菜品表中是否有该分类的菜品
        LambdaQueryWrapper<Dish> dishLambdaQueryWrapperwrapper = new LambdaQueryWrapper<>();
        dishLambdaQueryWrapperwrapper.eq(Dish::getCategoryId, id);
        if (dishService.count(dishLambdaQueryWrapperwrapper)>0){
            return R.error("该分类下有菜品，不能删除");

//            也可以使用业务异常处理
//            throw new CustomeException("该分类下有菜品，不能删除");

        }

        LambdaQueryWrapper<Setmeal> setmealLambdaQueryWrapper = new LambdaQueryWrapper<>();
        setmealLambdaQueryWrapper.eq(Setmeal::getCategoryId, id);
        if (setmealService.count(setmealLambdaQueryWrapper)>0){
            return R.error("该分类下有套餐，不能删除");
        }
        this.removeById(id);
        return R.success("删除成功");
    }


}




