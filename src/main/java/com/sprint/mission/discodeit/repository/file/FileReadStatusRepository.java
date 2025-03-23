package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Primary
@Repository
public class FileReadStatusRepository implements ReadStatusRepository {
    private static final String FILE_PATH = "read_status.ser";

    @Override
    public void save(ReadStatus readStatus) {
        if (readStatus.getUser() == null || readStatus.getChannel() == null) {
            throw new IllegalArgumentException("User 또는 Channel이 비어 있습니다.");
        }
        List<ReadStatus> readStatuses = readFromFile();
        readStatuses.removeIf(rs -> rs.getUser().getId().equals(readStatus.getUser().getId()) &&
                rs.getChannel().getId().equals(readStatus.getChannel().getId()));
        readStatuses.add(readStatus);
        writeToFile(readStatuses);
    }

    @Override
    public Optional<ReadStatus> findById(UUID userId, UUID channelId) {
        return readFromFile().stream()
                .filter(rs -> rs.getUser().getId().equals(userId) &&
                        rs.getChannel().getId().equals(channelId))
                .findFirst();
    }

    @Override
    public List<ReadStatus> findAll() {
        return readFromFile();
    }

    @Override
    public List<ReadStatus> findAllByUserId(UUID userId) {
        return readFromFile().stream()
                .filter(rs -> rs.getUser().getId().equals(userId))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID userId, UUID channelId) {
        List<ReadStatus> readStatuses = readFromFile();
        boolean removed = readStatuses.removeIf(rs -> rs.getUser().getId().equals(userId) &&
                rs.getChannel().getId().equals(channelId));
        if (removed) {
            writeToFile(readStatuses);
        }
    }

    private void writeToFile(List<ReadStatus> readStatuses) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(readStatuses);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private List<ReadStatus> readFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<ReadStatus> readStatuses = (List<ReadStatus>) ois.readObject();

            return readStatuses.stream()
                    .filter(rs -> rs.getUser() != null && rs.getUser().getId() != null &&
                            rs.getChannel() != null && rs.getChannel().getId() != null)
                    .collect(Collectors.toList());

        } catch (IOException | ClassNotFoundException e) {
            return new ArrayList<>();
        }
    }
}