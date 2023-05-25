package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.Employee;
import com.example.reggie_take_out.service.EmployeeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.sql.Struct;

/**
 * 员工信息(Employee)表控制层
 *
 * @author makejava
 * @since 2023-05-02 17:23:37
 */
@Api(tags = "员工信息(Employee)表控制层")
@Slf4j
@RestController
@RequestMapping("/employee")
public class EmployeeController {
    /**
     * 服务对象
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录方法
     */

    @PostMapping("/login")
    public R<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        return employeeService.login(request, employee);
    }

    /**
     * 退出登录
     *
     * @param request
     * @return
     */

    @PostMapping("/logout")
    public R logout(HttpServletRequest request) {
        return employeeService.logout(request);
    }

    /**
     * 新增员工
     *
     * @param employee
     * @return
     */

    @PostMapping
    public R<Employee> addEmployee(@RequestBody Employee employee) {
        log.info("新增员工：{}", employee);
        // 将密码加密后再进行保存（MD5）
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        this.employeeService.save(employee);
        return R.success(employee);
    }

    /**
     * 员工信息分页查询
     *
     * @param page
     * @param pageSize
     * @param name
     * @return
     */

    @GetMapping("/page")
    public R<Page> listEmployee(Integer page,
                                Integer pageSize,
                                String name) {
        log.info("员工信息分页查询：page = {}, pageSize = {}, name = {}", page, pageSize, name);
        return employeeService.listEmployee(page, pageSize, name);
    }

    /**
     * 根据id修改员工信息
     * @param employee
     * @return
     */

    @PutMapping
    public R setstatus(@RequestBody Employee employee) {
        System.out.println(employee.getId());
        employeeService.updateById(employee);
        return R.success(employee);
    }


    /**
     * 根据id获取员工信息
     * @param id
     * @return
     */

    @GetMapping("/{id}")
    public R getEmployeeById(@PathVariable("id") Long id) {
        Employee employee = employeeService.getById(id);
        return R.success(employee);
    }
}

