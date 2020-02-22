package com.person.hero;

import com.google.protobuf.GeneratedMessageV3;
import com.google.protobuf.Message;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 消息解码器
 */
public class GameMsgDecoder  extends ChannelInboundHandlerAdapter {

    private static final Logger log = LoggerFactory.getLogger(GameMsgDecoder.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if(!(msg instanceof BinaryWebSocketFrame)){
            return;
        }

        // WebSocket 二进制消息会通过 HttpServerCodec 解码成 BinaryWebSockerFrame 类对象
        BinaryWebSocketFrame frame = (BinaryWebSocketFrame)msg;
        ByteBuf buf = frame.content();

        buf.readShort();//读取消息的长度
        int msgCode = buf.readShort();

        // 获取消息构建者
        Message.Builder msgBuilder = GameMsgRecognizer.getBuilderByMsgCode(msgCode);
        if(null == msgBuilder){
            log.info("无法识别的消息, msgCode = {}" +msgCode);
            return;
        }

        byte[] msgbody = new byte[buf.readableBytes()]; //读取消息体
        buf.readBytes(msgbody);

        msgBuilder.clear();
        msgBuilder.mergeFrom(msgbody);

        /*GeneratedMessageV3 cmd = null;

        switch (msgCode){
            case GameMsgProtocol.MsgCode.USER_ENTRY_CMD_VALUE:
                cmd = GameMsgProtocol.UserEntryCmd.parseFrom(msgbody);
                break;
            case GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_CMD_VALUE:
                cmd = GameMsgProtocol.WhoElseIsHereCmd.parseFrom(msgbody);
                break;
            case GameMsgProtocol.MsgCode.USER_MOVE_TO_CMD_VALUE:
                cmd = GameMsgProtocol.UserMoveToCmd.parseFrom(msgbody);
                break;
        }*/

        Message newMsg = msgBuilder.build();

        if(null != newMsg){
            ctx.fireChannelRead(newMsg);
        }
    }
}
