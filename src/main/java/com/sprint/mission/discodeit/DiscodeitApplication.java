package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.data.redis.RedisRepositoriesAutoConfiguration;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableAsync
@EnableCaching
@EnableScheduling
@EnableJpaAuditing
@SpringBootApplication(exclude = {RedisRepositoriesAutoConfiguration.class})
public class DiscodeitApplication {

	public static void main(String[] args) {
		SpringApplication.run(DiscodeitApplication.class, args);
	}

}
