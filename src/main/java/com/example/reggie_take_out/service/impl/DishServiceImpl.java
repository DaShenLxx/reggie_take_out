package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.entity.Category;
import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.DishFlavor;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.DishFlavorService;
import com.example.reggie_take_out.service.DishService;
import com.example.reggie_take_out.mapper.DishMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author 80954
 * @description 针对表【dish(菜品管理)】的数据库操作Service实现
 * @createDate 2023-05-04 14:42:24
 */
@Service
public class DishServiceImpl extends ServiceImpl<DishMapper, Dish>
        implements DishService {

    @Autowired
    private DishFlavorService dishFlavorService;

    @Autowired
    private CategoryService categoryService;

    /**
     * 分页查询菜品
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> listDish(Integer page, Integer pageSize, String name) {
        // 构造分页构造器
        Page<Dish> dishPage = new Page<>(page, pageSize);
        // 因为前端需要展示分类的名称，所以封装成DishDto对象
        Page<DishDto> dishDtoPage = new Page(page, pageSize);
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        //        如果name不为空，就构造like条件
        wrapper.like(StringUtils.isNotEmpty(name), Dish::getName, name);
        wrapper.orderByDesc(Dish::getId);
        this.page(dishPage, wrapper);
        //        将DishPage转换成dishDtoPage
        List<DishDto> dishDtoList = new ArrayList<>();
        dishPage.getRecords().forEach(dish -> {
            DishDto dishDto = new DishDto();
//            使用BeanUtils去复制
            BeanUtils.copyProperties(dish, dishDto);
//            获取类别名称
            Category category = categoryService.getById(dish.getCategoryId());
            dishDto.setCategoryName(category.getName());
            dishDtoList.add(dishDto);
        });
        BeanUtils.copyProperties(dishPage, dishDtoPage,"records");
        dishDtoPage.setRecords(dishDtoList);
        return R.success(dishDtoPage);
    }

    /**
     * 新增菜品,新增菜品时，需要将菜品的口味信息也一并新增
     *
     * @param dishDto
     * @return
     */
    @Override
    @Transactional
    public R saveDish(DishDto dishDto) {
//        保存菜品信息
        this.save(dishDto);
//        获取菜品的id
        Long dishId = dishDto.getId();
//        获取菜品的口味信息
        List<DishFlavor> list = dishDto.getFlavors();
//        遍历口味信息，设置菜品id，保存口味信息
        list.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
//        list.forEach(System.out::println);
//        保存口味信息
        dishFlavorService.saveBatch(list);
        return null;
    }


    /**
     * 根据ID查询菜品信息以及对应的口味信息
     *
     * @param id
     * @return
     */
    @Override
    public DishDto getByIdWithFlavor(Long id) {
        // 查询菜品基本信息，从dish表查询
        Dish dish = this.getById(id);
        DishDto dishDto = new DishDto();
        BeanUtils.copyProperties(dish, dishDto);
        // 查询对应的口味信息
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dish.getId());
        List<DishFlavor> list = dishFlavorService.list(queryWrapper);
        dishDto.setFlavors(list);
        return dishDto;
    }

    /**
     * 修改菜品
     *
     * @param dishDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(DishDto dishDto) {
        // 更新菜品表
        this.updateById(dishDto);
        // 清理当前菜品对应口味数据（删除操作）
        LambdaQueryWrapper<DishFlavor> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(DishFlavor::getDishId, dishDto.getId());
        dishFlavorService.remove(queryWrapper);
//        获取菜品ID
        Long dishId = dishDto.getId();
        // 加载当前提交过来的口味数据（插入操作）
        List<DishFlavor> flavors = dishDto.getFlavors();
        flavors.forEach(dishFlavor -> {
            dishFlavor.setDishId(dishId);
        });
        // 批量更新
        dishFlavorService.saveBatch(flavors);
    }

    /**
     * 修改菜品状态(可批量修改)
     * @param id
     * @param status
     * @return
     */
    @Override
    public R<String> updateDishStatus(List<Long> id, Integer status) {
        LambdaQueryWrapper<Dish> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Dish::getId,id);
        Dish dish = new Dish();
//       如果status为0，表示禁售请求，将status设置为0
        if (status == 0){
            dish.setStatus(0);
            this.update(dish,wrapper);
            return R.success(null);
        }
//        如果status为1，表示上架请求，将status设置为1
        dish.setStatus(1);
        this.update(dish,wrapper);
        return R.success("修改状态成功");
    }
}




