package org.zxs.test.step040.entity;

import javassist.*;

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

        /*// 抽象的助手类
        CtClass abstractEntityHelperClazz = pool.getCtClass(AbstractEntityHelper.class.getName());*/

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
