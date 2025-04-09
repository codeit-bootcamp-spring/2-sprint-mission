package com.sprint.discodeit.sprint5.repository.util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FindRepository<T> {

    Optional<T> findById(UUID uuId);
    List<T> findByAll();
}
