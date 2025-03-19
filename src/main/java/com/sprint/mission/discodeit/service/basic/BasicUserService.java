package com.sprint.mission.discodeit.service.basic;

import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND;
import static com.sprint.mission.discodeit.constant.ErrorMessages.ERROR_USER_NOT_FOUND_BY_EMAIL;
import static com.sprint.mission.discodeit.constant.FilePath.IMAGE_STORAGE_DIRECTORY;
import static com.sprint.mission.discodeit.constant.FilePath.JPG_EXTENSION;
import static com.sprint.mission.util.FileUtils.init;

import com.sprint.mission.discodeit.application.UserDto;
import com.sprint.mission.discodeit.application.UserRegisterDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {
    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public UserDto register(UserRegisterDto userRegisterDto) {
        validateDuplicateEmail(userRegisterDto.email());
        UUID profileId = processProfileImage(userRegisterDto);

        User requestUser = new User(
                userRegisterDto.name(),
                userRegisterDto.email(),
                userRegisterDto.password(),
                profileId);

        User savedUser = userRepository.save(requestUser);

        return UserDto.fromEntity(savedUser);
    }

    @Override
    public UserDto findById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND.getMessageContent()));

        return UserDto.fromEntity(user);
    }

    @Override
    public List<UserDto> findByName(String name) {
        return userRepository.findByName(name)
                .stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    @Override
    public List<UserDto> findAll() {
        return userRepository.findAll()
                .stream()
                .map(UserDto::fromEntity)
                .toList();
    }

    @Override
    public UserDto findByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException(ERROR_USER_NOT_FOUND_BY_EMAIL.getMessageContent()));

        return UserDto.fromEntity(user);
    }

    @Override
    public List<UserDto> findAllByIds(List<UUID> userIds) {
        return userIds
                .stream()
                .map(this::findById)
                .toList();
    }

    @Override
    public void updateName(UUID id, String name) {
        userRepository.updateName(id, name);
    }

    @Override
    public void delete(UUID id) {
        userRepository.delete(id);
    }

    private void validateDuplicateEmail(String requestEmail) {
        boolean isDuplicate = userRepository.findAll()
                .stream()
                .anyMatch(existingUser -> existingUser.isSameEmail(requestEmail));

        if (isDuplicate) {
            throw new IllegalArgumentException("이미 존재하는 유저입니다");
        }
    }

    private void saveImageFile(UserRegisterDto userRegisterDto, Path directoryPath, Path imageFile) {
        try {
            init(directoryPath);
            Files.write(imageFile, userRegisterDto.multipartFile().getBytes());
        } catch (IOException e) {
            throw new UncheckedIOException("프로필이미지 파일을 저장할 수 없습니다.", e);
        }
    }

    private UUID processProfileImage(UserRegisterDto userRegisterDto) {
        if (userRegisterDto.multipartFile() == null) {
            return null;
        }

        Path imageFile = IMAGE_STORAGE_DIRECTORY.resolve(UUID.randomUUID() + JPG_EXTENSION);
        saveImageFile(userRegisterDto, IMAGE_STORAGE_DIRECTORY, imageFile);

        BinaryContent binaryContent = binaryContentRepository.save(new BinaryContent(imageFile));
        return binaryContent.getProfileId();
    }
}
