package com.cai.controller;

import com.cai.service.DemoTaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cai
 * @className RetryController
 * @description 重试控制器
 * @dateTime 2023/8/10 17:31
 */
@RestController
@RequestMapping("/retry")
@RequiredArgsConstructor
public class RetryController {

    private final DemoTaskService demoTaskService;

    @GetMapping("/spring")
    public Boolean springRetry(@RequestParam("param") String param) {
        return demoTaskService.testTask(param);
    }

    @GetMapping("/spring/annotation")
    public Boolean springRetryAnnotation(@RequestParam("param") String param) {
        return demoTaskService.annotationTestTask(param);
    }

    @GetMapping("/guava")
    public Boolean guavaRetry(@RequestParam("param") String param) {
        return demoTaskService.guavaTestTask(param);
    }

}
