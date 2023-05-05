package com.example.reggie_take_out.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

//全局异常处理

//@RestControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * 处理SQLIntegrityConstraintViolationException异常(用户账号异常)
     * @param e
     * @return
     */
 /*   @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> handleException(SQLIntegrityConstraintViolationException e) {
        log.error(e.getMessage());
        return R.error("服务器异常");
    }*/

    /**
     * 处理Exception异常
     * @param e
     * @return
     */
    @ExceptionHandler(Exception.class)
    public R<String> globalhandleException(Exception e) {
        System.out.println("==============================");
        System.out.println(e);
        if (e instanceof DuplicateKeyException){
            log.error(e.getMessage());
            return R.error("员工已存在");
        }
        else {
            log.error(e.getMessage());
            return R.error("服务器异常");
        }
    }

    @ExceptionHandler(CustomException.class)
    public R<String> globalhandleException(CustomException e) {
        System.out.println("==============================");
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }

}

