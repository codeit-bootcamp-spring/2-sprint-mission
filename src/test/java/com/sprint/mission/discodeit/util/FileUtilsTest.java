package com.sprint.mission.discodeit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class FileUtilsTest {
    @TempDir
    private Path path;

    @Test
    void 파일_유틸리티_저장_테스트() {
        Integer savedInteger = FileUtils.loadAndSave(path.resolve("random.ser"),
                (Map<String, Integer> data) -> {
                    data.put("2", 2);
                    return data.get("2");
                }
        );

        assertThat(savedInteger).isEqualTo(2);
    }
}