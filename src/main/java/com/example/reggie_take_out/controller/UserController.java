package com.example.reggie_take_out.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.reggie_take_out.common.R;
import com.example.reggie_take_out.entity.User;
import com.example.reggie_take_out.service.UserService;
import com.example.reggie_take_out.utils.RedisUtil;
import com.example.reggie_take_out.utils.SMSUtils;
import com.example.reggie_take_out.utils.ValidateCodeUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Map;

/**
 * 用户信息(User)表控制层
 *
 * @author makejava
 * @since 2023-05-11 16:35:54
 */
@Api(tags = "用户信息(User)表控制层")
@Slf4j
@RestController
@RequestMapping("user")
public class UserController {
    /**
     * 服务对象
     */
    @Resource
    private UserService userService;
    /*项目优化*/
    @Autowired
    private RedisUtil redisUtil;
    /*项目优化*/

    /**
     * 移动端发送短信
     *
     * @param user
     * @param session
     * @return
     */

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession session) {
        // 获取手机号
        String phone = user.getPhone();
        if (StringUtils.isNotEmpty(phone)) {
            // 生成验证码
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            log.info("瑞吉外卖验证码：code为：" + code);
            // 调用阿里云短信服务API完成发送短信
//            SMSUtils.sendMessage("阿里云短信测试","SMS_154950909",phone,code);
//            SMSUtils.sendMessage("瑞吉外卖", "SMS_460740551", phone, code);


            // 将生成的验证码保存
            /*项目优化*/
            // 将随机生成的验证码保存到redis中
            redisUtil.set(phone, code, 60 * 5);
            /*项目优化*/
//           session.setAttribute(phone,code);
            /*项目优化*/


            return R.success("短信发送成功");
        }
        return R.error("短信发送失败");
    }

    /**
     * 移动端用户登录
     *
     * @param map
     * @param session
     * @return
     */

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession session) {
        log.info(map.toString());
        // 获取手机号
        String phone = (String) map.get("phone");
        // 获取验证码
        String code = (String) map.get("code");


        /*项目优化*/
        // session中获取验证码
//        Object codeSession = session.getAttribute(phone);
        Object codeSession = redisUtil.get(phone);
        /*项目优化*/


        // 比对验证码
        if (codeSession != null && codeSession.equals(code)) {
            // 成功，则登录
            // 判断当前用户是否为新用户，新用户自动完成注册
            LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper();
            // 手机号查询新用户
            queryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(queryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setStatus(1);
                userService.save(user);
            }
            session.setAttribute("user", user);
            session.setAttribute("userid", user.getId());
            System.out.println(user);

            /*项目优化*/
            //如果用户登录成功，删除redis中的验证码
            redisUtil.del(phone);

            /*项目优化*/

            return R.success(user);
        }
        return R.error("登录失败,验证码有误");
    }

    /**
     * 移动端用户退出登录
     * @param request
     * @return
     */

    @PostMapping("/loginout")
    public R<String> loginout(HttpServletRequest request) {
        request.getSession().removeAttribute("user");
        request.getSession().removeAttribute("userid");
        return R.success("退出成功");
    }

}

