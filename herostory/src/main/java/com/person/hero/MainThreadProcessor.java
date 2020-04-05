package com.person.hero;

import com.google.protobuf.GeneratedMessageV3;
import com.person.hero.cmdHandler.CmdHandlerFactory;
import com.person.hero.cmdHandler.ICmdHandler;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

public final class MainThreadProcessor {

    private static final Logger log = LoggerFactory.getLogger(MainThreadProcessor.class);

    //单例对象
    private static  final MainThreadProcessor _instance = new MainThreadProcessor();

    //创建单线程
    private final ExecutorService _es = Executors.newSingleThreadExecutor((r)-> {
        Thread newThread = new Thread(r);
        newThread.setName("MainThreadProcessor");
        return newThread;
    });

    /**
     * 私有化构造器
     */
    private MainThreadProcessor(){
    }

    //返回单例对象
    public static MainThreadProcessor getInstance(){
        return _instance;
    }

    public void process(ChannelHandlerContext ctx, GeneratedMessageV3 msg){
        if(null == ctx || null == msg){
            return;
        }

        this._es.submit(()->{

            //获取消息类
            Class<?> msgClazz = msg.getClass();
            ICmdHandler<? extends GeneratedMessageV3> cmdHandler = CmdHandlerFactory.create(msgClazz);

            if(null == cmdHandler){
                log.error("未找到对应的指令 msgClazz " +msgClazz.getName());
            }
            try{
                cmdHandler.handle(ctx,cast(msg));
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }
        });

    }

    /**
     * 异步处理消息
     * @param r
     */
    public void process(Runnable r){
        if(null != r){
            _es.submit(r);
        }
    }

    private static <TCmd extends GeneratedMessageV3> TCmd cast(Object msg){

        if(null == msg){
            return null;
        }else {
            return (TCmd) msg;
        }
    }
}
