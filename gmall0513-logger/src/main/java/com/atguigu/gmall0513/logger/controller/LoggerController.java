package com.atguigu.gmall0513.logger.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atguigu.gmall0513.common.constants.GmallConstant;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
public class LoggerController {
    @Autowired
    KafkaTemplate<String, String> kafkaTemplate;

    @PostMapping("/log")
    public String log(@RequestParam("logString") String logString) {
        //写日志工具
        JSONObject jsonObject = JSON.parseObject(logString);
        jsonObject.put("ts", System.currentTimeMillis());

        if ("startup".equals(jsonObject.get("type"))) {
            kafkaTemplate.send(GmallConstant.KAFKA_STARTUP,jsonObject.toJSONString());
        } else {
            kafkaTemplate.send(GmallConstant.KAFKA_EVENT,jsonObject.toJSONString());
        }

        log.info(logString);

        //System.out.println(logString);
        return "success";
    }
}
