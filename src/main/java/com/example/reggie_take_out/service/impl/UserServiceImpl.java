package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.User;
import com.example.reggie_take_out.service.UserService;
import com.example.reggie_take_out.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 80954
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-05-11 16:35:28
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




