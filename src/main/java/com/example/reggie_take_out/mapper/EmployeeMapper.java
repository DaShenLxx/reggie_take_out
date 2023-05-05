package com.example.reggie_take_out.mapper;

import com.example.reggie_take_out.entity.Employee;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 80954
* @description 针对表【employee(员工信息)】的数据库操作Mapper
* @createDate 2023-05-02 17:21:35
* @Entity com.example.reggie_take_out.entity.Employee
*/
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {

}




