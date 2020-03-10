package org.zxs.test.step010;

import org.zxs.test.step010.entity.UserEntity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class App010 {
    public static void main(String args[]){
        new App010().getResult();
    }

    private void getResult(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbstr = "jdbc:mysql://localhost:3306/testdb?user=root&password=root";
            Connection conn = DriverManager.getConnection(dbstr);
            Statement stmt = conn.createStatement();

            String sql = "select * from t_user";

            ResultSet rs = stmt.executeQuery(sql);
            long t1 = System.currentTimeMillis();

            while (rs.next()){
                UserEntity user = new UserEntity();
                user.userId = rs.getInt("user_id");
                user.username = rs.getString("user_name");
                user.passwrod = rs.getString("password");

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
