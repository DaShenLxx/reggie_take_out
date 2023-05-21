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
import com.example.reggie_take_out.utils.RedisUtil;
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

    /*项目优化*/
    @Autowired
    private RedisUtil redisUtil;
    /*项目优化*/

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

        /*项目优化*/
//        清理所有菜品的缓存数据
//        Set<String> keys = redisUtil.keys("dish_*");
//        redisUtil.del(keys);
//        清理当前菜品所属分类的缓存数据
        String catrgoryid=dishDto.getCategoryId().toString();
        String key = "dish_"+catrgoryid+"_"+dishDto.getStatus();
        redisUtil.del(key);
        /*项目优化*/


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

        /*项目优化*/
//        清理所有菜品的缓存数据
//        Set<String> keys = redisUtil.keys("dish_*");
//        redisUtil.del(keys);
//        清理当前菜品所属分类的缓存数据
        String catrgoryid=dishDto.getCategoryId().toString();
        String key = "dish_"+catrgoryid+"_"+dishDto.getStatus();
        redisUtil.del(key);
        /*项目优化*/


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

    /**
     * 删除菜品(可批量删除)
     * @param id
     * @return
     */
    @Override
    public R<String> deleteDish(List<Long> id) {
        //        判断套餐是否处于上架状态
        LambdaQueryWrapper<Dish> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.in(Dish::getId, id);
        wrapper2.eq(Dish::getStatus, 1);
        if (this.count(wrapper2)>0){
            return R.error("菜品处于上架状态，无法删除");
        }
        else {
            LambdaQueryWrapper<Dish> wrapper=new LambdaQueryWrapper<>();
            wrapper.in(Dish::getId,id);
            this.remove(wrapper);
            return R.success("菜品删除成功");
        }
    }

    /**
     * 据菜品分类id查询菜品列表
     * @param dish
     * @return
     */
    public R<List<DishDto>> list(Dish dish){
        List<DishDto> dishDtoList = null;
        /*项目优化*/
//        先从redis中获取数据
//        动态拼接key
        String key = "dish_"+dish.getCategoryId()+"_"+dish.getStatus();
        dishDtoList = (List<DishDto>) redisUtil.get(key);
//        如果redis中有数据，就直接返回
        if (dishDtoList != null){
            return R.success(dishDtoList);
        }
//        如果redis中没有数据，就从数据库中获取数据
        /*项目优化*/


        // 构造条件
        LambdaQueryWrapper<Dish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Dish::getStatus, 1);
        queryWrapper.eq(dish.getCategoryId()!=null, Dish::getCategoryId, dish.getCategoryId());
        // 添加排序条件
        queryWrapper.orderByAsc(Dish::getSort).orderByDesc(Dish::getUpdateTime);
        List<Dish> dishList = this.list(queryWrapper);

        dishDtoList = dishList.stream().map((item)->{
            DishDto dishDto = new DishDto();
            BeanUtils.copyProperties(item,dishDto);
            // 获取categoryId
            Long categoryId = item.getCategoryId();
            // 给categoryName赋值
            Category category = categoryService.getById(categoryId);
            if(category!=null){
                dishDto.setCategoryName(category.getName());
            }
            // 当前菜品ID
            Long dishId = item.getId();
            LambdaQueryWrapper<DishFlavor> dishFlavorQueryWrapper= new LambdaQueryWrapper<>();
            dishFlavorQueryWrapper.eq(DishFlavor::getDishId,dishId);
            List<DishFlavor> dishFlavorList = dishFlavorService.list(dishFlavorQueryWrapper);
            dishDto.setFlavors(dishFlavorList);
            return dishDto;
        }).collect(Collectors.toList());


        /*项目优化*/
        // 将数据存入redis
        redisUtil.set(key,dishDtoList,60*60);
        /*项目优化*/
        return R.success(dishDtoList);
    }
}




