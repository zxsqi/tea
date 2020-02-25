package com.person.hero.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import com.person.hero.GameMsgDecoder;
import com.person.hero.util.PackageUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public final class CmdHandlerFactory {

    private static final Logger log = LoggerFactory.getLogger(CmdHandlerFactory.class);

    /**
     * 处理器字典
     */
    public static Map<Class<?>,ICmdHandler<? extends GeneratedMessageV3>>  _handlerMap = new HashMap();

    private CmdHandlerFactory(){

    }

    /**
     * 初始化
     */
    public static void init(){
        log.info("====完成cmd和handler的关联=====");
        //获取包名称
        final String  packageName = CmdHandlerFactory.class.getPackage().getName();
        //获取当前包中 ICmdHandler 接口的所有子类
        Set<Class<?>> clazzSet = PackageUtil.listSubClazz(packageName,true,ICmdHandler.class);
        for(Class<?> clazz : clazzSet){
            if((clazz.getModifiers() & Modifier.ABSTRACT) != 0){
                //如果抽象类
                continue;
            }

            log.info("---clazz---" + clazz.getSimpleName());

            //获取方法数组
            Method[] methodArray = clazz.getDeclaredMethods();
            Class<?> msgType = null;

            for(Method currMethod : methodArray){
                if(!currMethod.getName().equals("handle")){
                    //如果不是handle方法
                    continue;
                }

                //获取函数参数类型
                Class<?>[] paramTypeArray = currMethod.getParameterTypes();
                if(paramTypeArray.length<2
                        || paramTypeArray[1] == GeneratedMessageV3.class
                        || !GeneratedMessageV3.class.isAssignableFrom(paramTypeArray[1])
                ){
                    continue;
                }

                msgType = paramTypeArray[1];
                break;
            }

            if(null == msgType){
                continue;
            }

            try {
                ICmdHandler<?> newHandler = (ICmdHandler<?>) clazz.newInstance();
                log.info("关联 {} <==> {}",msgType.getName(),clazz.getName());
                _handlerMap.put(msgType,newHandler);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        /*_handlerMap.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHandler());
        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHandler());
        _handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());*/
    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz){
        if(null == msgClazz){
            return null;
        }

        return _handlerMap.get(msgClazz);
    }
}

