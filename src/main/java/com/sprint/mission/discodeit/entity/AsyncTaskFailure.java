package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskFailure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private String requestId;
    private String failureReason;

    @Builder
    public AsyncTaskFailure(String taskName, String requestId, String failureReason) {
        this.taskName = taskName;
        this.requestId = requestId;
        this.failureReason = failureReason;
    }

}
