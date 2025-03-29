package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.basic.repositoryimpl.*;
import com.sprint.mission.discodeit.file.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
@Configuration
public class RepositoryConfig {

    @Value("${discodeit.storage.data-dir:data}")
    private String dataDir;
    @Bean
    @Primary // FileUserRepositoryImplement를 기본 구현체로 사용
    public UserRepository fileUserRepository() {
        return new FileUserRepositoryImplement(dataDir);
    }

    @Bean
    public UserRepository basicUserRepository() {
        return new BasicUserRepositoryImplement(/* 필요한 파라미터 */); // 기본 구현체, 파라미터 전달
    }

    @Bean
    @Primary // FileChannelRepositoryImplement를 기본 구현체로 사용
    public ChannelRepository fileChannelRepository() {
        return new FileChannelRepositoryImplement(dataDir);
    }

    @Bean
    public ChannelRepository basicChannelRepository() {
        return new BasicChannelRepositoryImplement();
    }

    @Bean
    @Primary // FileMessageRepositoryImplement를 기본 구현체로 사용
    public MessageRepository fileMessageRepository() {
        return new FileMessageRepositoryImplement(dataDir);
    }

    @Bean
    public MessageRepository basicMessageRepository() {
        return new BasicMessageRepositoryImplement(); // 기본 구현체
    }

    @Bean
    @Primary // FileReadStatusRepositoryImplement를 기본 구현체로 사용
    public ReadStatusRepository fileReadStatusRepository() {
        return new FileReadStatusRepositoryImplement(dataDir);
    }

    @Bean("basicReadStatusRepositoryImplement") // Bean 이름 수정
    public ReadStatusRepository basicReadStatusRepository() {
        return new BasicReadStatusRepositoryImplement(); // 기본 구현체
    }


    @Bean
    public BinaryContentRepository basicBinaryContentRepository() {
        return new BasicBinaryContentRepositoryImplement(); // 기본 구현체
    }

    @Bean
    @Primary // FileUserStatusRepositoryImpl를 기본 구현체로 사용
    public UserStatusRepository fileUserStatusRepository() {
        return new FileUserStatusRepositoryImpl(dataDir);
    }

    @Bean
    public UserStatusRepository basicUserStatusRepository() {
        return new UserStatusRepositoryImplement(/* 필요한 파라미터 */); // 기본 구현체, 파라미터 전달
    }
}