package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "discodeit.repository")
@Getter
@Setter
public class RepositoryProperties {
    private String type;
    private String fileDirectory;
}