package com.person.hero.cmdHandler;

import com.person.hero.model.MoveState;
import com.person.hero.model.User;
import com.person.hero.model.UserManager;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 谁在现场的处理指令
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd> {

    private static final Logger log = LoggerFactory.getLogger(WhoElseIsHereCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg) {

        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        log.info("--------WhoElseIsHereCmdHandler handle-------");
        for(User user : UserManager.listUser()){
            if(null == user){
                continue;
            }

            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setHeroAvatar(user.heroAvatar);
            userInfoBuilder.setUserId(user.userId);

            //构建移动状态
            MoveState moveState = user.moveState;
            GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.Builder mvStateBuilder =
                    GameMsgProtocol.WhoElseIsHereResult.UserInfo.MoveState.newBuilder();
            mvStateBuilder.setToPosX(moveState.toPosX);
            mvStateBuilder.setToPosY(moveState.toPosY);
            mvStateBuilder.setFromPosX(moveState.fromPosX);
            mvStateBuilder.setFromPosY(moveState.fromPosY);
            mvStateBuilder.setStartTime(moveState.startTime);
            //将移动状态设置给用户信息
            userInfoBuilder.setMoveState(mvStateBuilder);


            resultBuilder.addUserInfo(userInfoBuilder);
        }

        GameMsgProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }
}
