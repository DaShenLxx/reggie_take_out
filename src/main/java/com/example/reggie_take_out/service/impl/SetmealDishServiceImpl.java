package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.SetmealDish;
import com.example.reggie_take_out.service.SetmealDishService;
import com.example.reggie_take_out.mapper.SetmealDishMapper;
import org.springframework.stereotype.Service;

/**
* @author 80954
* @description 针对表【setmeal_dish(套餐菜品关系)】的数据库操作Service实现
* @createDate 2023-05-08 11:30:07
*/
@Service
public class SetmealDishServiceImpl extends ServiceImpl<SetmealDishMapper, SetmealDish>
    implements SetmealDishService{

}




