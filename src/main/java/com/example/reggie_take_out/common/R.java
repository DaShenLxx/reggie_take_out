package com.example.reggie_take_out.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 通用返回对象,服务端返回给前端的数据都会封装成这个对象，便于用于返回给前端的数据统一
 *
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code; //编码：1成功，0和其它数字为失败

    private String msg; //错误信息

    private T data; //数据

    private Map map = new HashMap(); //动态数据

    //<T> 表示该方法是个泛型方法 R<T>就是返回值
//    一个泛型类其内部的普通方法不需要再声明泛型方法，但若是static方法，他不属于类的一部分，所以需要声明为泛型方法
    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
