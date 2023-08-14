package com.cai.service;

import com.cai.exception.ServiceException;
import com.github.rholder.retry.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author cai
 * @className DemoTaskService
 * @description 任务
 * @dateTime 2023/8/10 16:52
 */
@Slf4j
@Service
public class DemoTaskService {

    @Resource
    @Qualifier("springRetryTemplate")
    private RetryTemplate retryTemplate;

    public boolean guavaTestTask(String param) {
        // 构建重试实例 可以设置重试源且可以支持多个重试源 可以配置重试次数或重试超时时间，以及可以配置等待时间间隔
        Retryer<Boolean> retriever = RetryerBuilder.<Boolean>newBuilder()
                // 重试的异常类以及子类
                .retryIfExceptionOfType(ServiceException.class)
                // 根据返回值进行重试
                .retryIfResult(result -> !result)
                // 设置等待间隔时间,每次请求间隔1s
                .withWaitStrategy(WaitStrategies.fixedWait(1, TimeUnit.SECONDS))
                // 设置最大重试次数,尝试请求6次
                .withStopStrategy(StopStrategies.stopAfterAttempt(3))
                .build();
        try {
            return retriever.call(() -> randomResult(param));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    @Retryable(value = {ServiceException.class}, maxAttempts = 3, backoff = @Backoff(delay = 2000L, multiplier = 2))
    public boolean annotationTestTask(String param) {
        return randomResult(param);
    }

    /**
     * 根据相同参数和相同返回值匹配
     */
    @Recover
    public boolean annotationRecover(Exception e, String param) {
        log.error("参数:{},已达到最大重试次数或抛出了不在重试策略内的异常:", param, e);
        return false;
    }

    public boolean testTask(String param) {
        Boolean executeResult = retryTemplate.execute(retryCallback -> {
            boolean result = randomResult(param);
            log.info("调用的结果：{}", result);
            return result;
        }, recoveryCallback -> {
            log.info("已达到最大重试次数或抛出了不在重试策略内的异常");
            return false;
        });
        log.info("执行结果：{}", executeResult);
        return executeResult;
    }


    /**
     * 根据随机数模拟各种情况
     */
    public boolean randomResult(String param) {
        log.info("进入测试任务，参数：{}", param);

        int i = new Random().nextInt(10);
        log.info("随机生成的数：{}", i);
        switch (i) {
            case 0: {
                log.info("数字为0，返回成功");
                return true;
            }
            case 1: {
                log.info("数字为1，返回失败");
                return false;
            }
            case 2:
            case 3:
            case 4:
            case 5: {
                log.info("数字为2、3、4、5，抛出参数异常");
                throw new IllegalArgumentException("参数异常");
            }
            default: {
                log.info("数字大于5，抛出自定义异常");
                throw new ServiceException(10010, "远程调用失败");
            }
        }
    }

}
