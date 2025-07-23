package com.sprint.mission.discodeit.common.failure;

import com.sprint.mission.discodeit.common.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "async_failure")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsyncTaskFailure extends BaseUpdatableEntity {

  @Column(name = "task_name", nullable = false)
  private String taskName;

  @Column(name = "request_id", nullable = false)
  private String requestId;

  @Column(name = "failure_type", nullable = false)
  private String failureType;

  public AsyncTaskFailure(String taskName, String requestId, String failureType) {
    this.taskName = taskName;
    this.requestId = requestId;
    this.failureType = failureType;
  }

}