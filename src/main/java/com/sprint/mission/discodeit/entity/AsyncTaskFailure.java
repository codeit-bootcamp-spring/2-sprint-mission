package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "async_task_failure")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AsyncTaskFailure extends BaseEntity {

  @Column(name = "task_name", nullable = false)
  private String taskName;

  @Column(name = "request_id", nullable = false)
  private String requestId;

  @Column(name = "failure_reason", nullable = false)
  private String failureReason;
}
