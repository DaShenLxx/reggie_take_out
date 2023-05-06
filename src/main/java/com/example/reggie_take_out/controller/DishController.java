package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.service.DishService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 菜品管理(Dish)表控制层
 *
 * @author makejava
 * @since 2023-05-05 22:19:10
 */
@Slf4j
@RestController
@RequestMapping("dish")
public class DishController {
    @Autowired
    private DishService dishService;

    /**
     * 分页查询所有数据
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
     * 删除菜品，逻辑删除
     * @param id
     * @return
     */
    @DeleteMapping
    public R deleteDish(@RequestParam("ids") Long id) {
        log.info("删除菜品：id = {}", id);
        this.dishService.removeById(id);
        return R.success(null);
    }

    /**
     * 修改售卖状态
     * @param status
     * @param id
     * @return
     */
    @PostMapping("/status/{status}")
    public R updateDishStatus(@PathVariable("status") Integer status,@RequestParam("ids") Long id){
        log.info("修改菜品的售卖状态：{}",id);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Dish::getId,id);
        Dish dish = new Dish();
//       如果status为0，表示禁售请求，将status设置为0
        if (status == 0){
            dish.setStatus(0);
            this.dishService.update(dish,wrapper);
            return R.success(null);
        }
//        如果status为1，表示上架请求，将status设置为1
        dish.setStatus(1);
        this.dishService.update(dish,wrapper);


        return R.success(null);
    }

}

