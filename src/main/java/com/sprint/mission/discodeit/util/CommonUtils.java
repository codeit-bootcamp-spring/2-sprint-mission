package com.sprint.mission.discodeit.util;

import com.sprint.mission.discodeit.exception.Valid.DuplicateUserException;
import com.sprint.mission.discodeit.exception.Valid.InvalidTokenException;

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

    public static <T> void checkUserDuplicate(List<T> list, String data, Function<T, String> function) {
        list.stream().filter(t->function.apply(t).equals(data)).findFirst().ifPresent(t -> {throw new DuplicateUserException("중복된 유저가 존재합니다.");});
    }

    public static void checkValidToken(String token) {
        JwtUtil jwtUtil = new JwtUtil();
        Boolean validated = jwtUtil.validateToken(token);
        if (!validated) {
            throw new InvalidTokenException("유효하지 않은 토큰입니다.");
        }
    }
}
