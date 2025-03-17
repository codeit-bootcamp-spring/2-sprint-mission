package com.sprint.discodeit.repository.util;

import java.util.List;
import java.util.Optional;

public interface FindRepository<T> {

    Optional<T> findById(String uuId);
    List<T> findByAll();
}
