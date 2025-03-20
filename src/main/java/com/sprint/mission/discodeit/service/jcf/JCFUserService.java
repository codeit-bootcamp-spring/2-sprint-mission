package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.dto.FindUserDto;
import com.sprint.mission.discodeit.dto.SaveUserParamDto;
import com.sprint.mission.discodeit.dto.UpdateUserParamDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> userList = new ArrayList<>();

    @Override
    public void save(SaveUserParamDto saveUserParamDto) {
        User user = new User(
                saveUserParamDto.username(), saveUserParamDto.password(),
                saveUserParamDto.nickname(), saveUserParamDto.email(),
                saveUserParamDto.profileUUID());
        userList.add(user);
        System.out.println("유저 생성 완료" + user);
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
    public void update(UpdateUserParamDto updateUserDto) {
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
