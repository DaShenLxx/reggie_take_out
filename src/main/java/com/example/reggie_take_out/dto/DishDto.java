package com.example.reggie_take_out.dto;

import com.example.reggie_take_out.entity.Dish;
import com.example.reggie_take_out.entity.DishFlavor;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
//@ApiModel(value = "com-example-reggie_take_out-dto-DishDto")
@Data
public class DishDto extends Dish{

//    @ApiModelProperty(value = "口味信息列表")
    /** 菜品口味*/
    private List<DishFlavor> flavors = new ArrayList<>();
//    @ApiModelProperty(value = "类别信息")
    private String categoryName;
//    @ApiModelProperty(value = "copies")
    private Integer copies;
}
