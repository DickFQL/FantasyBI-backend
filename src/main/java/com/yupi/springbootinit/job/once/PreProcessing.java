package com.yupi.springbootinit.job.once;

import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;

import javax.annotation.Resource;
import java.io.*;
import java.util.concurrent.*;

public class PreProcessing {
        private static final int QUEUE_CAPACITY = 1000;

//        @Resource
//        private static ChartService chartService;


        public static void seperateRW() throws InterruptedException {
            String inputFile = "E:\\IDEAproject\\yupiproject1\\sql\\chart.csv";
            BlockingQueue<Chart> queue = new LinkedBlockingQueue<>(QUEUE_CAPACITY);
            ExecutorService executorService = Executors.newFixedThreadPool(2); // 一个读线程，一个写线程
            // 启动读线程，处理为一行数据
            executorService.execute(new FileReaderTask(queue, inputFile));
            // 启动写线程
            executorService.execute(new FileWriterTask(queue));
            executorService.shutdown();
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        }
    }

