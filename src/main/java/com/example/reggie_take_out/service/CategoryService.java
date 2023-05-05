package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Category;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.RequestParam;

/**
* @author 80954
* @description 针对表【category(菜品及套餐分类)】的数据库操作Service
* @createDate 2023-05-04 10:12:11
*/
public interface CategoryService extends IService<Category> {
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<Page> listCategory(Integer page, Integer pageSize, String name);
    /**
     * 根据ID删除分类，分类之前需要判断
     * @param id
     */
     R deleteCategory(Long id);
}
