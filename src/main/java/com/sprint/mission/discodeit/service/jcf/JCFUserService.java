package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.UpdateUserDto;
import com.sprint.mission.discodeit.dto.UserSaveDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    private final List<User> userList = new ArrayList<>();

    @Override
    public UserSaveDto save(String username, String password, String nickname, String email, byte[] profile) {
        User user = new User(username, password, nickname, email, UUID.randomUUID());
        userList.add(user);
        System.out.println("유저 생성 완료" + user);
        return new UserSaveDto(user.getId(), user.getNickname(), user.getProfile(), user.getCreatedAt());
    }

    @Override
    public FindUserDto findByUser(UUID uuid) {
        //for (User u : userList) {
        //    if (u.getId().equals(uuid)) {
        //        return u;
        //    }
        //}
        //System.out.print("[실패]일치하는 회원이 없습니다.  ");
        return null;
    }

    @Override
    public List<FindUserDto> findAllUser() {
        if (userList.isEmpty()) {
            System.out.println("아이디가 존재하지 않습니다");
        }
        return Collections.EMPTY_LIST;
    }

    @Override
    public void update(UpdateUserDto updateUserDto) {
        if (userList.stream().noneMatch(data -> data.getId().equals(updateUserDto.userUUID()))) {
            System.out.println("[실패]수정하려는 아이디가 존재하지 않습니다.");
            return;
        }

        for (User u : userList) {
            if (u.getId().equals(updateUserDto.userUUID())) {
                u.updateNickname(updateUserDto.nickname());
                System.out.println("[성공]사용자 변경 완료[사용자 아이디: " + u.getId() +
                        ", 닉네임: " + u.getNickname() +
                        ", 변경 시간: " + u.getUpdatedAt() +
                        "]");
            }
        }
    }

    @Override
    public void delete(UUID uuid) {
        boolean removed = userList.removeIf(u -> u.getId().equals(uuid));

        if (!removed) {
            System.out.println("[실패]존재하지 않는 사용자");
        } else {
            System.out.println("[성공]사용자 삭제 완료");
        }
    }
}
