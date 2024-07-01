package com.yupi.springbootinit.biRMQ;

import com.rabbitmq.client.Channel;
import com.yupi.springbootinit.common.BaseResponse;
import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.common.ResultUtils;
import com.yupi.springbootinit.constant.CommonConstant;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.springbootinit.exception.ThrowUtils;
import com.yupi.springbootinit.manager.AiManager;
import com.yupi.springbootinit.model.entity.Chart;
import com.yupi.springbootinit.service.ChartService;
import com.yupi.springbootinit.utils.ExcelUtils;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import static com.yupi.springbootinit.biRMQ.BiMqConstant.BI_QUEUE_NAME;

@Component
@Slf4j
public class BiComsumer {

    @Resource
    private ChartService chartService;

    @Resource
    private AiManager aiManager;

    @SneakyThrows
    @RabbitListener(queues = {BI_QUEUE_NAME},ackMode = "MANUAL")
    public void receiveMessage(String id, Channel channel,@Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag){
        //参数死记硬背
        //先修改图表任务为：“执行中”，等执行成功后，修改为“已完成”、保存执行结果；执行失败后，状态改为“失败"，记录任务失败信息。
        if (id == null && Long.parseLong(id) < 0){
            channel.basicNack(deliveryTag,false,false);
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        //todo 修改
        System.out.println("消费者执行了咯99999999999999999");
        long biModelId = CommonConstant.BI_MODEL_ID;

        Chart updateChart = new Chart();
        updateChart.setId(Long.valueOf(id));
        updateChart.setStatus("执行中");
        //处理异常两种方式

        boolean b = chartService.updateById(updateChart);
        if (!b){
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(updateChart.getId(),"更新图表状态失败");
//               throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            return;
        }
        //根据chartId获取要生成的chart信息
            String userInput = getGoalById(updateChart.getId());
        if (StringUtils.isAnyBlank(userInput) ){
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(updateChart.getId(),"更新图表状态失败");
//               throw new BusinessException(ErrorCode.SYSTEM_ERROR);
            return;
        }
        //根据需求生成结论和图表
        String result = aiManager.doChat(biModelId, userInput.toString());
        String[] splits = result.split("【【【【【");
        if (splits.length < 3) {
            handleChartUpdateError(updateChart.getId(),"更新图表状态失败");
            channel.basicNack(deliveryTag,false,false);
            return;
//            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "AI 生成错误");
        }
        String genChart = splits[1].trim();
        String genResult = splits[2].trim();
        updateChart.setGenChart(genChart);
        updateChart.setGenResult(genResult);
        updateChart.setStatus("已完成");
        boolean update = chartService.updateById(updateChart);
        if (!update){
            channel.basicNack(deliveryTag,false,false);
            handleChartUpdateError(updateChart.getId(),"更新图表状态失败");
        }
        channel.basicAck(deliveryTag, false);


    }

    private void handleChartUpdateError(long chartId, String execMessage) {
        Chart updateChartResult = new Chart();
        updateChartResult.setId(chartId);
        updateChartResult.setStatus("failed");
        updateChartResult.setExecMessage("execMessage");
        boolean updateResult = chartService.updateById(updateChartResult);
        if (!updateResult) {
            log.error("更新图表失败状态失败" + chartId + "," + execMessage);
        }
    }

    public BaseResponse<Chart> getChartById(long id) {
        if (id <= 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Chart chart = chartService.getById(id);
        if (chart == null) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }
        return ResultUtils.success(chart);
    }

    public String getGoalById(Long chartId){
        ThrowUtils.throwIf(chartId == null,ErrorCode.PARAMS_ERROR);
        Chart chart = chartService.getById(chartId);
        // 构造用户输入
        StringBuilder userInput = new StringBuilder();
        String chartType = chart.getChartType();
        String csvData = chart.getChartData();
        String userGoal = chart.getGoal();

        userInput.append("分析需求：").append("\n");
        // 拼接分析目标
        if (StringUtils.isNotBlank(chartType)) {
            userGoal += "，请使用" + chartType;
        }
        userInput.append(userGoal).append("\n");
        userInput.append("原始数据：").append("\n");
        // 压缩后的数据
//        String csvData = ExcelUtils.excelToCsv(chartData);
        userInput.append(csvData).append("\n");
        return userInput.toString();
    }

}
