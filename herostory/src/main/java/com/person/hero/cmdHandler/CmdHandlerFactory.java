package com.person.hero.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

public final class CmdHandlerFactory {
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
        _handlerMap.put(GameMsgProtocol.UserMoveToCmd.class,new UserMoveToCmdHandler());
        _handlerMap.put(GameMsgProtocol.UserEntryCmd.class,new UserEntryCmdHandler());
        _handlerMap.put(GameMsgProtocol.WhoElseIsHereCmd.class,new WhoElseIsHereCmdHandler());
    }

    public static ICmdHandler<? extends GeneratedMessageV3> create(Class<?> msgClazz){
        if(null == msgClazz){
            return null;
        }

        return _handlerMap.get(msgClazz);
    }
}
