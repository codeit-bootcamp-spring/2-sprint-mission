package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Entity;
import lombok.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AsyncTaskFailure extends BaseEntity {

    private String requestId;       // MDC에서 꺼낸 Request ID
    private String taskName;
    private String failureReason;
}
