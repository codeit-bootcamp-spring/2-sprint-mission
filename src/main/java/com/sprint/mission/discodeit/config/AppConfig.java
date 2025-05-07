package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
// CreateDate, LastModifiedDate 어노테이션이 동작하기 위해 선언
@EnableJpaAuditing
// Application의 전반적인 설정을 따로 관리하는 class
public class AppConfig {

}
