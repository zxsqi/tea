package com.person.hero.cmdHandler;

import com.person.hero.Broadcaster;
import com.person.hero.model.User;
import com.person.hero.model.UserManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

public class UserAttkCmdHandler implements ICmdHandler<GameMsgProtocol.UserAttkCmd> {

    private static final Logger log = LoggerFactory.getLogger(UserAttkCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserAttkCmd cmd) {
        if(null == ctx || null == cmd ){
            return;
        }

        //获取攻击者ID
        Integer attkUserId = (Integer) ctx.channel().attr(AttributeKey.valueOf("userId")).get();
        if(null == attkUserId){
            return;
        }

        //获取被攻击者ID
        int targetUserId = cmd.getTargetUserId();

        GameMsgProtocol.UserAttkResult.Builder resultBuilder = GameMsgProtocol.UserAttkResult.newBuilder();
        resultBuilder.setAttkUserId(attkUserId);
        resultBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserAttkResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);

        //获取被攻击者
        User targetUser = UserManager.getUserById(targetUserId);
        if(null == targetUser){
            return;
        }

        log.info("当前线程={}",Thread.currentThread().getName());

        int subtractUp = 10;
        targetUser.currHp = targetUser.currHp - subtractUp;

        //广播减血消息
        broadCastSubtractHp(targetUserId,subtractUp);

        if(targetUser.currHp <= 0){
            broadCastDie(targetUserId);
        }
    }

    /**
     * 广播减血量消息
     * @param targetUserId 被攻击者ID
     * @param subtractHp  减血量
     */
    private static void broadCastSubtractHp(int targetUserId,int subtractHp){
        if(targetUserId <= 0 || subtractHp <=0){
            return;
        }

        //减血消息
        GameMsgProtocol.UserSubtractHpResult.Builder resultBuilder = GameMsgProtocol.UserSubtractHpResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);
        resultBuilder.setSubtractHp(subtractHp);

        GameMsgProtocol.UserSubtractHpResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }

    /**
     * 广播死亡消息
     * @param targetUserId 被攻击者ID
     */
    private static void broadCastDie(int targetUserId){
        if(targetUserId <= 0){
            return;
        }

        GameMsgProtocol.UserDieResult.Builder resultBuilder = GameMsgProtocol.UserDieResult.newBuilder();
        resultBuilder.setTargetUserId(targetUserId);

        GameMsgProtocol.UserDieResult newResult = resultBuilder.build();
        Broadcaster.broadcast(newResult);
    }
}
