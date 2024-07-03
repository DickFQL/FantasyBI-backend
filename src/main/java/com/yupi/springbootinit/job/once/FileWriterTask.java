package com.yupi.springbootinit.job.once;

import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;

import javax.annotation.Resource;
import java.beans.PropertyVetoException;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.BlockingQueue;

class FileWriterTask implements Runnable {

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

    private final BlockingQueue<Chart> queue;
    @Resource
    private  ChartService chartService;

    public FileWriterTask(BlockingQueue<Chart> queue) {
        this.queue = queue;
//        this.chartService = chartService;
    }

    @Override
    public void run() {
            Chart chart = null;


                try {
                    while (true){
                        chart = queue.take();
                        if ("EOF".equals(chart.getName())) break;
                        String sql = "insert into chart(id,goal,name,chartData,chartType,genChart,genResult,status,execMessage,userID) values(?,?,?,?,?,?,?,?,?,?)";
                        try (Connection connection = dataSource.getConnection();
                             PreparedStatement statement = connection.prepareStatement(sql);
                             ){
                            statement.setLong(1,chart.getId());
                            statement.setString(2, chart.getGoal());
                            statement.setString(3, chart.getName());
                            statement.setString(4, chart.getChartData());
                            statement.setString(5, chart.getChartType());
                            statement.setString(6, chart.getGenChart());
                            statement.setString(7, chart.getGenResult());
                            statement.setString(8, chart.getStatus());
                            statement.setString(9, chart.getExecMessage());
                            statement.setLong(10, chart.getUserId());
                            statement.executeUpdate();

                        } catch (SQLException e){
                            e.printStackTrace();
                        }
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }



    }
}
