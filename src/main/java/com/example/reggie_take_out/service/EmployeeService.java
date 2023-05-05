package com.example.reggie_take_out.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Employee;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
* @author 80954
* @description 针对表【employee(员工信息)】的数据库操作Service
* @createDate 2023-05-02 17:21:35
*/
public interface EmployeeService extends IService<Employee> {
    /**
     * 登录方法
     * @param request
     * @param employee
     * @return
     */
    R<Employee> login(HttpServletRequest request, Employee employee);

    /**
     * 退出登录
     * @param request
     * @return
     */
    R logout(HttpServletRequest request);

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    R<Page> listEmployee(Integer page, Integer pageSize, String name);
}


