package com.sprint.mission.discodeit.config;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "discodeit.storage.local")
public class LocalStorageProperties {

    @NotBlank
    private String rootPath;
}