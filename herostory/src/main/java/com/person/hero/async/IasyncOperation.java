package com.person.hero.async;

public interface IasyncOperation {
    /**
     * 异步操作
     */
    void doAsync();

    /**
     * 执行完成逻辑
     */
    default void dofinish(){
    }

    /**
     * 获取绑定ID
     * @return
     */
    default int bindId(){
        return 0;
    }
}
