package com.sprint.mission.discodeit.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import jakarta.persistence.Id;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AsyncTaskFailure {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String taskName;
    private String requestId;
    private String failureReason;
    private Instant failedAt;

    @Builder
    public AsyncTaskFailure(String taskName, String requestId, String failureReason, Instant failedAt) {
        this.taskName = taskName;
        this.requestId = requestId;
        this.failureReason = failureReason;
        this.failedAt = failedAt;
    }
}