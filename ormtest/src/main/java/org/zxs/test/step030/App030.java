package org.zxs.test.step030;

import org.zxs.test.step030.entity.UserEntity;
import org.zxs.test.step030.entity.XxxEntity_Helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App030 {
    public static void main(String args[]){
        new App030().getResult();
    }

    private void getResult(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbstr = "jdbc:mysql://localhost:3306/testdb?user=root&password=root";
            Connection conn = DriverManager.getConnection(dbstr);
            Statement stmt = conn.createStatement();

            String sql = "select * from t_user";

            //创建助手类
            XxxEntity_Helper helper = new XxxEntity_Helper();
            ResultSet rs = stmt.executeQuery(sql);
            long t1 = System.currentTimeMillis();

            while (rs.next()){
                UserEntity user = helper.create(UserEntity.class,rs);
            }

            long t2 = System.currentTimeMillis();
            rs.close();
            stmt.close();
            conn.close();

            System.out.println("t2-t1: "+(t2 - t1) + " ms");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
