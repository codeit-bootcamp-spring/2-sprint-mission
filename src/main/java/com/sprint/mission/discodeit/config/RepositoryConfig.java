package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {

    private static final String FILE_TYPE = "file";

    private final RepositoryProperties repositoryProperties;

    private final JCFUserRepository jcfUserRepository;
    private final JCFChannelRepository jcfChannelRepository;
    private final JCFMessageRepository jcfMessageRepository;
    private final JCFUserStatusRepository jcfUserStatusRepository;
    private final JCFReadStatusRepository jcfReadStatusRepository;
    private final JCFBinaryContentRepository jcfBinaryContentRepository;
    private final JCFBinaryDataRepository jcfBinaryDataRepository;

    private final FileUserRepository fileUserRepository;
    private final FileChannelRepository fileChannelRepository;
    private final FileMessageRepository fileMessageRepository;
    private final FileUserStatusRepository fileUserStatusRepository;
    private final FileReadStatusRepository fileReadStatusRepository;
    private final FileBinaryContentRepository fileBinaryContentRepository;
    private final FileBinaryDataRepository fileBinaryDataRepository;

    @Bean
    public UserRepository userRepository() {
        if(FILE_TYPE.equalsIgnoreCase(repositoryProperties.getType())) {
            return fileUserRepository;
        }
        return jcfUserRepository;
    }

    @Bean
    public ChannelRepository channelRepository() {
        if(FILE_TYPE.equalsIgnoreCase(repositoryProperties.getType())) {
            return fileChannelRepository;
        }
        return jcfChannelRepository;
    }

    @Bean
    public MessageRepository messageRepository() {
        if(FILE_TYPE.equalsIgnoreCase(repositoryProperties.getType())) {
            return fileMessageRepository;
        }
        return jcfMessageRepository;
    }

    @Bean
    public UserStatusRepository userStatusRepository() {
        if(FILE_TYPE.equalsIgnoreCase(repositoryProperties.getType())) {
            return fileUserStatusRepository;
        }
        return jcfUserStatusRepository;
    }

    @Bean
    public ReadStatusRepository readStatusRepository() {
        if(FILE_TYPE.equalsIgnoreCase(repositoryProperties.getType())) {
            return fileReadStatusRepository;
        }
        return jcfReadStatusRepository;
    }

    @Bean
    public BinaryContentRepository binaryContentRepository() {
        if(FILE_TYPE.equalsIgnoreCase(repositoryProperties.getType())) {
            return fileBinaryContentRepository;
        }
        return jcfBinaryContentRepository;
    }

    @Bean
    public BinaryDataRepository binaryDataRepository() {
        if(FILE_TYPE.equalsIgnoreCase(repositoryProperties.getType())) {
            return fileBinaryDataRepository;
        }
        return jcfBinaryDataRepository;
    }

}
