package com.person.hero.login;

import com.person.hero.MySqlSessionFactory;
import com.person.hero.async.AsyncOperationProcessor;
import com.person.hero.async.IasyncOperation;
import com.person.hero.login.db.IUserDao;
import com.person.hero.login.db.UserEntity;
import org.apache.ibatis.session.SqlSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;

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
     * @param callback 回调函数
     * @return
     */
    public void userLogin(String username , String password, Function<UserEntity,Void>callback){

        if(null == username || null == password){
            return ;
        }

        IasyncOperation asyncOp = new AsyncGetUserByName(username,password) {
            @Override
            public void dofinish() {
                callback.apply(this.getUserEntity());
            }
        };

        AsyncOperationProcessor.getInstance().process(asyncOp);
    }

    /**
     * 异步方式获取用户
     */
    private class AsyncGetUserByName implements IasyncOperation{

        //用户名称
        private final String _username;
        //用户密码
        private final String _password;
        //用户实体
        private UserEntity _userEntity = null;

        AsyncGetUserByName(String username,String password){
            _username = username;
            _password = password;
        }

        /**
         * 获取用户实体
         * @return
         */
        public UserEntity getUserEntity(){
            return _userEntity;
        }

        @Override
        public int bindId() {
            return _username.charAt(_username.length()-1);
        }

        @Override
        public void doAsync() {
            try (SqlSession session = MySqlSessionFactory.openSession()){
                IUserDao dao = session.getMapper(IUserDao.class);

                //查看当前线程
                log.info("当前线程 = {}" , Thread.currentThread().getName());
                UserEntity user = dao.getUserByName(_username);

                //判断用户密码
                if(null != user){
                    if(!_password.equals(user.password)){
                        log.info("用户密码错误，userId={},userName={}",user.userId,_username);
//                        throw new RuntimeException("用户密码错误");
                    }
                }else {
                    //如果实体为空则新建用户
                    user = new UserEntity();
                    user.userName = _username;
                    user.password = _password;
                    user.heroAvatar = "Hero_Shaman";

                    //将用户实体添加到数据库
                    dao.insertInto(user);
                }

                _userEntity = user;
            }catch (Exception e){
                log.info(e.getMessage(),e);
            }


        }
    }
}
