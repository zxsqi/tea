package com.person.hero.cmdHandler;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.channel.ChannelHandlerContext;

public interface ICmdHandler<TCmd extends GeneratedMessageV3> {

    /**
     * 处理
     * @param ctx
     * @param cmd
     */
    void handle(ChannelHandlerContext ctx, TCmd cmd);
}
