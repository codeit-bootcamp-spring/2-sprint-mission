package com.sprint.sprint2.discodeit.repository.util;

import java.util.List;

public interface FindRepository<T> {

    T findById(String uuId);
    List<T> findByAll();
}
