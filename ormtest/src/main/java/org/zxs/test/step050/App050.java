package org.zxs.test.step050;

import org.zxs.test.step050.entity.AbstractEntityHelper;
import org.zxs.test.step050.entity.EntityHelperFactory;
import org.zxs.test.step050.entity.UserEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App050 {
    public static void main(String args[]){
        new App050().getResult();
    }

    private void getResult(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbstr = "jdbc:mysql://localhost:3306/testdb?user=root&password=root";
            Connection conn = DriverManager.getConnection(dbstr);
            Statement stmt = conn.createStatement();

            String sql = "select * from t_user";

            //创建助手类
            AbstractEntityHelper helper = EntityHelperFactory.getEntityHelper(UserEntity.class);
            ResultSet rs = stmt.executeQuery(sql);
            long t1 = System.currentTimeMillis();

            while (rs.next()){
                UserEntity user = (UserEntity)helper.create(rs);
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
