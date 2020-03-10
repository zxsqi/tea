package org.zxs.test.step030.entity;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public class XxxEntity_Helper {
    /**
     * 将数据转换成实体
     * @param rs
     * @return
     */
    public <TEnttity> TEnttity create(Class<TEnttity> entityClazz,ResultSet rs) throws Exception {

        if(null == rs){
            return null;
        }

        //创建新实体
        Object newEntity = entityClazz.newInstance();
//        UserEntity ue = new UserEntity();
        //获取类的字段数组
        Field[] fArray = entityClazz.getClass().getFields();

        for (Field f : fArray){
            //获取字段上的注解名
            Column annoColumn = f.getAnnotation(Column.class);

            if(null == annoColumn){
                continue;
            }

            //获取列名称
            String colName = annoColumn.name();
            //从数据库中获中列值
            Object colValue = rs.getObject(colName);
            if(null == colValue){
                continue;
            }
            f.set(newEntity,colValue);
        }

        return (TEnttity)newEntity;
    }
}
