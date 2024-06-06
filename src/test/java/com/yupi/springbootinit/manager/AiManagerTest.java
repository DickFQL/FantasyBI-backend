package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.constant.CommonConstant;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class AiManagerTest {

    @Resource
    private AiManager aiManager;
    @Test
    void testAi(){
        String string = aiManager.doChat(CommonConstant.BI_MODEL_ID,"能给我推荐一些邓紫棋的歌曲吗");
        System.out.println(string);
    }
}
