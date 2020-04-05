package com.person.hero.async;

import com.person.hero.MainThreadProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 异步操作处理器
 */
public final class AsyncOperationProcessor {

    /**
     * 日志对象
     */
    private static final Logger log = LoggerFactory.getLogger(AsyncOperationProcessor.class);
    /**
     * 单例对象
     */
    private static final AsyncOperationProcessor _instance = new AsyncOperationProcessor();
    /**
     * 创建单线程
     */
    private final ExecutorService[] _esArray = new ExecutorService[8];

    /**
     * 私有化默认构造器
     */
    private AsyncOperationProcessor(){
        for(int i=0;i < _esArray.length; i++){
            //线程名称
            final String finalName="AsyncOperationProcessor" + i;
            //创建单线程
            _esArray[i] = Executors.newSingleThreadExecutor((newRunnable)->{
                Thread newThread = new Thread(newRunnable);
                newThread.setName(finalName);
                return newThread;
            });
        }

    }

    /**
     * 返回实例对象
     * @return asyncOp 异步操作
     */
    public static AsyncOperationProcessor getInstance(){
        return _instance;
    }

    public void process(IasyncOperation asyncOp) {
        if (null == asyncOp) {
            return;
        }

        //根据bindId获取线程索引
        int bindId = Math.abs(asyncOp.bindId());
        int esIndex = bindId % _esArray.length;
        _esArray[esIndex].submit(() -> {
            asyncOp.doAsync();

            try{
                MainThreadProcessor.getInstance().process(asyncOp::dofinish);
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }

        });
    }

}
