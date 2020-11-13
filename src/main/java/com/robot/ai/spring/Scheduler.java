package com.robot.ai.spring;

import com.forte.qqrobot.BotRuntime;
import com.forte.qqrobot.sender.MsgSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class Scheduler {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
    //每隔2秒执行一次
    //@Scheduled(fixedRate = 5000)
    public void testTasks() {
        System.out.println("定时任务执行时间：" + dateFormat.format(new Date()));

    }

    private MsgSender getSend(){
        return BotRuntime.getRuntime().getBotManager().getBot("3255414764").getSender();
    }
}
