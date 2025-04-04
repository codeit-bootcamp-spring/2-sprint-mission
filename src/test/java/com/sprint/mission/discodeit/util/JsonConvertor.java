package com.sprint.mission.discodeit.util;

import com.fasterxml.jackson.databind.ObjectMapper;

public final class JsonConvertor {
    private JsonConvertor() {
    }

    public static String asString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
