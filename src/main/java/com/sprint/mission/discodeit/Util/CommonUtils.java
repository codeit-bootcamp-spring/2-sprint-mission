package com.sprint.mission.discodeit.Util;

import java.util.List;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Stream;

public class CommonUtils {
    public static <T> Stream<T> filterById(List<T> list, UUID id, Function<T, UUID> function) {
        return list.stream().filter(t -> function.apply(t).equals(id));
    }
}
