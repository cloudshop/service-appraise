package com.eyun.appraise.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.eyun.appraise")
public class FeignConfiguration {

}
