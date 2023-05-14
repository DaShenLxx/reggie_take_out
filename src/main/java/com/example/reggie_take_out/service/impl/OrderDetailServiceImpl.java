package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.OrderDetail;
import com.example.reggie_take_out.service.OrderDetailService;
import com.example.reggie_take_out.mapper.OrderDetailMapper;
import org.springframework.stereotype.Service;

/**
* @author 80954
* @description 针对表【order_detail(订单明细表)】的数据库操作Service实现
* @createDate 2023-05-12 18:20:39
*/
@Service
public class OrderDetailServiceImpl extends ServiceImpl<OrderDetailMapper, OrderDetail>
    implements OrderDetailService{

}




