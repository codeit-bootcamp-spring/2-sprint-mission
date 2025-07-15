package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.mdc.MdcThreadLocalAccessor;
import io.micrometer.context.ContextRegistry;
import jakarta.annotation.PostConstruct;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContextPropagationConfig {

  @PostConstruct
  public void registerMdcAccessor() {
    ContextRegistry.getInstance()
        .registerThreadLocalAccessor(new MdcThreadLocalAccessor());
  }
}
