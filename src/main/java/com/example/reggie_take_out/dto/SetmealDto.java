package com.example.reggie_take_out.dto;


import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.entity.SetmealDish;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.List;
//@ApiModel(value = "com-example-reggie_take_out-dto-SetmealDto")
@Data
public class SetmealDto extends Setmeal {
//    @ApiModelProperty(value = "套餐详情列表")
    private List<SetmealDish> setmealDishes;
//    @ApiModelProperty(value = "类别名称")
    private String categoryName;
}
