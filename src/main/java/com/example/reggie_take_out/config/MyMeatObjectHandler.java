package com.example.reggie_take_out.config;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;

/**
 * 自定义填充公共字段
 * 1.实现MetaObjectHandler接口
 * 2.重写insertFill和updateFill方法
 * 3.在启动类中配置
 */
@Component
public class MyMeatObjectHandler implements MetaObjectHandler {
    @Autowired
    HttpServletRequest httpServletRequestrequest;
    @Override
    public void insertFill(MetaObject metaObject) {
        HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Long loginEmployeeId = (Long) session.getAttribute("loginEmployeeId");
        this.setFieldValByName("updateUser", loginEmployeeId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
        this.setFieldValByName("createUser", loginEmployeeId, metaObject);
        this.setFieldValByName("createTime",new Date(), metaObject);
//        也可以用下面的方法，但是不推荐，因为这样写会导致每次插入都会执行一次sql语句
//        metaObject.setValue("createTime",new Date());
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        HttpServletRequest request= ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
        HttpSession session = request.getSession();
        Long loginEmployeeId = (Long) session.getAttribute("loginEmployeeId");
        this.setFieldValByName("updateUser", loginEmployeeId, metaObject);
        this.setFieldValByName("updateTime", new Date(), metaObject);
    }
}
