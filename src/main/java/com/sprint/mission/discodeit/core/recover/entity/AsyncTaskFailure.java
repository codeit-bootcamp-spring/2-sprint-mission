package com.sprint.mission.discodeit.core.recover.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "taskFailure")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class AsyncTaskFailure {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "task_name")
  private String taskName;

  @Column(name = "request_id")
  private String requestId;

  @Column(name = "failure_reason")
  private String failureReason;

  public AsyncTaskFailure(String taskName, String requestId, String failureReason) {
    this.taskName = taskName;
    this.requestId = requestId;
    this.failureReason = failureReason;
  }
}
