package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.service.EmployeeService;
import com.example.reggie_take_out.mapper.EmployeeMapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 80954
 * @description 针对表【employee(员工信息)】的数据库操作Service实现
 * @createDate 2023-05-02 17:21:35
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
        implements EmployeeService {
    @Autowired
    private EmployeeMapper employeeMapper;
    /**
     * 登录方法
     * @param request
     * @param employee
     * @return
     */
    public R<Employee> login(HttpServletRequest request, Employee employee) {
        //        将密码加密后再进行查询（MD5）
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
//        根据用户名和密码查询员工信息
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Employee::getUsername, employee.getUsername());
        Employee loginEmployee = this.getOne(wrapper);
//        判断员工是否存在
        if (loginEmployee == null) {
            return R.error("用户名不存在");
        }
//        判断密码是否正确
        if (!loginEmployee.getPassword().equals(password)) {
            return R.error("密码错误");
        }
//        查看员工是否被禁用
        if (loginEmployee.getStatus() == 0) {
            return R.error("该用户已被禁用");
        }
//        将员工信息存入session
        request.getSession().setAttribute("loginEmployee", loginEmployee);
        request.getSession().setAttribute("loginEmployeeId", loginEmployee.getId());
        return R.success(loginEmployee);
    }

    /**
     * 退出登录
     * @param request
     * @return
     */
    @Override
    public R logout(HttpServletRequest request) {

        request.getSession().removeAttribute("loginEmployee");
        request.getSession().removeAttribute("loginEmployeeId");
        return R.success("退出成功");
    }

    /**
     * 员工分页查询
     * @param page
     * @param pageSize
     * @param name
     * @return
     */
    @Override
    public R<Page> listEmployee(Integer page, Integer pageSize, String name){
        //        构造分页构造器
        Page<Employee> employeePage = new Page<>(page, pageSize);
//        构造条件构造器
        LambdaQueryWrapper<Employee> wrapper = new LambdaQueryWrapper<>();
//        如果name不为空，就构造like条件
        wrapper.like(StringUtils.isNotEmpty(name),Employee::getName, name);
        wrapper.orderByDesc(Employee::getUpdateTime);
//        查询执行
        Page<Employee> result = this.page(employeePage, wrapper);
//        可以不用再封装给result了，因为执行page()方法已经自动给employeePage赋值了，Page已经封装好了
        List<Employee> records = employeePage.getRecords();

        return R.success(employeePage);
    }
}




