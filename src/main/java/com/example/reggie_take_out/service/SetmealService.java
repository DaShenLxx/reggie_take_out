package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.DishDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.Setmeal;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
* @author 80954
* @description 针对表【setmeal(套餐)】的数据库操作Service
* @createDate 2023-05-04 14:42:24
*/
public interface SetmealService extends IService<Setmeal> {
    /**
     * 分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<Page> listSetmeal(Integer page, Integer pageSize, String name);

    /**
     * 上传子套餐
     * @param setmealDto
     * @return
     */
    R saveSetmeal(SetmealDto setmealDto);

    /**
     * 删除套餐(可批量删除)
     * @param ids
     * @return
     */
    R<String> deleteSetmeal(List<Long> ids);

    /**
     * 修改套餐状态
     * @param ids
     * @param status
     * @return
     */
    R<String> updateStatus(List<Long> ids, Integer status);


    /**
     * 根据id反查套餐信息
     * @param id
     * @return
     */
    R<SetmealDto> getSetmeal(Long id);

    /**
     * 修改套餐
     * @param setmealDto
     */
    public void updateWithFlavor(SetmealDto setmealDto);
}
