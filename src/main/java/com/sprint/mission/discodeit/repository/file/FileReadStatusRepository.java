package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import java.io.File;
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
public class FileReadStatusRepository implements ReadStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    //
    private Map<UUID, ReadStatus> readStatusData;
    private final Path readStatusFilePath;

    public FileReadStatusRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, ReadStatus.class.getSimpleName());
        this.readStatusFilePath = DIRECTORY.resolve("readStatus" + EXTENSION);

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
        if (!Files.exists(readStatusFilePath)) {
            readStatusData = new HashMap<>();
            dataSave();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(readStatusFilePath.toFile()))) {
            readStatusData = (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일을 불러올 수 없습니다.", e);
        }
    }

    public void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(readStatusFilePath.toFile()))) {
            oos.writeObject(readStatusData);
        } catch (IOException e) {
            throw new RuntimeException("파일을 저장할 수 없습니다."+ e.getMessage(), e);
        }
    }

    public ReadStatus save(ReadStatus readStatus){
        this.readStatusData.put(readStatus.getId(), readStatus);
        dataSave();

        return readStatus;
    }

    public ReadStatus findById(UUID id){
        return Optional.ofNullable(readStatusData.get(id))
                .orElseThrow(() -> new NoSuchElementException("ReadStatus with id " + id + " not found"));
    }

    public List<ReadStatus> findAll(){
        return this.readStatusData.values().stream().toList();
    }

    public ReadStatus update(ReadStatusUpdateRequestDto dto){
        ReadStatus readStatus = readStatusData.get(dto.getReadStatusId());
        readStatus.update(dto.getNewLastReadAt());

        dataSave();
        return readStatus;
    }

    public void delete(UUID readStatusID){
        readStatusData.remove(readStatusID);
        dataSave();
    }
}
