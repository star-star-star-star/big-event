package com.example.big_event.util;

/**
 * @apiNote ThreadLocal可以为每一个用户单独开辟存储空间进行数据存取，并且是线程安全的
 * @since 1.0.0
 */
public class ThreadLocal {

    private static final java.lang.ThreadLocal<Object> THREAD_LOCAL = new java.lang.ThreadLocal<>();

    /**
     * @apiNote 存储数据
     * @param object 需要存储的数据
     */
    public static void setObject(Object object){
        THREAD_LOCAL.set(object);
    }

    /**
     * @apiNote 读取数据
     * @return 读取的数据
     */
    public static Object getObject(){
        return THREAD_LOCAL.get();
    }

    /**
     * @apiNote 清空容器
     */
    public static void clear(){
        THREAD_LOCAL.remove();
    }
}