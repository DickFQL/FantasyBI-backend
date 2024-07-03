package com.yupi.springbootinit.job.once;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class C3P0Example {
 private static final ComboPooledDataSource dataSource;

 static {
 dataSource = new ComboPooledDataSource();
 dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/fantasybi?useUnicode=true&serverTimezone=GMT%2B8");
 dataSource.setUser("root");
 dataSource.setPassword("root");
 try {
 dataSource.setDriverClass("com.mysql.cj.jdbc.Driver");
} catch (PropertyVetoException e){
 e.printStackTrace();
}

//设置连接池配置
 dataSource.setMinPoolSize(5);
 dataSource.setMaxPoolSize(20);
 dataSource.setAcquireIncrement(5);
}

 public static void main(String[] args){
 try (Connection connection = dataSource.getConnection();
 PreparedStatement statement = connection.prepareStatement("SELECT * FROM chart");
 ResultSet resultSet = statement.executeQuery()){

   while (resultSet.next()){
  //处理结果集
   System.out.println(resultSet.getString("name"));
  }

} catch (SQLException e){
 e.printStackTrace();
}
}
}