package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

public interface BinaryContentStorage {

    UUID put(UUID id, byte[] bytes);


    InputStream get(UUID id) throws IOException;

    ResponseEntity<?> download(BinaryContentDto binaryContentDto);

}
