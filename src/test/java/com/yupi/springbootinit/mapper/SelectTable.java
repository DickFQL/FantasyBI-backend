package com.yupi.springbootinit.mapper;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@SpringBootTest
public class SelectTable {

    @Resource
    private ChartMapper chartMapper;
    @Test
    void test(){
        List<Map<String, Object>> maps = chartMapper.selectTable("select * from `chart`");
        System.out.println(maps);
    }


}
