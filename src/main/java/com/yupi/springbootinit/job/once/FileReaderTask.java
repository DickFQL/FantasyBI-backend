package com.yupi.springbootinit.job.once;

import com.yupi.springbootinit.model.entity.Chart;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 读文件线程操作
 */
class FileReaderTask implements Runnable {
    private final BlockingQueue<Chart> queue;
    private final String inputFile;

    public FileReaderTask(BlockingQueue<Chart> queue, String inputFile) {
        this.queue = queue;
        this.inputFile = inputFile;
    }

    @Override
    public void run() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(inputFile), "UTF-8"))) {
            String line;
            int i = 0;
            Chart chart = new Chart();
            while ((line = br.readLine()) != null) {
                String[] split = line.split(",");
                chart.setId(Long.valueOf(split[0]));
                chart.setGoal(split[1]);
                chart.setName(split[2]);
                chart.setChartData(split[3]);
                chart.setChartType(split[4]);
                chart.setGenChart(split[5]);
                chart.setGenResult(split[6]);
                chart.setStatus(split[7]);
                chart.setExecMessage(split[8]);
                chart.setUserId(Long.valueOf(split[9]));
//                Date(split[10] );
//                chart.setCreateTime(split[10]);
//                chart.setUpdateTime(split[11]);
                chart.setIsDelete(Integer.valueOf(split[12]));
                System.out.println("这是第"+i+"条数据" + line);
                queue.put(chart);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                // 结束标记
                Chart chart = new Chart();
                chart.setName("EOF");
                queue.put(chart);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public Integer countComma(String currentLine){
        String regex =",(?=([^\"]*\"[^\"]*\")*[^\"]*$)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(currentLine);
        int count = 0;
        while (matcher.find()) count++;
        return count;
    }
}
