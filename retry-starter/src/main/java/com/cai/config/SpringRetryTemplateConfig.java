package com.cai.config;

import com.cai.exception.ServiceException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.FixedBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cai
 * @className SpringRetryTemplateConfig
 * @description 重试配置类
 * @dateTime 2023/8/10 17:33
 */
@Configuration
public class SpringRetryTemplateConfig {

    @Bean("springRetryTemplate")
    public RetryTemplate springRetryTemplate() {
        // 重试的异常map，key:异常的class类字节码 value:true表示重试
        Map<Class<? extends Throwable>, Boolean> exceptionMap = new HashMap<>();
        exceptionMap.put(ServiceException.class, true);

        RetryTemplate retryTemplate = new RetryTemplate();

        // 重试策略，参数依次代表：最大尝试次数、可重试的异常映射
        SimpleRetryPolicy retryPolicy = new SimpleRetryPolicy(3, exceptionMap);
        retryTemplate.setRetryPolicy(retryPolicy);

        // 重试间隔时间
        FixedBackOffPolicy backOffPolicy = new FixedBackOffPolicy();
        backOffPolicy.setBackOffPeriod(1000L);
        retryTemplate.setBackOffPolicy(backOffPolicy);

        return retryTemplate;
    }
}
