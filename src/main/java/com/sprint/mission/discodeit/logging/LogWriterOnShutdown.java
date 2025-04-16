package com.sprint.mission.discodeit.logging;

import jakarta.annotation.PreDestroy;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LogWriterOnShutdown {

  @PreDestroy
  public void onShutdown() {
    List<String> logs = InMemoryLogAppender.getLogs();
    String timestamp = LocalDateTime.now()
        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
    Path logPath = Paths.get("logs", "log-on-shutdown-" + timestamp + ".txt");

    try {
      Files.createDirectories(logPath.getParent());
      Files.write(logPath, logs, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
      log.info("📝 로그 저장 완료: {}", logPath);
    } catch (IOException e) {
      log.error("📛 로그 파일 저장 실패", e);
    }
  }

}
