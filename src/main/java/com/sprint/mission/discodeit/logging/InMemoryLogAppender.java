package com.sprint.mission.discodeit.logging;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.AppenderBase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class InMemoryLogAppender extends AppenderBase<ILoggingEvent> {

  private static final List<String> LOGS = Collections.synchronizedList(new ArrayList<>());

  @Override
  protected void append(ILoggingEvent event) {
    LOGS.add(event.getFormattedMessage());
  }

  public static List<String> getLogs() {
    return List.copyOf(LOGS);
  }

  public static void clearLogs() {
    LOGS.clear();
  }
}
