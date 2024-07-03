package com.yupi.springbootinit.job.once;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class PreProcess {
    @Test
    void test() throws InterruptedException {
        PreProcessing.seperateRW();
    }
}
