package com.sprint.mission.discodeit.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.mock.web.MockMultipartFile;

public class TestMultipartUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static MockMultipartFile jsonPart(String partName, Object dto) throws Exception {
        return new MockMultipartFile(
            partName,
            null,
            "application/json",
            objectMapper.writeValueAsBytes(dto));
    }
}
