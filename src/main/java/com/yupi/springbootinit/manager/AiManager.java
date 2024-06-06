package com.yupi.springbootinit.manager;

import com.yupi.springbootinit.common.ErrorCode;
import com.yupi.springbootinit.exception.BusinessException;
import com.yupi.yucongming.dev.client.YuCongMingClient;
import com.yupi.yucongming.dev.common.BaseResponse;
import com.yupi.yucongming.dev.model.DevChatRequest;
import com.yupi.yucongming.dev.model.DevChatResponse;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AiManager {

//    @Resource
//    private YuCongMingClient yuCongMingClient;

    /**
     * AI对话
     * @param message
     * @return
     */
    public String doChat(Long modelId, String message){
        String accessKey = "t36kg2fmq1u7bx6d7i32hjba01sp2kqc";
        String secretKey = "zxkev1n7p5bu7lqhw9c4bmzt5l1muk9d";

        YuCongMingClient yuCongMingClient = new YuCongMingClient(accessKey, secretKey);
        DevChatRequest devChatRequest = new DevChatRequest();
        devChatRequest.setModelId(modelId);
        devChatRequest.setMessage(message);
        BaseResponse<DevChatResponse> response = yuCongMingClient.doChat(devChatRequest);
        if (response == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"参数错误");
        }
        if (response.getData() == null) throw new BusinessException(ErrorCode.PARAMS_ERROR,"数据为空");
        String content = response.getData().getContent();

        return content;
    }



}
