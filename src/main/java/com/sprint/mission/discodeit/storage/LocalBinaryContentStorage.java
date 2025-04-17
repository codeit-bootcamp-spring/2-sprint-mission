package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
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

   public LocalBinaryContentStorage(@Value("{discodeit.storage.local.root-path}") String rootDir) {
       this.root = Paths.get(rootDir);
       init();
   }

    private void init() {
        try {
            if (!Files.exists(root)) {
                Files.createDirectories(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("저장 위치 초기화 불가능", e);
        }
    }

    @Override
    public UUID put(UUID id, byte[] bytes) {
        Path path = resolvePath(id);
        try{
            Files.createDirectories(path.getParent());
            Files.write(path, bytes);
            return id;
        } catch (IOException e) {
            throw new RuntimeException("파일 저장 실패",e);
        }
    }

    private Path resolvePath(UUID id) {
        return root.resolve(id.toString());
    }

    @Override
    public InputStream get(UUID id) {
        Path path = resolvePath(id);
        try{
            if (!Files.exists(path)) {
                throw new RuntimeException("파일 찾을 수 없음.");
            }
            return new FileInputStream(path.toFile());
        } catch (IOException e) {
            throw new RuntimeException("파일 읽어오기 실패",e);
        }
    }

    @Override
    public ResponseEntity<Resource> download(BinaryContentDto binaryContentDto) {
        try{
            InputStream inputStream = get (binaryContentDto.id());
            InputStreamResource resource = new InputStreamResource(inputStream);

            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + binaryContentDto.fileName());

            MediaType mediaType;
            try{
                mediaType = MediaType.parseMediaType(binaryContentDto.contentType());
            } catch (Exception e){
                mediaType = MediaType.APPLICATION_OCTET_STREAM;
            }

            return ResponseEntity.ok()
                    .headers(headers)
                    .contentType(mediaType)
                    .contentLength(binaryContentDto.size())
                    .body((Resource) resource);
        } catch (Exception e){
            throw new RuntimeException("다운로드 실패", e);
        }
    }
}
