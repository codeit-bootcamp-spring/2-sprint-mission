package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file")
public class FileBinaryContentRepository implements BinaryContentRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    //
    private Map<UUID, BinaryContent> data;
    private final Path binaryContentFilePath;

    public FileBinaryContentRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {

        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, BinaryContent.class.getSimpleName());
        this.binaryContentFilePath = DIRECTORY.resolve("binaryContent" + EXTENSION);

        if (Files.notExists(DIRECTORY)) {
            try {
                Files.createDirectories(DIRECTORY);
            } catch (IOException e) {
                throw new RuntimeException("디렉토리 생성 실패: " + e.getMessage(), e);
            }
        }
        dataLoad();
    }

    public void dataLoad() {
        if (!Files.exists(binaryContentFilePath)) {
            data = new HashMap<>();
            dataSave();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryContentFilePath.toFile()))) {
            data = (Map<UUID, BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일을 불러올 수 없습니다.", e);
        }
    }

    public void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryContentFilePath.toFile()))) {
            oos.writeObject(data);
        } catch (IOException e) {
            throw new RuntimeException("파일을 저장할 수 없습니다."+ e.getMessage(), e);
        }
    }

    public BinaryContent save(BinaryContent binaryContent){
        this.data.put(binaryContent.getId(), binaryContent);
        dataSave();

        return binaryContent;
    }

    public BinaryContent findById(UUID binaryContentId){
        return Optional.ofNullable(data.get(binaryContentId))
                .orElseThrow(() -> new NoSuchElementException("BinaryContent with id " + binaryContentId + " not found"));
    }

    public List<BinaryContent> findAll(){
        return this.data.values().stream().toList();
    }

    public void delete(UUID binaryContentId){
        data.remove(binaryContentId);
        dataSave();
    }
}
