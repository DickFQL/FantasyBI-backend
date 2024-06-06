package com.yupi.springbootinit.controller;



import cn.hutool.json.JSONUtil;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@RestController
public class QueueController {

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @GetMapping("/add")
    public void add(String name){

        CompletableFuture.runAsync(()->{
            System.out.println("任务执行中:"+name+",执行人:"+Thread.currentThread().getName());
            try{
                Thread.sleep(30000);
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        },threadPoolExecutor);

    }

    @GetMapping("/get")
    public String get(){
        Map<Object, Object> map = new HashMap<>();
        map.put("队列长度",threadPoolExecutor.getQueue().size());
        map.put("任务总数量",threadPoolExecutor.getTaskCount());
        map.put("已完成任务总数",threadPoolExecutor.getCompletedTaskCount());
        map.put("正在工作线程？？？",threadPoolExecutor.getActiveCount());
        map.put("最大线程池",threadPoolExecutor.getLargestPoolSize());
//        map.put("获得线程工厂",threadPoolExecutor.getThreadFactory());
        return JSONUtil.toJsonStr(map);


    }
}
