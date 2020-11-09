package com.robot.ai.jdbc;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.sql.ResultSet;

/**
 * 返回一个JavaBean
 * @Author: fangju
 * @Date: 2019/6/15
 */
public class BeanHandler<T> implements IResultSetHandler<T> {
    private Class<T> clazz;

    public BeanHandler(Class<T> clazz){
        this.clazz = clazz;
    }

    @Override
    public T handle(ResultSet rs) throws Exception{
        //结果集默认指向为第一个数据的前一个
        if (rs.next()){
            //根据传入的字节码创建传入的指定对象
            T obj = clazz.newInstance();
            //获取指定字节码信息
            BeanInfo beanInfo = Introspector.getBeanInfo(clazz,Object.class);
            //获取所有属性描述器
            PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor pd:pds){
                //获取结果集中对应字段名的值
                Object o = rs.getObject(pd.getName());
                //执行当前方法并传入参数
                pd.getWriteMethod().invoke(obj,o);
            }
            return obj;
        }
        return null;
    }
}

