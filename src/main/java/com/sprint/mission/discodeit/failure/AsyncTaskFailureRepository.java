package com.sprint.mission.discodeit.failure;

import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsyncTaskFailureRepository extends JpaRepository<AsyncTaskFailure, UUID> {

}
