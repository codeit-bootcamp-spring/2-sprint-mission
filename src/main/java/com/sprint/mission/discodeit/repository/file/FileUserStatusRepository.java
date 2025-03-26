package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.domain.UserStatus;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
@ConditionalOnProperty(name = "discodeit.repository.type", havingValue = "file", matchIfMissing = false)
public class FileUserStatusRepository implements UserStatusRepository {
    private final Path DIRECTORY;
    private final String EXTENSION = ".ser";

    public FileUserStatusRepository() {
        this.DIRECTORY = Paths.get(System.getProperty("user.dir"), "file-data-map", UserStatus.class.getSimpleName());
        if(Files.notExists(DIRECTORY)){
            try{
                Files.createDirectories(DIRECTORY);
            }catch (IOException e){
                throw new RuntimeException(e);
            }
        }
    }

    private Path resolvePath(UUID id){
        return DIRECTORY.resolve(id + EXTENSION);
    }

    @Override
    public UserStatus save(UserStatus userStatus) {
        Path path = resolvePath(userStatus.getId());
        try(
                FileOutputStream fos = new FileOutputStream(path.toFile());
                ObjectOutputStream oos = new ObjectOutputStream(fos)
        ){
            oos.writeObject(userStatus);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return userStatus;
    }

    @Override
    public Optional<UserStatus> findById(UUID id) {
        UserStatus userStatusNullable = null;
        Path path = resolvePath(id);
        if(Files.notExists(path)){
            try(
                    FileInputStream fis = new FileInputStream(path.toFile());
                    ObjectInputStream ois = new ObjectInputStream(fis)
            ){
                userStatusNullable = (UserStatus) ois.readObject();
            } catch (ClassNotFoundException | IOException e) {
                throw new RuntimeException(e);
            }
        }
        return Optional.ofNullable(userStatusNullable);
    }

    @Override
    public Optional<UserStatus> findByUserId(UUID userId) {
        return findAll().stream()
                .filter(UserStatus -> UserStatus.getUserId().equals(userId))
                .findFirst();
    }

    @Override
    public List<UserStatus> findAll() {
        try{
            return Files.list(DIRECTORY)
                    .filter(path -> path.toString().endsWith(EXTENSION))
                    .map(path -> {
                        try(
                                FileInputStream fis = new FileInputStream(path.toFile());
                                ObjectInputStream ois = new ObjectInputStream(fis);
                        ) {
                            return (UserStatus) ois.readObject();
                        }catch(IOException | ClassNotFoundException e){
                            throw new RuntimeException(e);
                        }
                    })
                    .toList();
        } catch(IOException e){
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UUID id) {
        Path path = resolvePath(id);
        try{
            Files.delete(path);
        }catch(IOException e){
            throw new RuntimeException(e);
        }
    }
}
