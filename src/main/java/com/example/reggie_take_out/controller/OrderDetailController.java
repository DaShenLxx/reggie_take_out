package com.example.reggie_take_out.controller;

import com.example.reggie_take_out.service.OrderDetailService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;

/**
 * 订单明细表(OrderDetail)表控制层
 *
 * @author makejava
 * @since 2023-05-12 18:20:49
 */
@RestController
@RequestMapping("orderDetail")
public class OrderDetailController {
    /**
     * 服务对象
     */
    @Resource
    private OrderDetailService orderDetailService;


}

