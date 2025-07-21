package com.sprint.mission.discodeit.config;


import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;

@Configurable
@EnableRetry
public class RetryConfig {
}
