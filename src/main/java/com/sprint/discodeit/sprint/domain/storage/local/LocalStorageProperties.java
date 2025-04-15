package com.sprint.discodeit.sprint.domain.storage.local;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "discodeit.storage.local")
public class LocalStorageProperties {
    private String rootPath;
}
