package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.dto.binarycontent.CreateBinaryContentRequest;
import com.sprint.mission.discodeit.dto.user.CreateUserRequest;
import com.sprint.mission.discodeit.dto.user.UpdateUserRequest;
import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.status.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FileUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public UUID create(CreateUserRequest request) {
        String username = request.getName();
        String email = request.getEmail();

        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("[ERROR] user name already exist");
        }
        if (userRepository.existByEmail(email)) {
            throw new IllegalArgumentException("[ERROR] email already exist");
        }

        User user = new User(request.getName(), request.getEmail(), request.getPassword());
        userRepository.save(user);

        UserStatus userStatus = new UserStatus(user.getId());
        userStatusRepository.save(userStatus);

        return user.getId();
    }

    @Override
    public UUID create(CreateUserRequest request, CreateBinaryContentRequest binaryContentRequest) {
        UUID uuid = create(request);
        User user = userRepository.findByUserId(uuid);

        String fileName = binaryContentRequest.getName();
        String contentType = binaryContentRequest.getContentType();
        byte[] bytes = binaryContentRequest.getBytes();
        int size = bytes.length;

        BinaryContent binaryContent = new BinaryContent(fileName, size, contentType, bytes);
        binaryContentRepository.save(binaryContent);

        user.updateProfileImageId(binaryContent.getId());

        return user.getId();
    }

    @Override
    public UserResponseDto findByUserId(UUID userId) {
        User user = userRepository.findByUserId(userId);
        boolean isOline = userStatusRepository.findByUserId(userId).isOnline();

        return UserResponseDto.from(user, isOline);
    }

    @Override
    public List<UserResponseDto> findAll() {
        return userRepository.findAll().stream()
            .map(user -> UserResponseDto.from(user,
                userStatusRepository.findByUserId(user.getId()).isOnline())).toList();
    }

    @Override
    public UUID update(UUID userId, UpdateUserRequest request) {
        User user = userRepository.findByUserId(userId);

        String username = request.getName();
        String email = request.getEmail();
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("[ERROR] already exist");
        }
        if (userRepository.existByEmail(email)) {
            throw new IllegalArgumentException("[ERROR] already exist");
        }

        String password = request.getPassword();

        user.updateName(username);
        user.updateEmail(email);
        user.updatePassword(password);

        userRepository.save(user);

        return user.getId();
    }

    @Override
    public UUID update(UUID userId, UpdateUserRequest request,
        CreateBinaryContentRequest binaryContentRequest) {

        UUID findUser = update(userId, request);

        User user = userRepository.findByUserId(findUser);
        binaryContentRepository.delete(user.getProfileImageId());

        String fileName = binaryContentRequest.getName();
        String contentType = binaryContentRequest.getContentType();
        byte[] bytes = binaryContentRequest.getBytes();
        int length = bytes.length;

        BinaryContent binaryContent = new BinaryContent(fileName,
            length,
            contentType,
            bytes);

        user.updateProfileImageId(binaryContent.getId());

        return user.getId();
    }

    @Override
    public void remove(UUID userId) {
        User user = userRepository.findByUserId(userId);

        userRepository.delete(userId);
        userStatusRepository.deleteByUserId(userId);
        binaryContentRepository.delete(user.getProfileImageId());
    }
}
