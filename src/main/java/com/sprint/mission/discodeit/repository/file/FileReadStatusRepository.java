package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
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

public class FileReadStatusRepository implements ReadStatusRepository {

    private Map<UUID, ReadStatus> readStatusData;
    private static final String READ_STATUS_FILE_PATH = "readStatus.ser";

    public FileReadStatusRepository() {
        dataLoad();
    }

    public void dataLoad() {
        File file = new File(READ_STATUS_FILE_PATH);
        if (!file.exists()) {
            readStatusData = new HashMap<>();
            dataSave();
            return;
        }
        try (FileInputStream fis = new FileInputStream(READ_STATUS_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(fis)) {
            readStatusData = (Map<UUID, ReadStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("파일을 불러올 수 없습니다.");
        }
    }

    public void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(READ_STATUS_FILE_PATH))) {
            oos.writeObject(readStatusData);
        } catch (IOException e) {
            e.printStackTrace();
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
