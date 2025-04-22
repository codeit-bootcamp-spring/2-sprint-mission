package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@ConditionalOnProperty(name = "discodeit.storage.type", havingValue = "local")
public class LocalBinaryContentStorage implements BinaryContentStorage {
    private final Path root;
    private final String EXTENSION = ".ser";

    public LocalBinaryContentStorage(
            @Value("${discodeit.storage.local.root-path}") String root
    ) {
        this.root = Paths.get(root);
        init();
    }

    private void init() {
        try{
            if(!Files.exists(root)){
                Files.createDirectories(root);
            }
        }catch (IOException e){
            throw new RuntimeException("디렉토리 생성 실패",e);
        }
    }

    private Path resolvePath(UUID id) { return root.resolve(id+EXTENSION); }

    @Override
    public UUID put(UUID id, byte[] data) {
        Path path = resolvePath(id);
        try {
            Files.write(path, data);
            return id;
        }catch (IOException e){
            throw new RuntimeException("BinaryContent 저장 실패 :"+e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        try {
            return Files.newInputStream(path);
        } catch (IOException e) {
            throw new RuntimeException("BinaryContent 읽기 실패: " + id, e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        InputStream inputStream = get(binaryContentDto.id());
        InputStreamResource resource = new InputStreamResource(inputStream);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + binaryContentDto.fileName() + "\"")
                .contentType(MediaType.parseMediaType(binaryContentDto.contentType()))
                .body(resource);
    }
}
