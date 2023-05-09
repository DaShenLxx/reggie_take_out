package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.DishFlavor;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.entity.SetmealDish;
import com.example.reggie_take_out.service.SetmealDishService;
import com.example.reggie_take_out.service.SetmealService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * 套餐(Setmeal)表控制层
 *
 * @author makejava
 * @since 2023-05-08 11:22:34
 */
@Slf4j
@RestController
@RequestMapping("setmeal")
public class SetmealController {
    /**
     * 服务对象
     */
    @Resource
    private SetmealService setmealService;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分页查询所有数据
     *
     * @param page 分页对象
     * @return 所有数据
     */

    @GetMapping("/page")
    public R<Page> listSetmeal(Integer page,
                               Integer pageSize,
                               String name) {
        log.info("菜品信息分页查询：page = {}, pageSize = {}, name = {}", page, pageSize, name);
        return setmealService.listSetmeal(page, pageSize, name);
    }

    /**
     * 上传子套餐
     */
    @PostMapping
    public R saveSetmeal(@RequestBody SetmealDto setmealDto) {
        log.info("上传子套餐：setmeal = {}", setmealDto);
        this.setmealService.saveSetmeal(setmealDto);
        return R.success("套餐添加成功");
    }

    /**
     * 删除套餐(可批量删除)
     */
    @DeleteMapping
    public R deleteSetmeal(@RequestParam("ids") List<Long> ids) {
        log.info("删除套餐：id = {}", ids);
        return this.setmealService.deleteSetmeal(ids);
    }

    /**
     * 修改套餐状态
     *
     * @param ids
     * @param status
     * @return
     */
    @PostMapping("/status/{status}")
    public R updateStatus(@RequestParam("ids") List<Long> ids,
                          @PathVariable("status") Integer status) {
        log.info("修改套餐状态：id = {}, status = {}", ids, status);
        return R.success(this.setmealService.updateStatus(ids, status));
    }

    /**
     * 修改套餐时反查套餐信息
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public R<SetmealDto> getSetmeal(@PathVariable("id") Long id) {
        log.info("修改套餐反查：id = {}", id);
        return this.setmealService.getSetmeal(id);
    }

    /**
     * 修改套餐
     * @param setmealDto
     * @return
     */
    @PutMapping
    public R<String> update(@RequestBody SetmealDto setmealDto) {
        log.info("修改菜品信息{}",setmealDto);
        this.setmealService.updateWithFlavor(setmealDto);
        return R.success("套餐修改成功");
    }
}

