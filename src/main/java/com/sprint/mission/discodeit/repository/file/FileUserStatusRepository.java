package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.userStatus.UserStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
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
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";
    //
    private Map<UUID, UserStatus> userStatusData;
    private final Path userStatusFilePath;

    public FileUserStatusRepository(@Value("${discodeit.repository.file-directory:data}") String fileDirectory) {

        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), fileDirectory, UserStatus.class.getSimpleName());
        this.userStatusFilePath = DIRECTORY.resolve("userStatus" + EXTENSION);

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
        if (!Files.exists(userStatusFilePath)) {
            userStatusData = new HashMap<>();
            dataSave();
            return;
        }
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(userStatusFilePath.toFile()))) {
            userStatusData = (Map<UUID, UserStatus>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("파일을 불러올 수 없습니다.", e);
        }
    }

    public void dataSave() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(userStatusFilePath.toFile()))) {
            oos.writeObject(userStatusData);
        } catch (IOException e) {
            throw new RuntimeException("파일을 저장할 수 없습니다."+ e.getMessage(), e);
        }
    }

    public UserStatus save(UserStatus userStatus){
        this.userStatusData.put(userStatus.getId(), userStatus);
        dataSave();

        return userStatus;
    }

    public UserStatus update(UserStatusUpdateRequestDto dto){
        UserStatus userStatus = userStatusData.get(dto.getId());
        userStatus.update(dto.getNewActivatedAt());

        dataSave();
        return userStatus;
    }

    public List<UserStatus> findAll(){
        return this.userStatusData.values().stream().toList();
    }

    public UserStatus findById(UUID userStatusId){
        return Optional.ofNullable(userStatusData.get(userStatusId))
                .orElseThrow(() -> new NoSuchElementException("UserStatus with id " + userStatusId + " not found"));
    }

    public UserStatus findByUserId(UUID userId){
        return userStatusData.values().stream()
                .filter(status -> status.getUserId().equals(userId))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("UserStatus with userid " + userId + " not found"));
    }

    public void delete(UUID userStatusId){
        userStatusData.remove(userStatusId);
        dataSave();
    }
}
