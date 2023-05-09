package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.*;
import com.example.reggie_take_out.service.CategoryService;
import com.example.reggie_take_out.service.SetmealDishService;
import com.example.reggie_take_out.service.SetmealService;
import com.example.reggie_take_out.mapper.SetmealMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 80954
 * @description 针对表【setmeal(套餐)】的数据库操作Service实现
 * @createDate 2023-05-04 14:42:24
 */
@Slf4j
@Service
public class SetmealServiceImpl extends ServiceImpl<SetmealMapper, Setmeal>
        implements SetmealService {

    @Autowired
    private CategoryService categoryService;
    @Autowired
    private SetmealDishService setmealDishService;

    /**
     * 分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    public R<Page> listSetmeal(Integer page, Integer pageSize, String name) {
        // 构造分页构造器
        Page<Setmeal> setmealPage = new Page<>(page, pageSize);
        // 因为前端需要展示分类的名称，所以封装成DishDto对象
        Page<SetmealDto> setmealDtoPage = new Page(page, pageSize);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        //        如果name不为空，就构造like条件
        wrapper.like(StringUtils.isNotEmpty(name), Setmeal::getName, name);
        wrapper.orderByDesc(Setmeal::getId);
        this.page(setmealPage, wrapper);
        //        将DishPage转换成dishDtoPage
        List<SetmealDto> setmealDtoList = new ArrayList<>();
        setmealPage.getRecords().forEach(setmeal -> {
            SetmealDto setmealDto = new SetmealDto();
//            使用BeanUtils去复制
            BeanUtils.copyProperties(setmeal, setmealDto);
//            获取类别名称
            Category category = categoryService.getById(setmeal.getCategoryId());
            setmealDto.setCategoryName(category.getName());
            setmealDtoList.add(setmealDto);
        });
        BeanUtils.copyProperties(setmealPage, setmealDtoPage, "records");
        setmealDtoPage.setRecords(setmealDtoList);
        return R.success(setmealDtoPage);

    }


    /**
     * 上架子套餐
     *
     * @param setmealDto
     * @return
     */
    public R saveSetmeal(SetmealDto setmealDto) {
        //        保存套餐
        this.save(setmealDto);
        //        获取子套餐id
        String setmealid = String.valueOf(setmealDto.getId());
//        遍历菜品信息，传入子套餐id
        List<SetmealDish> dishList = setmealDto.getSetmealDishes();
        dishList.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealid);
        });
//        保存子套餐内包含的菜品
        setmealDishService.saveBatch(dishList);
        return null;

    }

    /**
     * 删除套餐，同时删除套餐所包含的菜品(可批量删除)
     * @param ids
     * @return
     */

    @Override
    public R<String> deleteSetmeal(List<Long> ids) {
         String msg= null;
//        判断套餐是否处于上架状态
        LambdaQueryWrapper<Setmeal> wrapper2 = new LambdaQueryWrapper<>();
        wrapper2.in(Setmeal::getId, ids);
        wrapper2.eq(Setmeal::getStatus, 1);
        if (this.count(wrapper2)>0){
            return R.error("套餐处于上架状态，无法删除");
        }
        else {
//        删除套餐
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId, ids);
        this.remove(wrapper);
//        删除套餐下的菜品
        LambdaQueryWrapper<SetmealDish> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.in(SetmealDish::getSetmealId, ids);
        this.setmealDishService.remove(wrapper1);
        return R.success("删除成功");
        }
    }


    /**
     * 修改套餐状态
     * @param ids
     * @param status
     * @return
     */
    public R<String> updateStatus(List<Long> ids, Integer status){
        log.info("修改菜品的售卖状态：{}",ids);
        LambdaQueryWrapper<Setmeal> wrapper = new LambdaQueryWrapper<>();
        wrapper.in(Setmeal::getId,ids);
        Setmeal setmeal = new Setmeal();
//       如果status为0，表示禁售请求，将status设置为0
        if (status == 0){
            setmeal.setStatus(0);
            this.update(setmeal,wrapper);
            return R.success(null);
        }
//        如果status为1，表示上架请求，将status设置为1
        setmeal.setStatus(1);
        this.update(setmeal,wrapper);
        return R.success("修改状态成功");
    }

    /**
     * 根据id反查套餐信息
     * @param id
     * @return
     */

   public R<SetmealDto> getSetmeal(Long id){
       //        查询套餐信息
       Setmeal setmeal = this.getById(id);
       SetmealDto setmealDto=new SetmealDto();
       BeanUtils.copyProperties(setmeal,setmealDto);
//        查询套餐下的菜品信息
       LambdaQueryWrapper<SetmealDish> wrapper=new LambdaQueryWrapper<>();
       wrapper.eq(SetmealDish::getSetmealId,setmeal.getId());
       List<SetmealDish> list = this.setmealDishService.list(wrapper);
       setmealDto.setSetmealDishes(list);
       return R.success(setmealDto);
   }

    /**
     * 修改套餐信息
     *
     * @param setmealDto
     */
    @Override
    @Transactional
    public void updateWithFlavor(SetmealDto setmealDto) {
        // 更新套餐表
        this.updateById(setmealDto);
        // 清理当前套餐对应菜品数据（删除操作）
        LambdaQueryWrapper<SetmealDish> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(SetmealDish::getSetmealId, setmealDto.getId());
        this.setmealDishService.remove(queryWrapper);
//        获取套餐ID
        String setmealId = String.valueOf(setmealDto.getId());
        // 加载当前提交过来的口味数据（插入操作）
        List<SetmealDish> setmealDishes = setmealDto.getSetmealDishes();
        setmealDishes.forEach(setmealDish -> {
            setmealDish.setSetmealId(setmealId);
        });
        // 批量更新
        setmealDishService.saveBatch(setmealDishes);
    }
}




