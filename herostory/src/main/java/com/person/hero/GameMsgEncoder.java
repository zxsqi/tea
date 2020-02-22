package com.person.hero;

import com.google.protobuf.GeneratedMessageV3;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class GameMsgEncoder extends ChannelOutboundHandlerAdapter {

    private static  final Logger logger = LoggerFactory.getLogger(GameMsgEncoder.class);

    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if(null == msg || !(msg instanceof GeneratedMessageV3)){
            super.write(ctx,msg,promise);
            return;
        }

        int msgCode = GameMsgRecognizer.getMsgCodeByMsgClazz(msg.getClass());
        if(msgCode <= -1){
            logger.info("无法识别的消息, msgClazz = {}" +msg.getClass().getName());
            return;
        }


        /*if(msg instanceof GameMsgProtocol.UserEntryResult){
            msgCode = GameMsgProtocol.MsgCode.USER_ENTRY_RESULT_VALUE;
        }else if(msg instanceof GameMsgProtocol.WhoElseIsHereResult){
            msgCode = GameMsgProtocol.MsgCode.WHO_ELSE_IS_HERE_RESULT_VALUE;
        }else if(msg instanceof GameMsgProtocol.UserMoveToResult) {
            msgCode = GameMsgProtocol.MsgCode.USER_MOVE_TO_RESULT_VALUE;
        }else if (msg instanceof GameMsgProtocol.UserQuitResult) {
                msgCode = GameMsgProtocol.MsgCode.USER_QUIT_RESULT_VALUE;
        }else{
            logger.error("不能识别的消息类型, msgClazz = " + msg.getClass().getName() );
            return;
        }*/


        byte[] msgBody = ((GeneratedMessageV3)msg).toByteArray();
        ByteBuf buf = ctx.alloc().buffer();

        buf.writeShort((short)0);//写出消息长度，写出消息0只是为了占位
        buf.writeShort((short)msgCode);//写出消息编号
        buf.writeBytes(msgBody); // 写出消息体

        BinaryWebSocketFrame frame = new BinaryWebSocketFrame(buf);
        super.write(ctx,frame,promise);
    }
}
