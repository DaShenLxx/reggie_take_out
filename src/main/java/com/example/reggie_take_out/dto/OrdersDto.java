package com.example.reggie_take_out.dto;

import com.example.reggie_take_out.entity.OrderDetail;
import com.example.reggie_take_out.entity.Orders;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

//@ApiModel(value = "com-example-reggie_take_out-dto-OrdersDto")
@Data
public class OrdersDto extends Orders {

    //    获取ordersDetail的number和name对象
//    private Integer number;
//    @ApiModelProperty(value = "订单详情列表")
    private List<OrderDetail> orderDetails;

}
