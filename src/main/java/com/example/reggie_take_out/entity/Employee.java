package com.example.reggie_take_out.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.Getter;

/**
 * 员工信息
 * @TableName employee
 */
@ApiModel(value = "员工信息")
@TableName(value ="employee")
@Data
public class Employee implements Serializable {
    /**
     * 主键
     */
//    @JsonFormat(shape = JsonFormat.Shape.STRING)作用就是将JSON数据的此字段格式化为字符串类型，保证前端超过17位不会出现精度丢失问题！
//由于JavaScript中Number类型的自身原因，并不能完全表示Long型的数字，在Long长度大于17位时会出现精度丢失的问题。
//所以，不应该使用ResponseBean<Long>，应该使用ResponseBean<String>，转换成字符串类型的。
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId
    private Long id;

    /**
     * 姓名
     */
    private String name;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 性别
     */
    private String sex;

    /**
     * 身份证号
     */
    private String idNumber;

    /**
     * 状态 0:禁用，1:正常
     */
    private Integer status;

    /**
     * 创建时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    /**
     * 创建人
     */
    @TableField(fill = FieldFill.INSERT)
    private Long createUser;

    /**
     * 修改人
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Long updateUser;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}