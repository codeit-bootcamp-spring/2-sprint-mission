package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RepositoryConfig {

    @Value("${discodeit.repository.type:jcf}")
    private String repositoryType;

    @Bean
    public Map<Class<?>, Object> repositoryBeans(FileStorageManager fileStorageManager) {
        Map<Class<?>, Object> beans = new HashMap<>();

        if ("file".equals(repositoryType)) {
            beans.put(BinaryContentRepository.class, new FileBinaryContentRepository(fileStorageManager));
            beans.put(UserRepository.class, new FileUserRepository(fileStorageManager));
            beans.put(ReadStatusRepository.class, new FileReadStatusRepository(fileStorageManager));
            beans.put(MessageRepository.class, new FileMessageRepository(fileStorageManager));
            beans.put(ChannelRepository.class, new FileChannelRepository(fileStorageManager));
            beans.put(UserStatusRepository.class, new FileUserStatusRepository(fileStorageManager));
        } else {
            beans.put(BinaryContentRepository.class, new JCFBinaryContentRepository());
            beans.put(UserRepository.class, new JCFUserRepository());
            beans.put(ReadStatusRepository.class, new JCFReadStatusRepository());
            beans.put(MessageRepository.class, new JCFMessageRepository());
            beans.put(ChannelRepository.class, new JCFChannelRepository());
            beans.put(UserStatusRepository.class, new JCFUserStatusRepository());
        }

        return beans;
    }

    @Bean
    public BinaryContentRepository binaryContentRepository(Map<Class<?>, Object> repositoryBeans) {
        return (BinaryContentRepository) repositoryBeans.get(BinaryContentRepository.class);
    }

    @Bean
    public UserRepository userRepository(Map<Class<?>, Object> repositoryBeans) {
        return (UserRepository) repositoryBeans.get(UserRepository.class);
    }

    @Bean
    public ReadStatusRepository readStatusRepository(Map<Class<?>, Object> repositoryBeans) {
        return (ReadStatusRepository) repositoryBeans.get(ReadStatusRepository.class);
    }

    @Bean
    public MessageRepository messageRepository(Map<Class<?>, Object> repositoryBeans) {
        return (MessageRepository) repositoryBeans.get(MessageRepository.class);
    }

    @Bean
    public ChannelRepository channelRepository(Map<Class<?>, Object> repositoryBeans) {
        return (ChannelRepository) repositoryBeans.get(ChannelRepository.class);
    }

    @Bean
    public UserStatusRepository userStatusRepository(Map<Class<?>, Object> repositoryBeans) {
        return (UserStatusRepository) repositoryBeans.get(UserStatusRepository.class);
    }
}

