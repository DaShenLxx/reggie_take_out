package com.example.reggie_take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.dto.OrdersDto;
import com.example.reggie_take_out.dto.SetmealDto;
import com.example.reggie_take_out.entity.OrderDetail;
import com.example.reggie_take_out.entity.Orders;
import com.example.reggie_take_out.entity.Setmeal;
import com.example.reggie_take_out.service.OrderDetailService;
import com.example.reggie_take_out.service.OrdersService;
import org.apache.commons.lang.StringUtils;
import org.omg.PortableInterceptor.INACTIVE;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单表(Orders)表控制层
 *
 * @author makejava
 * @since 2023-05-12 18:20:49
 */
@RestController
@RequestMapping("order")
public class OrdersController {
    /**
     * 服务对象
     */
    @Resource
    private OrdersService ordersService;
    @Autowired
    private HttpSession session;
    @Autowired
    private OrderDetailService orderDetailService;

//    提交订单
    @PostMapping("submit")
    public R<String> submit(@RequestBody Orders orders){
        this.ordersService.submit(orders);
        return R.success("下单成功");

}

//客户端查看订单(分页查询)
    @GetMapping("userPage")
    public R<Page> userPage(Integer page, Integer pageSize){
        // 构造分页构造器
        Page<Orders> ordersPage = new Page<>(page, pageSize);
        // 因为前端需要订单详情的名称和数量，所以封装成OrdersDto对象
        Page<OrdersDto> ordersDtoPage = new Page(page, pageSize);
        LambdaQueryWrapper<Orders> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Orders::getUserId, BaseContext.getCurrentId());
        wrapper.orderByDesc(Orders::getOrderTime);
        this.ordersService.page(ordersPage, wrapper);
        //将ordersPage转换成ordersDtoPage
        List<OrdersDto> OrdersDtoList = new ArrayList<>();
        ordersPage.getRecords().forEach(orders -> {
            OrdersDto ordersDto = new OrdersDto();
            // 使用BeanUtils去复制
            BeanUtils.copyProperties(orders, ordersDto);
//            获取订单id
            Long ordersId = orders.getId();
//            获取订单详情
            LambdaQueryWrapper<OrderDetail> wrapper1 = new LambdaQueryWrapper<>();
            wrapper1.eq(OrderDetail::getOrderId,ordersId);
            ordersDto.setOrderDetails(this.orderDetailService.list(wrapper1));
            OrdersDtoList.add(ordersDto);
        });
        BeanUtils.copyProperties(ordersPage, ordersDtoPage,"records");
        ordersDtoPage.setRecords(OrdersDtoList);
        return R.success(ordersDtoPage);
    }


//    后台订单界面分页查询
    @GetMapping("/page")
    public R<Page> page(int page,int pageSize,String number,String beginTime,String endTime){
        //分页构造器对象
        Page<Orders> pageInfo=new Page<>(page,pageSize);
        //构造条件查询对象
        LambdaQueryWrapper<Orders> queryWrapper=new LambdaQueryWrapper<>();
        //添加查询条件  动态sql  字符串使用StringUtils.isNotEmpty这个方法来判断
        //这里使用了范围查询的动态SQL，这里是重点！！！
        queryWrapper.like(number!=null,Orders::getNumber,number)
                .gt(StringUtils.isNotEmpty(beginTime),Orders::getOrderTime,beginTime)
                .lt(StringUtils.isNotEmpty(endTime),Orders::getOrderTime,endTime);
        ordersService.page(pageInfo,queryWrapper);
        return R.success(pageInfo);
    }

//    更新状态
    @PutMapping
    public R<String> updetestatus(@RequestBody Orders orders){


        this.ordersService.updateById(orders);
        return R.success("订单状态修改成功！");

    }


}

