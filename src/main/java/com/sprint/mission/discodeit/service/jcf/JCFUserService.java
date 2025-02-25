package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JCFUserService implements UserService {

    private final List<User> data;

    // 생성자에서 data 필드 초기화
    // 요구사항엔 이렇게 적혀있는데, 항상 빈 배열로 초기화 할거면 왜 필드가 아니라 생성자에서 초기화하라고 했을까?
    private JCFUserService() {
        data = new ArrayList<>();
    } // private으로 객체 생성 방지 -> 싱글톤 패턴

    // 싱글톤 패턴 적용 - 지금은 javaApplication에서만 사용되는데 굳이 적용할 필요가 있을까? -> 나중을 위해 적용해놓자.
    // 아직까진 멀티 쓰레드 환경이 아니므로 게으른 초기화 사용
    private static UserService userService;

    public static UserService getInstance() {
        if(userService == null) {
            userService = new JCFUserService();
        }
        return userService;
    }

    // 유저 생성
    @Override
    public void createUser(User user) {
        if (user == null) {
            throw new IllegalArgumentException("생성하려는 유저 정보가 올바르지 않습니다.");
        }
        // 필수 입력 항목이 하나라도 빠지면 Exception 발생
        if (user.getUsername().isEmpty() || user.getPassword().isEmpty() || user.getNickname().isEmpty()) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }
        // 중복 유저 방지, username을 기준으로 회원을 찾거나 수정하므로 중복이 생기면 안된다.
        if (data.stream().
                anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            // 처음엔 data.stream().filter(u -> u.getUsername().equals(user.getUsername()).findFirst().isPresent()로 작성하였으나,
            // anyMatch를 사용하는게 더 간편해 보여 사용
            throw new IllegalArgumentException("중복된 유저이름 입니다.");
        }
        data.add(user);
    }

    // 유저 하나 읽기
    @Override
    public User getUser(String username) {
        User findUser = null;
        for (User user : data) {
            if (user.getUsername().equals(username)) {
                findUser = user;
                break;
            }
        }

        if (findUser == null) {
            throw new IllegalArgumentException("존재하지 않는 유저 입니다.");
        }

        // User findUser = data.stream().filter(u -> u.getId().equals(id)).findFirst().orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저 입니다.");
        // 뭐가 더 좋은 방식일까요?

        return findUser;
    }

    // 유저 전체 읽기
    @Override
    public List<User> getAllUsers() {
        // 유저가 없는 경우엔 빈 List를 반환하는게 좋을지, 예외를 던져주는게 좋을지 고민
        return data;
    }

    // 유저 수정
    @Override
    public void updateUser(User user) { // 매개변수로 도대체 뭘 받아야 효율적일지 모르겟다. 필드별로 받기 vs User 객체로 받기
        if (user == null) {
            throw new IllegalArgumentException("수정 정보가 존재하지 않습니다.");
        }

        // 필수 입력 항목이 하나라도 빠지면 Exception 발생 (username은 아이디이므로 수정X)
        if (user.getPassword().isEmpty() || user.getNickname().isEmpty()) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }

        User findUser = getUser(user.getUsername()); // getUser 메소드로 반환되는 User 객체는 data 리스트에 이미 존재하는 객체의 참조 -> findUser를 수정하면 data의 해당 객체도 수정됨

        // 존재하지 않는 회원 정보를 수정하려고 할 때
        if (findUser == null) {
            throw new IllegalArgumentException("존재하지 않는 회원입니다.");
        }

        findUser.updateUpdatedAt(System.currentTimeMillis());
        findUser.updateNickname(user.getNickname());
        findUser.updatePassword(user.getPassword());
    }

    // 권한 추가
    @Override
    public void addRole(String role, String username) {
        if (role == null) {
            throw new IllegalArgumentException("추가할 권한이 없습니다.");
        }
        if (username == null) {
            throw new IllegalArgumentException("username이 존재하지 않습니다.");
        }

        User findUser = getUser(username);
        findUser.addRole(role);
    }


    // 권한 삭제
    @Override
    public void removeRole(String role, String username) {
        if (role == null) {
            throw new IllegalArgumentException("삭제할 권한이 없습니다.");
        }
        if (username == null) {
            throw new IllegalArgumentException("username이 존재하지 않습니다.");
        }

        User findUser = getUser(username);
        // 해당 role이 있는지 먼저 확인
        if (!findUser.getRoles().stream().anyMatch(r -> r.equals(role))) {
            throw new IllegalArgumentException("해당 유저에게 해당 권한은 존재하지 않습니다.");
        }
        findUser.removeRole(role);
    }

    // 유저 삭제
    @Override
    public void deleteUser(String username) {
        User findUser = getUser(username);
        data.remove(findUser);
    }
}
