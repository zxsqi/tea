package com.person.hero;

import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public final class Broadcaster {

    private static final ChannelGroup _channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    private Broadcaster(){
    }

    /**
     * 添加信道
     * @param channel
     */
    public static void addChannel(Channel channel){
        _channelGroup.add(channel);
    }

    /**
     * 删除信道
     * @param channel
     */
    public static void removeChannel(Channel channel){
        _channelGroup.remove(channel);
    }

    /**
     * 关播消息
     * @param msg
     */
    public static void broadcast(Object msg){
        if(null == msg){
            return;
        }

        _channelGroup.writeAndFlush(msg);
    }

}
