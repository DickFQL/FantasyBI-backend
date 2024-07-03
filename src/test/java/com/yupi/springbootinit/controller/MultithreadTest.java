package com.yupi.springbootinit.controller;

import com.yupi.springbootinit.config.ThreadPoolExecutorConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadPoolExecutor;

@SpringBootTest
public class MultithreadTest {

    @Resource
    private ThreadPoolExecutorConfig threadPoolExecutorConfig;

    @Resource
    private ThreadPoolExecutor threadPoolExecutor;

    @Test
    void test(){

        CompletableFuture<Void> completableFuture = CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(10000);
                System.out.println("你好呀");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }, threadPoolExecutor);
        completableFuture.join();
    }

    @Test
    void test2() throws InterruptedException {
        for (int i = 0; i < 4; i++) {
            test();
        }
//        Thread.sleep(20000);
    }

}
