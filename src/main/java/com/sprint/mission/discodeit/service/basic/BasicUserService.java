package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.dto.data.UserDto;
import com.sprint.mission.discodeit.dto.request.RoleUpdateRequest;
import com.sprint.mission.discodeit.dto.request.UserCreateRequest;
import com.sprint.mission.discodeit.dto.request.UserUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.file.FileException;
import com.sprint.mission.discodeit.exception.file.FileProcessingCustomException;
import com.sprint.mission.discodeit.exception.user.UserAlreadyExistException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserOperationRestrictedException;
import com.sprint.mission.discodeit.mapper.BinaryContentMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.SessionOnlineService;
import com.sprint.mission.discodeit.service.UserService;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;


@RequiredArgsConstructor
@Service
public class BasicUserService implements UserService {

    private static final Logger log = LoggerFactory.getLogger(BasicUserService.class);

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final BinaryContentMapper binaryContentMapper;
    private final BinaryContentRepository binaryContentRepository;
    private final MessageRepository messageRepository;
    private final BinaryContentService binaryContentService;
    private final PasswordEncoder passwordEncoder;
    private final SessionOnlineService sessionOnlineService;

    @Transactional
    @Override
    public UserDto create(UserCreateRequest userCreateRequest, MultipartFile profileImageFile) {
        String username = userCreateRequest.username();
        String email = userCreateRequest.email();

        validateUserDoesNotExist(username, email);

        String encodedPassword = passwordEncoder.encode(userCreateRequest.password());

        // 프로필 이미지가 있으면 먼저 처리
        BinaryContent profile = null;
        if (profileImageFile != null && !profileImageFile.isEmpty()) {
            try {
                profile = processProfileImage(profileImageFile);
            } catch (Exception e) {
                log.error("프로필 이미지 처리 중 오류 발생, User는 프로필 없이 생성됨", e);
                profile = null;
            }
        }

        // User를 프로필과 함께 생성/저장
        User user = User.builder().username(username).email(email).password(encodedPassword)
            .profile(profile).role(Role.ROLE_USER).build();

        User savedUser = userRepository.save(user);

        boolean isOnline = isUserOnline(savedUser);
        UserDto result = userMapper.toDto(savedUser, isOnline);

        return result;
    }

    private BinaryContent processProfileImage(MultipartFile profileImageFile) {
        if (profileImageFile == null || profileImageFile.isEmpty()) {
            log.warn("프로필 이미지 파일이 null이거나 비어있음");
            return null;
        }

        BinaryContentDto profileDto = null;
        try {
            profileDto = binaryContentService.create(profileImageFile);
        } catch (Exception e) {
            log.error("BinaryContentService.create 실패", e);
            return null;
        }

        if (profileDto == null || profileDto.id() == null) {
            log.error("BinaryContentDto 생성 실패");
            return null;
        }

        // 데이터베이스에서 저장된 Entity를 조회
        BinaryContent result = null;
        try {
            result = binaryContentRepository.findById(profileDto.id()).orElse(null);
            if (result == null) {
                log.error("데이터베이스에서 BinaryContent를 찾을 수 없음 - ID: {}", profileDto.id());
            }
        } catch (Exception e) {
            log.error("데이터베이스 조회 중 오류 발생 - ID: {}", profileDto.id(), e);
            return null;
        }

        return result;
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    @Override
    public UserDto find(UUID userId) {
        log.info("🔍 사용자 조회 시작 - userId: {}", userId);

        User user = userRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        log.info("🔍 사용자 조회 완료 - username: {}", user.getUsername());
        log.info("🔍 프로필 상태 - profile null?: {}", user.getProfile() == null);
        if (user.getProfile() != null) {
            log.info("🔍 프로필 정보 - ID: {}, 파일명: {}, 크기: {}", user.getProfile().getId(),
                user.getProfile().getFileName(), user.getProfile().getSize());
        }

        boolean isOnline = isUserOnline(user);
        UserDto result = userMapper.toDto(user, isOnline);

        log.info("🔍 최종 UserDto - profile null?: {}", result.profile() == null);

        return result;
    }

    @PreAuthorize("hasRole('USER')")
    @Transactional(readOnly = true)
    @Override
    public List<UserDto> findAll() {
        log.info("사용자 목록 조회");

        List<User> users = userRepository.findAllWithProfile();

        if (users.isEmpty()) {
            return Collections.emptyList();
        }

        Set<String> onlineUsernames = sessionOnlineService.getAllOnlineUser();

        return users.stream().map(user -> {
            boolean isOnline = onlineUsernames.contains(user.getUsername());
            return userMapper.toDto(user, isOnline);
        }).collect(Collectors.toList());
    }


    @PreAuthorize("authentication.principal.user.id == #userId or hasRole('ADMIN')")
    @Transactional
    @Override
    public UserDto update(UUID userId, UserUpdateRequest userUpdateRequest,
        Optional<MultipartFile> profileRequest) {

        User user = userRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            log.warn("관리자 계정 수정 불가");
            throw new UserOperationRestrictedException();
        }

        String username = userUpdateRequest.username();
        String email = userUpdateRequest.email();

        if (username != null && !username.isEmpty() && !user.getUsername().equals(username)) {
            if (userRepository.existsByUsername(username)) {
                throw new UserAlreadyExistException();
            }
            user.setUsername(username);
            log.info("사용자 수정");
        }

        if (email != null && !email.isEmpty() && !user.getEmail().equals(email)) {
            if (userRepository.existsByEmail(email)) {
                throw new UserAlreadyExistException();
            }
            user.setEmail(email);
            log.info("사용자 수정");
        }

        if (userUpdateRequest.password() != null && !userUpdateRequest.password().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(userUpdateRequest.password());
            user.setPassword(encodedPassword);
            log.info("사용자 수정");
        }

        BinaryContent oldProfile = user.getProfile();

        if (profileRequest.isPresent()) {
            MultipartFile profileFile = profileRequest.get();

            try {
                BinaryContent newProfile = processProfileImage(profileFile);

                if (newProfile != null) {
                    if (oldProfile != null) {
                        log.info("기존 프로필 제거");
                    }
                    user.setProfile(newProfile);

                } else {
                    log.error(" 프로필 이미지 처리 실패");
                }
            } catch (FileProcessingCustomException e) {
                throw new FileProcessingCustomException();
            }
        }

        User updatedUser = userRepository.save(user);

        boolean isOnline = isUserOnline(updatedUser);

        return userMapper.toDto(updatedUser, isOnline);
    }


    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public UserDto updateRole(RoleUpdateRequest roleUpdateRequest) {
        UUID targetUserId = roleUpdateRequest.userId();
        Role newRole = roleUpdateRequest.newRole();

        User targetUser = userRepository.findByIdWithProfile(targetUserId)
            .orElseThrow(() -> new UserNotFoundException(targetUserId));

        if (Role.ROLE_ADMIN.equals(targetUser.getRole())) {
            throw new UserOperationRestrictedException();
        }

        if ("admin".equalsIgnoreCase(targetUser.getUsername())) {
            throw new UserOperationRestrictedException();
        }

        targetUser.setRole(newRole);
        User updatedUser = userRepository.save(targetUser);

        boolean isOnline = isUserOnline(updatedUser);
        return userMapper.toDto(updatedUser, isOnline);
    }

    @PreAuthorize("authentication.principal.user.id == #userId or hasRole('ADMIN')")
    @Transactional
    @Override
    public void delete(UUID userId) {

        User user = userRepository.findByIdWithProfile(userId)
            .orElseThrow(() -> new UserNotFoundException(userId));

        if ("admin".equalsIgnoreCase(user.getUsername())) {
            throw new UserOperationRestrictedException();
        }

        List<Message> messagesToUpdate = messageRepository.findByAuthorId(userId);
        if (!messagesToUpdate.isEmpty()) {
            for (Message message : messagesToUpdate) {
                message.setAuthor(null);
            }
            messageRepository.saveAll(messagesToUpdate);
        } else {
            log.info("작성한 메세지 존재 x");
        }

        BinaryContent userProfile = user.getProfile();
        if (userProfile != null) {
            try {
                binaryContentService.delete(userProfile.getId());
            } catch (FileException e) {
                throw new UserOperationRestrictedException();

            }
        }

        userRepository.delete(user);
        log.info("사용자 삭제 완료");
    }

    private void validateUserDoesNotExist(String username, String email) {
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException();
        }
        if (userRepository.existsByUsername(username)) {
            throw new UserAlreadyExistException();
        }
    }

    @Override
    public boolean isUserOnline(User user) {
        return sessionOnlineService.isUserOnline(user.getUsername());
    }
}