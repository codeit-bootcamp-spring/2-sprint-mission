package com.sprint.discodeit.repository.util;

import java.util.UUID;

public interface SaveRepository<T> {

    public void save(T t);
    public void delete(UUID uuId);
}
