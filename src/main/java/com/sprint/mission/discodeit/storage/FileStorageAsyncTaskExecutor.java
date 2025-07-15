package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.AsyncTaskFailure;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

@Slf4j
@Service
public class FileStorageAsyncTaskExecutor {

  /**
   * 주어진 작업을 비동기적으로 실행하고 결과를 CompletableFuture로 반환합니다.
   * 이 메서드 자체가 @Async 애노테이션을 가지므로,
   * 이 메서드가 호출될 때 별도의 스레드에서 람다를 실행하게 됩니다.
   *
   * @param task 비동기적으로 실행할 Supplier 람다 (실제 파일 저장 로직 포함)
   * @param <T> 작업 결과의 타입
   * @return 작업 결과를 담는 CompletableFuture
   */
  @Async("fileExecutor") // AsyncConfig에 정의된 fileExecutor를 사용하도록 명시
  @Retryable(
      retryFor = { RuntimeException.class },
      maxAttempts = 5,
      backoff = @Backoff(delay = 1000, multiplier = 2, random = true)
  )
  public <T> CompletableFuture<T> executeAsync(Supplier<T> task) {
    // 여기서는 CompletableFuture.supplyAsync()를 사용하지 않습니다.
    // @Async가 이미 이 메서드 호출을 비동기 스레드로 넘겨주기 때문입니다.
    // 따라서, 람다 'task.get()' 내부에서 발생하는 모든 예외는
    // @Async 프록시에 의해 CompletableFuture에 캡슐화됩니다.
    return CompletableFuture.completedFuture(task.get());
  }

  /**
   * 주어진 작업을 비동기적으로 실행하고 결과를 반환하지 않습니다 (void).
   * @param task 비동기적으로 실행할 Runnable 람다
   */
  @Async("fileExecutor")
  public void executeAsync(Runnable task) {
    task.run();
  }

  @Recover
  public <T> CompletableFuture<T> recover(RuntimeException e, Supplier<T> task) {
    String taskName = "BinaryContentStorage.put";
    String requestId = MDC.get("requestId");
    if (requestId == null) {
      requestId = "N/A";
    }
    String failureReason = "파일 업로드 실패: " + e.getMessage() + " - 스택트레이스: " + e.toString();

    AsyncTaskFailure failure = new AsyncTaskFailure(taskName, requestId, failureReason);
    log.error("Async Task Failure : " + failureReason);

    return CompletableFuture.failedFuture(new RuntimeException(e));
  }
}