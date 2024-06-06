package com.yupi.springbootinit.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.yupi.springbootinit.model.entity.Chart;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
* @author Mitsuha
* @description 针对表【chart(图表信息表)】的数据库操作Mapper
* @createDate 2024-04-18 21:10:14
* @Entity com.yupi.springbootinit.model.entity.Chart
*/
public interface ChartMapper extends BaseMapper<Chart> {

    List<Map<String ,Object>> selectTable(String querySql);
}




