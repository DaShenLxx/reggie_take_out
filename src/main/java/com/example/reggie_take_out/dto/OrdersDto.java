package com.example.reggie_take_out.dto;

import com.example.reggie_take_out.entity.OrderDetail;
import com.example.reggie_take_out.entity.Orders;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDto extends Orders {

//    获取ordersDetail的number和name对象
//    private Integer number;
    private List<OrderDetail> orderDetails;

}
