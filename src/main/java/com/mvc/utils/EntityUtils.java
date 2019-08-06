package com.mvc.utils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

public class EntityUtils {

    public static <T> void setNull(T t, String... fieldName){
        List<String> list = Arrays.asList(fieldName);
        setNull(t, f->list.contains(f.getName()));
    }

    public static <T> void setOtherNull(T t, String... fieldName){
        List<String> list = Arrays.asList(fieldName);
        setNull(t, f->! list.contains(f.getName()));
    }

    /**
     * 根据断言将对象中的字段设为null
     * @param t 对象
     * @param predicate 断言
     */
    public static <T> void setNull(T t, Predicate<Field> predicate){
        // 获取源数据的类
        Class<?> clazz = t.getClass();
        // 获取字段集合
        List<Field> fieldList = new ArrayList<>() ;
        while (clazz != null) {//当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(clazz.getDeclaredFields()));
            clazz = clazz.getSuperclass(); //得到父类,然后赋给自己
        }

        for (Field f : fieldList){
            if(predicate.test(f)){
                try {
                    // 给目标数据字段赋值
                    f.setAccessible(true);
                    f.set(t,null);
                } catch (IllegalAccessException e) {
                }finally {
                    // 访问权限还原
                    f.setAccessible(false);
                }
            }
        }
    }

}
