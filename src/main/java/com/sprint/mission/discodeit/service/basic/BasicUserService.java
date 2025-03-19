package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.dto.userdto.UserCreateDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserDeleteDto;
import com.sprint.mission.discodeit.service.dto.userdto.UserFindDto;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.dto.userdto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class BasicUserService implements UserService {

    private final UserRepository userRepository;
    private final BinaryContentRepository binaryContentRepository;
    private final UserStatusRepository userStatusRepository;

    @Override
    public User create(UserCreateDto userCreateDto) {
        List<User> userList = userRepository.load();
        Optional<User> validateName = userList.stream()
                .filter(u -> u.getName().equals(userCreateDto.name()))
                .findAny();
        Optional<User> validateEmail = userList.stream()
                .filter(u -> u.getEmail().equals(userCreateDto.email()))
                .findAny();
        if (validateName.isPresent()) {
            throw new IllegalArgumentException("User already exists.");
        }
        if (validateEmail.isPresent()) {
            throw new IllegalArgumentException("Email already exists.");
        }

        User user = new User(userCreateDto.name(), userCreateDto.email(), userCreateDto.password());
        User createdUser = userRepository.save(user);
        System.out.println(createdUser);
        return user;
    }


    @Override
    public User getUser(UserFindDto userFindDto) {
        Optional<User> user = userRepository.load().stream()
                .filter(u -> u.getId().equals(userFindDto.userId()))
                .findAny();
        return user.orElseThrow(() -> new NoSuchElementException("User does not exist."));
    }


    @Override
    public List<User> getAllUser() {
        List<User> userList = userRepository.load();
        if (userList.isEmpty()) {
            throw new NoSuchElementException("Profile not found.");
        }
        return userList;
    }

    @Override
    public User update(UserUpdateDto userUpdateDto) {
        Optional<User> matchingUser = userRepository.load().stream()
                .filter(u -> u.getId().equals(userUpdateDto.userId()))
                .findAny();
        if (matchingUser.isEmpty()) {
            throw new NoSuchElementException("User does not exist.");
        }
        User user = matchingUser.get();
        user.update(userUpdateDto.changeName(), userUpdateDto.changeEmail(), userUpdateDto.changePassword());
        userRepository.save(user);
        return user;
    }

    @Override
    public void delete(UserDeleteDto userDeleteDto) {
        Optional<User> matchingUser = userRepository.load().stream()
                .filter(u -> u.getId().equals(userDeleteDto.userId()))
                .findAny();
        if (matchingUser.isEmpty()) {
            throw new NoSuchElementException("User does not exist.");
        }
        User user = matchingUser.get();
        userRepository.remove(user);
    }
}
