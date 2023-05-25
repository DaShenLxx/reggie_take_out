package com.example.reggie_take_out.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.example.reggie_take_out.common.BaseContext;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.AddressBook;
import com.example.reggie_take_out.service.AddressBookService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 地址簿管理
 */
@Api(tags = "地址簿管理")
@Slf4j
@RestController
@RequestMapping("/addressBook")
public class AddressBookController {

    @Autowired
    private AddressBookService addressBookService;
    @Autowired
    HttpSession httpSession;

    /**
     * 新增
     */
    @ApiOperation(value = "新增地址", notes = "新增地址")
    @ApiImplicitParam(name = "addressBook", value = "地址簿实体类", required = true, dataType = "AddressBook")
    @PostMapping
    public R<AddressBook> save(@RequestBody AddressBook addressBook) {
//        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setUserId((Long) httpSession.getAttribute("userid"));
        log.info("addressBook:{}", addressBook);
        addressBookService.save(addressBook);
        return R.success(addressBook);
    }

    /**
     * 修改地址
     *
     * @param addressBook
     * @return
     */
    @ApiOperation(value = "修改地址", notes = "修改地址")
    @ApiImplicitParam(name = "addressBook", value = "地址簿实体类", required = true, dataType = "AddressBook")
    @PutMapping
    public R<String> update(@RequestBody AddressBook addressBook) {
//        addressBook.setUserId(BaseContext.getCurrentId());
        if (addressBook == null) {
            return R.error("请求异常");
        }
        addressBookService.updateById(addressBook);
        return R.success("修改成功！");
    }

    /**
     * 删除地址
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除地址", notes = "删除地址")
    @ApiImplicitParam(name = "ids", value = "地址簿id", required = true, dataType = "Long")
    @DeleteMapping
    public R<String> delete(@RequestParam("ids") Long id) {
        if (id == null) {
            return R.error("请求异常");
        }
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AddressBook::getId, id).eq(AddressBook::getUserId, BaseContext.getCurrentId());
        addressBookService.remove(queryWrapper);
        return R.success("删除地址成功");
    }


    /**
     * 设置默认地址
     */
    @ApiOperation(value = "设置默认地址", notes = "设置默认地址")
    @ApiImplicitParam(name = "addressBook", value = "地址簿实体类", required = true, dataType = "AddressBook")
    @PutMapping("default")
    public R<AddressBook> setDefault(@RequestBody AddressBook addressBook) {
        log.info("addressBook:{}", addressBook);
        LambdaUpdateWrapper<AddressBook> wrapper = new LambdaUpdateWrapper<>();
//        wrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        wrapper.eq(AddressBook::getUserId, this.httpSession.getAttribute("userid"));
        wrapper.set(AddressBook::getIsDefault, 0);
        //SQL:update address_book set is_default = 0 where user_id = ?
        addressBookService.update(wrapper);

        addressBook.setIsDefault(1);
        //SQL:update address_book set is_default = 1 where id = ?
        addressBookService.updateById(addressBook);
        return R.success(addressBook);
    }

    /**
     * 根据id查询地址
     */
    @ApiOperation(value = "根据id查询地址", notes = "根据id查询地址")
    @ApiImplicitParam(name = "id", value = "地址簿id", required = true, dataType = "Long")
    @GetMapping("/{id}")
    public R get(@PathVariable Long id) {
        AddressBook addressBook = addressBookService.getById(id);
        if (addressBook != null) {
            return R.success(addressBook);
        } else {
            return R.error("没有找到该对象");
        }
    }

    /**
     * 查询默认地址
     */
    @ApiOperation(value = "查询默认地址", notes = "查询默认地址")
    @GetMapping("default")
    public R<AddressBook> getDefault() {
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
//        queryWrapper.eq(AddressBook::getUserId, BaseContext.getCurrentId());
        queryWrapper.eq(AddressBook::getUserId, this.httpSession.getAttribute("userid"));
        queryWrapper.eq(AddressBook::getIsDefault, 1);

        //SQL:select * from address_book where user_id = ? and is_default = 1
        AddressBook addressBook = addressBookService.getOne(queryWrapper);

        if (null == addressBook) {
            return R.error("没有找到该对象");
        } else {
            return R.success(addressBook);
        }
    }

    /**
     * 查询指定用户的全部地址
     */
    @ApiOperation(value = "查询指定用户的全部地址", notes = "查询指定用户的全部地址")
    @GetMapping("/list")
    public R<List<AddressBook>> list( AddressBook addressBook) {
//        addressBook.setUserId(BaseContext.getCurrentId());
        addressBook.setUserId((Long) this.httpSession.getAttribute("userid"));
        log.info("addressBook:{}", addressBook);

        //条件构造器
        LambdaQueryWrapper<AddressBook> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(null != addressBook.getUserId(), AddressBook::getUserId, addressBook.getUserId());
        queryWrapper.orderByDesc(AddressBook::getUpdateTime);

        //SQL:select * from address_book where user_id = ? order by update_time desc
        return R.success(addressBookService.list(queryWrapper));
    }
}
