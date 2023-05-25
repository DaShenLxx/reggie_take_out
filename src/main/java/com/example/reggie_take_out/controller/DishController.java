package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.DishFlavor;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.DishFlavorService;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.utils.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 菜品管理(Dish)表控制层
 *
 * @author makejava
 * @since 2023-05-05 22:19:10
 */
@Api(tags = "菜品管理接口")
@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishService dishService;

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询所有数据
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public R listDish(Integer page, Integer pageSize, String name) {
        log.info("菜品信息分页查询：page = {}, pageSize = {}, name = {}", page, pageSize, name);
        return dishService.listDish(page, pageSize, name);
    }



    /**
     * 删除菜品，逻辑删除(可批量删除)
     * @param id
     * @return
     */

    @DeleteMapping
    public R<String> deleteDish(@RequestParam("ids") List<Long> id) {
        log.info("删除菜品：id = {}", id);
        return this.dishService.deleteDish(id);
    }

    /**
     * 修改售卖状态(可批量修改)
     *
     * @param status
     * @param id
     * @return
     */

    @PostMapping("/status/{status}")
    public R updateDishStatus(@PathVariable("status") Integer status, @RequestParam("ids") List<Long> id) {
        log.info("修改菜品的售卖状态：{}", id);
        return R.success(this.dishService.updateDishStatus(id, status));
    }

    /**
     * 新增菜品信息
     *
     * @param dishDto
     * @return
     */

    @PostMapping
    public R<String> insertDish(@RequestBody DishDto dishDto) {
        log.info("新增菜品信息：{}", dishDto);
        this.dishService.saveDish(dishDto);
        return R.success("菜品新增成功");
    }

    /**
     * 根据ID查询菜品信息以及对应的口味信息
     *
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R<DishDto> get(@PathVariable Long id) {
        DishDto dishDto = dishService.getByIdWithFlavor(id);
        return R.success(dishDto);
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     * @return R<String>
     */

    @PutMapping
    public R<String> update(@RequestBody DishDto dishDto) {
        log.info("修改菜品信息{}", dishDto);
        dishService.updateWithFlavor(dishDto);
        return R.success("菜品修改成功");
    }

    /**
     * 根据菜品分类id查询菜品列表
     *
     * @param dish
     * @return
     */

    @GetMapping("/list")
    public R<List<DishDto>> list(Dish dish){
        return this.dishService.list(dish);
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
            return R.error("菜品已存在");
        } else {
            log.error(e.getMessage());
            return R.error("服务器异常");
        }
    }

}

