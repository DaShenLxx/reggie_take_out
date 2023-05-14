package com.example.reggie_take_out.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.reggie_take_out.entity.AddressBook;
import com.example.reggie_take_out.service.AddressBookService;
import com.example.reggie_take_out.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 80954
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-05-11 20:29:36
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




