package com.example.reggie_take_out;

import com.alibaba.fastjson.JSON;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import java.util.HashMap;
import java.util.Map;
@SpringBootTest
class ReggieTakeOutApplicationTests {


            void sendSms() throws ClientException {

                // 指定地域名称 短信API的就是 cn-hangzhou 不能改变  后边填写您的  accessKey 和 accessKey Secret
                DefaultProfile profile = DefaultProfile.getProfile("cn-hangzhou", "LTAI5t7472si7MpeyCmHkP47", "2YfQOoYRSZXjnF63LhacyHvNbRKWnz");
                IAcsClient client = new DefaultAcsClient(profile);

                // 创建通用的请求对象
                CommonRequest request = new CommonRequest();
                // 指定请求方式
                request.setMethod(MethodType.POST);
                // 短信api的请求地址  固定
                request.setDomain("dysmsapi.aliyuncs.com");
                // 签名算法版本  固定
                request.setVersion("2017-05-25");
                //请求 API 的名称。
                request.setAction("SendSms");
                // 上边已经指定过了 这里不用再指定地域名称
//        request.putQueryParameter("RegionId", "cn-hangzhou");
                // 您的申请签名
                request.putQueryParameter("SignName", "阿里云短信测试");
                // 您申请的模板 code
                request.putQueryParameter("TemplateCode", "SMS_154950909");
                // 要给哪个手机号发送短信  指定手机号
                request.putQueryParameter("PhoneNumbers", "15036745579");

                // 创建参数集合
                Map<String, Object> params = new HashMap<>();
                // 生成短信的验证码
                String code = String.valueOf(Math.random()).substring(3, 9);
                // 这里的key就是短信模板中的 ${xxxx}
                params.put("code", code);

                // 放入参数  需要把 map转换为json格式  使用fastJson进行转换
                request.putQueryParameter("TemplateParam", JSON.toJSONString(params));


                    // 发送请求 获得响应体
                    CommonResponse response = client.getCommonResponse(request);
                    // 打印响应体数据
                    System.out.println(response.getData());
                    // 打印 请求状态 是否成功
                    System.out.println(response.getHttpResponse().isSuccess());

            }
        }






