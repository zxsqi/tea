package com.person.hero;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Mysql会话工厂会
 */
public final class MySqlSessionFactory {
    private static final Logger log = LoggerFactory.getLogger(MySqlSessionFactory.class);
    private static SqlSessionFactory _sqlSessionFactory;
    private MySqlSessionFactory(){

    }

    public static void init(){
        try {
            _sqlSessionFactory = (new SqlSessionFactoryBuilder()).build(Resources.getResourceAsStream("MyBatisConfig.xml"));
        }catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public static SqlSession openSession(){
        if(null == _sqlSessionFactory){
            log.info("_sqlSessionFactory 未被初始化");
            throw new RuntimeException("\"_sqlSessionFactory 未被初始化\"");
        }

        return _sqlSessionFactory.openSession(true);
    }
}
