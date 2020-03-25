package com.person.hero.cmdHandler;

import com.person.hero.Broadcaster;
import com.person.hero.login.LoginService;
import com.person.hero.login.db.UserEntity;
import com.person.hero.model.User;
import com.person.hero.model.UserManager;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tinygame.herostory.msg.GameMsgProtocol;

/**
 * 用户登录指令处理器
 */
public class UserLoginCmdHandler implements ICmdHandler<GameMsgProtocol.UserLoginCmd>{

    private static final Logger log = LoggerFactory.getLogger(UserLoginCmdHandler.class);

    @Override
    public void handle(ChannelHandlerContext ctx, GameMsgProtocol.UserLoginCmd cmd) {
        log.info("username = {}, password = {}",cmd.getUserName(),cmd.getPassword());
        UserEntity user = null;
        try {
            user = LoginService.getService().userLogin(cmd.getUserName(),cmd.getPassword());

        }catch (Exception e){
            log.info(e.getMessage(),e);
        }

        if(null == user){
            log.info("用户登录失败 username={}",cmd.getUserName());
            return;
        }


        int userId = user.userId;
        String heroAvatar = user.heroAvatar;

        User newUser = new User();
        newUser.userId = userId;
        newUser.userName = user.userName;
        newUser.heroAvatar = heroAvatar;
        newUser.currHp = 100;
        UserManager.addUser(newUser);

        // 将用户 Id 附着到 Channel
        ctx.channel().attr(AttributeKey.valueOf("userId")).set(userId);

        // 构建结果并发送
        GameMsgProtocol.UserLoginResult.Builder resultBuilder = GameMsgProtocol.UserLoginResult.newBuilder();
        resultBuilder.setUserId(newUser.userId);
        resultBuilder.setHeroAvatar(newUser.heroAvatar);
        resultBuilder.setUserName(newUser.userName);

        //构建结果并发送
        GameMsgProtocol.UserLoginResult newResult = resultBuilder.build();
        ctx.writeAndFlush(newResult);

    }
}
