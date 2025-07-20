package com.sprint.mission.discodeit.config;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.jsontype.impl.LaissezFaireSubTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.time.Duration;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
@EnableCaching
public class CacheConfig {

//  @Bean
//  public CacheManager cacheManager() {
//    CaffeineCacheManager cacheManager = new CaffeineCacheManager("channelsByUser",
//        "notificationsByUser", "users");
//    cacheManager.setCaffeine(Caffeine.newBuilder()
//        .expireAfterWrite(10, TimeUnit.MINUTES)
//        .maximumSize(1000)
//        .recordStats());
//    return cacheManager;
//  }

  @Bean
  public CacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    ObjectMapper mapper = new ObjectMapper();
    mapper.registerModule(
        new JavaTimeModule()); //java.time.Instant 타입을 JSON으로 직렬화할 때, 기본 Jackson 설정으로는 처리 X -> Jackson Serializer에 적용
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    // 타입 정보를 포함한 직렬화 활성화
    mapper.activateDefaultTyping(
        LaissezFaireSubTypeValidator.instance,
        ObjectMapper.DefaultTyping.NON_FINAL,
        JsonTypeInfo.As.PROPERTY
    );

    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(mapper);

    RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(serializer))
        .entryTtl(Duration.ofMinutes(10))
        .disableCachingNullValues();

    return RedisCacheManager.builder(connectionFactory)
        .cacheDefaults(config)
        .build();
  }

}
