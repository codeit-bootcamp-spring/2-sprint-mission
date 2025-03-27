package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

public class FileBinaryContentRepository implements BinaryContentRepository {
    private Map<UUID, BinaryContent> data;
    private static final String BINARY_CONTENT_FILE_PATH = "binaryContent.ser";

    public FileBinaryContentRepository() {
        dataLoad();
    }

    public void dataLoad() {
        File file = new File(BINARY_CONTENT_FILE_PATH);
        if (!file.exists()) {
            data = new HashMap<>();
            dataSave();
            return;
        }
        try (FileInputStream fis = new FileInputStream(BINARY_CONTENT_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            data = (Map<UUID, BinaryContent>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 불러올 수 없습니다.");
        }
    }

    public void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(BINARY_CONTENT_FILE_PATH))) {
            oos.writeObject(data);
        } catch (IOException e) {
            e.printStackTrace();
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
