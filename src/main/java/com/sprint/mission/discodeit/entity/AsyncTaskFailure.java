package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import java.util.UUID;
import lombok.*;

@Entity
@Table(name = "async_task_failure")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class AsyncTaskFailure extends BaseEntity {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private UUID id;

  // 실패한 작업 이름 (예: "FileUpload")
  @Column(nullable = false)
  private String taskName;

  // MDC에서 가져온 Request ID
  @Column(nullable = false, length = 100)
  private String requestId;

  // 실패 원인 메시지
  @Column(nullable = false, length = 1000)
  private String failureReason;

  public AsyncTaskFailure(String fileName, String requestId, String message) {
    this.taskName = fileName;
    this.requestId = requestId;
    this.failureReason = message;
  }
}

