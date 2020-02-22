package com.person.hero;

import com.google.protobuf.GeneratedMessage;
import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

import java.util.HashMap;
import java.util.Map;

public final class GameMsgRecognizer {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(GameMsgRecognizer.class);
    /**
     * 消息代码与消息体字典
     */
    private static final Map<Integer, GeneratedMessageV3> _msgCodeAndMsgBodyMap = new HashMap<>();
    /**
     *  消息类型与消息编号字典
     */
    private static final Map<Class<?>,Integer> _msgClazzAndMsgCodeMap = new HashMap<>();

    private GameMsgRecognizer(){
    }

    public static void init(){
        Class<?>[] innerClazzArray = GameMsgProtocol.class.getDeclaredClasses();
        for (Class<?> innerClazz:innerClazzArray){
            if(!GeneratedMessageV3.class.isAssignableFrom(innerClazz)){
                continue;
            }

            String clazzName = innerClazz.getSimpleName();
            clazzName = clazzName.toLowerCase();

            for (GameMsgProtocol.MsgCode msgCode: GameMsgProtocol.MsgCode.values()){
                String strMsgCode = msgCode.name();
                strMsgCode = strMsgCode.replaceAll("_","");
                strMsgCode = strMsgCode.toLowerCase();

                if(!strMsgCode.startsWith(clazzName)){
                    continue;
                }

                try{
                    Object returnObj = innerClazz.getDeclaredMethod("getDefaultInstance").invoke(innerClazz);
                    log.info("{} < == >{}",innerClazz.getName(),msgCode.getNumber());
                    _msgCodeAndMsgBodyMap.put(msgCode.getNumber(),(GeneratedMessageV3) returnObj);
                    _msgClazzAndMsgCodeMap.put(innerClazz,msgCode.getNumber());
                }catch (Exception e){
                    e.printStackTrace();
                }


            }

        }
    }

    /**
     * 根据消息编号获取构建者
     * @param msgCode
     * @return
     */
    public static Message.Builder getBuilderByMsgCode(int msgCode){
        if(msgCode < 0){
            return null;
        }

        GeneratedMessageV3 msg = _msgCodeAndMsgBodyMap.get(msgCode);
        if(null == msg){
            return null;
        }

        return msg.newBuilderForType();
    }

    /**
     * 根据消息类获取消息编号
     * @param msgClazz
     * @return
     */
    public static int getMsgCodeByMsgClazz(Class<?> msgClazz){

        if(null == msgClazz){
            return -1;
        }

        Integer msgCode = _msgClazzAndMsgCodeMap.get(msgClazz);
        if(null != msgCode){
            return msgCode.intValue();
        }else{
            return -1;
        }
    }

}
