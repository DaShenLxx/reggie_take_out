package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.service.CategoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 菜品及套餐分类(Category)表控制层
 *
 * @author makejava
 * @since 2023-05-04 10:23:09
 */
@Slf4j
@RestController
@RequestMapping("category")
@Api(tags = "菜品及套餐分类接口")
public class CategoryController {
    /**
     * 服务对象
     */
    @Resource
    private CategoryService categoryService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @return 所有数据
     */

    @GetMapping("/page")
    public R<Page> listCategory(Integer page,
                                Integer pageSize,
                                String name) {
        log.info("类别信息分页查询：page = {}, pageSize = {}, name = {}", page, pageSize, name);
        return categoryService.listCategory(page, pageSize, name);
    }

    /**
     * 新增分类
     *
     * @param category
     * @return
     */

    @PostMapping
    public R<Category> addCategory(@RequestBody Category category) {
        log.info("新增分类：{}", category);
        this.categoryService.save(category);
        return R.success(category);
    }

    /**
     * 删除分类，逻辑删除
     *
     * @param id
     * @return
     */

    @DeleteMapping
    public R deleteCategory(@RequestParam("ids") Long id) {
        log.info("删除分类：{}", id);
        return this.categoryService.deleteCategory(id);
    }

    /**
     * 修改分类
     *
     * @param category
     * @return
     */

    @PutMapping
    public R updateCategory(@RequestBody Category category) {
        log.info("修改分类：{}", category);
        this.categoryService.updateById(category);
        return R.success(category);
    }


    /**
     * 新增菜品时，查询所有分类
     *
     * @param category
     * @return
     */

    @GetMapping("/list")
    public R<List<Category>> listCategory(Category category) {
        log.info("查询所有分类");
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(category.getType() != null, Category::getType, category.getType());
        wrapper.orderByAsc(Category::getSort)
                .orderByDesc(Category::getUpdateTime);
        return R.success(this.categoryService.list(wrapper));
    }

    /**
     * 访问当前Controller下的异常发生处理
     *
     * @param e
     * @return
     */

    @ExceptionHandler(Exception.class)
    public R<String> globalhandleException(Exception e) {
        System.out.println("==============================");
        System.out.println(e);
        if (e instanceof DuplicateKeyException) {
            log.error(e.getMessage());
            return R.error("分类已存在");
        } else {
            log.error(e.getMessage());
            return R.error("服务器异常");
        }
    }
}

