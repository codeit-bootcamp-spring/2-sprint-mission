package com.sprint.mission.discodeit.Util;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

public class CommonUtils {
    public static <T> Optional<T> findByName(List<T> list, String name, Function<T, String> function) {
        return list.stream().filter(t -> function.apply(t).equals(name)).findFirst();
    }

    public static <T> Optional<T> findById(List<T> list, UUID id, Function<T, UUID> function) {
        return list.stream().filter(t -> function.apply(t).equals(id)).findFirst();
    }

    public static <T> List<T> findAllById(List<T> list, UUID id, Function<T, UUID> function) {
        return list.stream().filter(t -> function.apply(t).equals(id)).toList();
    }
}
