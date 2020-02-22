package com.person.hero;

import com.person.hero.cmdHandler.CmdHandlerFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http2.Http2FrameCodec;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ServiceMain {

    private static final Logger log = LoggerFactory.getLogger(ServiceMain.class);

    public static void main(String args[]){
        CmdHandlerFactory.init();
        GameMsgRecognizer.init();
        EventLoopGroup bossGroup = new NioEventLoopGroup(); //主线程
        EventLoopGroup workerGroup = new NioEventLoopGroup(); //从线程

        ServerBootstrap b = new ServerBootstrap();
        b.group(bossGroup,workerGroup);
        b.channel(NioServerSocketChannel.class);//服务器信道的处理方式
        b.childHandler(new ChannelInitializer<SocketChannel>() { //客户端信道处理方式

            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline().addLast(
                        new HttpServerCodec(), //服务器编解码
                        new HttpObjectAggregator(65535), //内容长度限制
                        new WebSocketServerProtocolHandler("/websocket"), //websocket 协议处理器，处理握手ping
                        new GameMsgDecoder(), //自定义消息解码器
                        new GameMsgEncoder(), //自定义消息编码器
                        new GameMsgHandler()  // 自定义的消息处理器
                );
            }
        });

        try {
            ChannelFuture f = b.bind(12345).sync();
            if(f.isSuccess()){
                log.info("服务器启动成功");
            }

            // 等待服务器信道关闭,
            // 也就是不要立即退出应用程序, 让应用程序可以一直提供服务
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}
