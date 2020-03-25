package com.person.hero.login;

import com.person.hero.MySqlSessionFactory;
import com.person.hero.login.db.IUserDao;
import com.person.hero.login.db.UserEntity;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginService {
    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    //单例对象
    private static final LoginService _instance = new LoginService();

    private LoginService(){
    }

    /**
     * 获取单例对象
     * @return
     */
    public static LoginService getService(){
        return _instance;
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    public UserEntity userLogin(String username , String password){

        if(null == username || null == password){
            return null;
        }

        try (SqlSession session = MySqlSessionFactory.openSession()){
            IUserDao dao = session.getMapper(IUserDao.class);

            //查看当前线程
            log.info("当前线程 = {}" , Thread.currentThread().getName());
            UserEntity user = dao.getUserByName(username);

            //判断用户密码
            if(null != user){
                if(!password.equals(user.password)){
                    log.info("用户密码错误，userId={},userName={}",user.userId,username);
                    throw new RuntimeException("用户密码错误");
                }
            }else {
                //如果实体为空则新建用户
                user = new UserEntity();
                user.userName = username;
                user.password = password;
                user.heroAvatar = "Hero_Shaman";

                //将用户实体添加到数据库
                dao.insertInto(user);
            }
            return user;
        }catch (Exception e){
            log.info(e.getMessage(),e);
            return null;
        }

    }

}
