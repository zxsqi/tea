package com.person.hero.cmdHandler;

import com.person.hero.model.User;
import com.person.hero.model.UserManager;
import io.netty.channel.ChannelHandlerContext;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 谁在现场的处理指令
 */
public class WhoElseIsHereCmdHandler implements ICmdHandler<GameMsgProtocol.WhoElseIsHereCmd> {

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.WhoElseIsHereCmd msg) {

        GameMsgProtocol.WhoElseIsHereResult.Builder resultBuilder = GameMsgProtocol.WhoElseIsHereResult.newBuilder();

        for(User user : UserManager.listUser()){
            if(null == user){
                continue;
            }

            GameMsgProtocol.WhoElseIsHereResult.UserInfo.Builder userInfoBuilder = GameMsgProtocol.WhoElseIsHereResult.UserInfo.newBuilder();
            userInfoBuilder.setHeroAvatar(user.heroAvatar);
            userInfoBuilder.setUserId(user.userId);
            resultBuilder.addUserInfo(userInfoBuilder);
        }

        GameMsgProtocol.WhoElseIsHereResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);
    }
}
