package com.example.reggie_take_out.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 地址管理
 * @TableName address_book
 */
@TableName(value ="address_book")
@ApiModel(value = "地址管理")
@Data
public class AddressBook implements Serializable {
    /**
     * 主键
     */
    @ApiModelProperty(value = "主键")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @TableId
    private Long id;

    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private Long userId;

    /**
     * 收货人
     */
    @ApiModelProperty(value = "收货人")
    private String consignee;

    /**
     * 性别 0 女 1 男
     */
    @ApiModelProperty(value = "性别 0 女 1 男")
    private Integer sex;

    /**
     * 手机号
     */
    @ApiModelProperty(value = "手机号")
    private String phone;

    /**
     * 省级区划编号
     */

    private String provinceCode;

    /**
     * 省级名称
     */

    private String provinceName;

    /**
     * 市级区划编号
     */

    private String cityCode;

    /**
     * 市级名称
     */

    private String cityName;

    /**
     * 区级区划编号
     */

    private String districtCode;

    /**
     * 区级名称
     */

    private String districtName;

    /**
     * 详细地址
     */

    private String detail;

    /**
     * 标签
     */

    private String label;

    /**
     * 默认 0 否 1是
     */

    private Integer isDefault;

    /**
     * 创建时间
     */

//    @JsonFormat设置全局配置(结果时间和数据库时间不一致问题)
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

    /**
     * 是否删除
     */

    private Integer isDeleted;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}