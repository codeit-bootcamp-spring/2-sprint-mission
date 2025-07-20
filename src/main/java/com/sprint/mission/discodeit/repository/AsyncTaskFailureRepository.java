package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.AsyncTaskFailure;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsyncTaskFailureRepository extends JpaRepository<AsyncTaskFailure, UUID> {

}
