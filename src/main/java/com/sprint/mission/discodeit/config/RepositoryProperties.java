package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "discodeit.repository.file-directory")
@Getter
@Setter
public class RepositoryProperties {
    private String binaryContent;
    private String channel;
    private String message;
    private String readStatus;
    private String user;
    private String userStatus;
}
