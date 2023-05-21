package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.Dish;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

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


    /**
     * 新增菜品,新增菜品时，需要将菜品的口味信息也一并新增
     * @param dishDto
     * @return
     */
    R saveDish(DishDto dishDto);

    /**
     * 根据ID查询菜品信息以及对应的口味信息
     * @param id
     * @return
     */
    public DishDto getByIdWithFlavor(Long id);

    /**
     * 修改菜品
     * @param dishDto
     */
    public void updateWithFlavor(DishDto dishDto);

    /**
     * 更新菜品状态
     * @param id
     * @param status
     * @return
     */
    public R<String> updateDishStatus(List<Long> id, Integer status);

    /**
     * 删除菜品
     * @param id
     * @return
     */
    R<String> deleteDish(List<Long> id);

    /**
     * 根据菜品分类id查询菜品列表
     *
     * @param dish
     * @return
     */
    public R<List<DishDto>> list(Dish dish);
}
