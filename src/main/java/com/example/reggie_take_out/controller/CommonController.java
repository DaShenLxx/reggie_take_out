package com.example.reggie_take_out.controller;


import com.example.reggie_take_out.common.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

/**
 * 公共控制器
 * 文件上传和下载
 */
@Api(tags = "公共接口")
@Slf4j
@RestController
@RequestMapping("/common")
public class CommonController {
    @Value("${reggie.path}")
    private String path;


    /**
     * 上传图片
     *
     * @param file
     * @return
     */

    @PostMapping("/upload")
//    此处形参名必须和前端传过来的参数名一致name="file"
    public R upload(MultipartFile file) {
//        当前file是一个临时文件，需要将其保存到服务器的某个位置，然后返回一个url给前端，否则前端无法访问到这个文件，且临时文件会被删除
        log.info("文件上传");
        /*log.info("文件名：{}", file.getOriginalFilename());
        log.info("文件大小：{}", file.getSize());
        log.info("文件类型：{}", file.getContentType());
        log.info("文件内容：{}", file);*/

//        原始文件名
        String originalFilename = file.getOriginalFilename();
//        截取出文件后缀名
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
//      使用UUID生成唯一文件名，防止文件覆盖(文件名重复)
        String fileName = UUID.randomUUID().toString() + suffix;
//        创建一个目录
        File dir = new File(this.path);
        if (!dir.exists()) {
            dir.mkdir();
        }
        System.out.println(this.path + fileName);
        try {
            //        将临时文件转存到指定位置
            file.transferTo(new File(path + fileName));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return R.success(fileName);
    }

    /**
     * 文件下载
     *
     * @param name     文件名
     * @param response 文件数据
     */

    @GetMapping("/download")
    public void downLoad(String name, HttpServletResponse response) {
        try {
            // 读取文件内容
            FileInputStream fileInputStream = new FileInputStream(new File(this.path + name));
            // 输出流，用于写入浏览器展示图片
            ServletOutputStream outputStream = response.getOutputStream();

            int len = 0;
            byte[] bytes = new byte[1024];
//            读取文件内容并写入到byte数组中
            while ((len = fileInputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, len);
                outputStream.flush();
            }
            // 关闭资源
            outputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
