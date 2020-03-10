package org.zxs.test.step050.entity;

import javassist.*;

import java.lang.reflect.Field;
import java.sql.ResultSet;

public final class EntityHelperFactory {
    private EntityHelperFactory(){

    }

    public static AbstractEntityHelper getEntityHelper(Class<?>  entityClazz) throws Exception{
        //全新设计，javasist

        if(null == entityClazz){
            return null;
        }

        ClassPool pool = ClassPool.getDefault();
        pool.appendSystemPath();

        // 相当于 import java.sql.ResultSet
        pool.importPackage(ResultSet.class.getName());
        //相当于 import org.zxs.test.step040.entity.UserEntity
        pool.importPackage(entityClazz.getName());


        //拿到AbstractEntityHelper类
        CtClass clazzHelper = pool.getCtClass(AbstractEntityHelper.class.getName());
        //要创建助手类名称
        String helperClazzName = entityClazz.getName() + "_Helper";

        //创建XxxEntity_Helper extends AbstractEntityHelper
        CtClass cc = pool.makeClass(helperClazzName,clazzHelper);

        //创建构造器
        //生成代码 public XxxEntity_Helper(){}
        CtConstructor constructor = new CtConstructor(new CtClass[0],cc);
        constructor.setBody("{}");

        cc.addConstructor(constructor);

        StringBuffer sb = new StringBuffer();
        sb.append("public Object create(java.sql.ResultSet rs) throws Exception {\n");
        sb.append(entityClazz.getName())
                .append(" obj = new ")
                .append(entityClazz.getName())
                .append("();\n");
        Field[] fArray = entityClazz.getFields();

        for(Field f : fArray){
            Column annoColumn = f.getAnnotation(Column.class);
            if(null == annoColumn){
                continue;
            }

            String columnName = annoColumn.name();
            if(f.getType() == Long.TYPE){
                sb.append("obj.")
                        .append(f.getName())
                        .append(" = rs.getLong(\"")
                        .append(columnName)
                        .append("\");\n");
            }else if(f.getType().equals(String.class)){
                sb.append("obj.")
                        .append(f.getName())
                        .append(" = rs.getString(\"")
                        .append(columnName)
                        .append("\");\n");
            }else {
                //不支持的类型
            }
        }

        sb.append("return null;");
        sb.append("}");

        //创建方法
        CtMethod cm = CtNewMethod.make(sb.toString(),cc);
        cc.addMethod(cm);

        Class<?> javaClazz = cc.toClass();
        Object helperImpl = javaClazz.newInstance();

        return (AbstractEntityHelper)helperImpl;
    }
}
