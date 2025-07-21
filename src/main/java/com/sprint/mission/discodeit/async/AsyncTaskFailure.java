package com.sprint.mission.discodeit.async;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "async_task_failure")
@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskFailure extends BaseEntity {

    private String taskName;
    private String requestId;
    private String failureReason;
}
