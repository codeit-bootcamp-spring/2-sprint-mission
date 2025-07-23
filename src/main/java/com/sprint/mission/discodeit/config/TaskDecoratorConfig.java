package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.config.async.ChainedTaskDecorator;
import com.sprint.mission.discodeit.config.async.MDCTaskDecorator;
import com.sprint.mission.discodeit.config.async.SecurityContextTaskDecorator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;

@Configuration
public class TaskDecoratorConfig {

    @Bean
    public TaskDecorator taskDecorator(MDCTaskDecorator mdcTaskDecorator, SecurityContextTaskDecorator securityContextTaskDecorator) {
        return new ChainedTaskDecorator(mdcTaskDecorator, securityContextTaskDecorator);
    }
}