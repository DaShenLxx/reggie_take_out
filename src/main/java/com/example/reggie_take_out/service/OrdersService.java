package com.example.reggie_take_out.service;

import com.example.reggie_take_out.entity.Orders;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 80954
* @description 针对表【orders(订单表)】的数据库操作Service
* @createDate 2023-05-12 18:20:39
*/
public interface OrdersService extends IService<Orders> {
    public void submit(Orders orders);
}
