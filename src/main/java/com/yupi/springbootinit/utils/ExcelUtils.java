package com.yupi.springbootinit.utils;


import cn.hutool.core.collection.CollUtil;
import cn.hutool.http.HttpRequest;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ExcelUtils {

    public static String excelToCsv(MultipartFile multipartFile) throws FileNotFoundException {
        File file = null;
        try {
            file = ResourceUtils.getFile("classpath:test_excel.xlsx");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        List<Map<Integer, String>> list = EasyExcel.read(file)
                .excelType(ExcelTypeEnum.XLSX)
                .sheet()
                .headRowNumber(0)
                .doReadSync();
        if (CollUtil.isEmpty(list)){
            return "";
        }
        //转换为csv
        StringBuilder stringBuilder = new StringBuilder();
        //读取表头
        LinkedHashMap<Integer, String> linkedHashMap = (LinkedHashMap) list.get(0);
//        String string = StringUtils.join(linkedHashMap.values(), ",");
        List<String> collect = linkedHashMap.values().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(collect,",")+"\n");
//        System.out.println(StringUtils.join(collect,","));
        for (int i = 1; i < list.size(); i++) {
            LinkedHashMap<Integer, String> integerStringMap = (LinkedHashMap)list.get(i);
            List<String> data = integerStringMap.values().stream().filter(StringUtils::isNotEmpty).collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(data,",")+"\n");
//            System.out.println(StringUtils.join(data,","));
        }

        System.out.println(stringBuilder.toString());
        return stringBuilder.toString();
    }

    public static void main(String[] args) throws FileNotFoundException {

        excelToCsv(null);
    }
}
