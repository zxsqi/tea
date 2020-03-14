package com.person.hero.cmdHandler;

import com.person.hero.Broadcaster;
import com.person.hero.model.MoveState;
import com.person.hero.model.User;
import com.person.hero.model.UserManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class UserMoveToCmdHandler implements ICmdHandler<GameMsgProtocol.UserMoveToCmd> {

    private static final Logger log = LoggerFactory.getLogger(UserMoveToCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserMoveToCmd msg) {
        Integer userId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();

        if(null == userId){
            return;
        }

        //获取移动用户
        User newUser = UserManager.getUserById(userId);
        if(null == newUser){
            log.info("没有找到用户，userId" + userId);
            return;
        }

        //获取移动状态
        MoveState moveState = newUser.getMoveState();
        //设置位置和开始时间
        moveState.fromPosX = msg.getMoveFromPosX();
        moveState.fromPosY = msg.getMoveFromPosY();
        moveState.toPosX = msg.getMoveToPosX();
        moveState.toPosY = msg.getMoveToPosY();
        moveState.startTime = System.currentTimeMillis();


        GameMsgProtocol.UserMoveToCmd cmd = msg;

        GameMsgProtocol.UserMoveToResult.Builder resultBuilder = GameMsgProtocol.UserMoveToResult.newBuilder();
        resultBuilder.setMoveUserId(userId);
        resultBuilder.setMoveToPosY(moveState.toPosY);
        resultBuilder.setMoveToPosX(moveState.toPosX);
        resultBuilder.setMoveFromPosX(moveState.fromPosX);
        resultBuilder.setMoveFromPosY(moveState.fromPosY);
        resultBuilder.setMoveStartTime(moveState.startTime);

        GameMsgProtocol.UserMoveToResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
