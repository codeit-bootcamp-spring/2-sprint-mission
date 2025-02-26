package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.UserService;

import java.util.*;

public class JCFUserService implements UserService {

    // 맵 객체가 더 나은가? - List 검색 시 처음부터 끝까지 순회 -> 데이터가 많아지면 검색 효율이 떨어짐
    // 반면에 Map은 key값으로 바로바로 정보를 꺼내올 수 있음
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
            throw new IllegalArgumentException("생성하려는 유저 정보가 입력되지 않았습니다.");
        }
        // 필수 입력 항목이 하나라도 빠지거나 공백이면 Exception 발생
        // 너무 길어지지만, isBlank는 null 체크를 못하고, == null은 공백문자를 체크하지 못함
        if (user.getUsername() == null || user.getUsername().isBlank()|| user.getPassword() == null || user.getPassword().isBlank() || user.getNickname() == null || user.getNickname().isBlank()) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다. (username, password, nickname)");
        }
        // 중복 유저 방지, username을 기준으로 회원을 찾거나 수정하므로 중복이 생기면 안된다.
        if (data.stream().
                anyMatch(u -> u.getUsername().equals(user.getUsername()))) {
            // 처음엔 data.stream().filter(u -> u.getUsername().equals(user.getUsername()).findFirst().isPresent()로 작성하였으나,
            // anyMatch를 사용하는게 더 간편해 보여 사용
            throw new IllegalStateException("중복된 username 입니다."); // 입력값 자체는 괜찮으나, 시스템에서 중복 유저를 허용하지 않으므로 IllegalStateException 사용
        }
        data.add(user);
    }

    // 유저 하나 읽기
    @Override
    public User getUser(String username) {
        if(username == null) {
            throw new IllegalArgumentException("username이 입력되지 않았습니다.");
        }

        User findUser = data.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElseThrow(() -> new NoSuchElementException("해당 username을 가진 유저는 존재하지 않습니다."));

        return findUser;
    }

    // 유저 전체 읽기
    @Override
    public List<User> getAllUsers() {
        // 유저가 없는 경우엔 빈 List를 반환하는게 좋을지, 예외를 던져주는게 좋을지 고민
        // 일단 검색 결과가 없다는걸 띄워야하므로 빈 List 반환
        return data;
    }

    // 유저 수정
    @Override
    public void updateUser(User user) { // 매개변수로 도대체 뭘 받아야 효율적일지 모르겟다. 필드별로 받기 vs User 객체로 받기
        if (user == null) {
            throw new IllegalArgumentException("수정 정보가 입력되지 않았습니다.");
        }

        // 필수 입력 항목이 하나라도 빠지면 Exception 발생 (username은 아이디이므로 수정X)
        if (user.getPassword().isEmpty() || user.getNickname().isEmpty()) {
            throw new IllegalArgumentException("필수 입력 항목을 빠뜨리셨습니다.");
        }

        User findUser = getUser(user.getUsername()); // getUser 메소드로 반환되는 User 객체는 data 리스트에 이미 존재하는 객체의 참조 -> findUser를 수정하면 data의 해당 객체도 수정됨

        findUser.updateUpdatedAt(System.currentTimeMillis());
        findUser.updateNickname(user.getNickname());
        findUser.updatePassword(user.getPassword());
    }

    // 권한 추가
    @Override
    public void addRole(String role, String username) {
        if (role == null) {
            throw new IllegalArgumentException("추가할 권한이 입력되지 않았습니다.");
        }
        if (username == null) {
            throw new IllegalArgumentException("username이 입력되지 않았습니다.");
        }

        User findUser = getUser(username);
        findUser.addRole(role);
    }


    // 권한 삭제
    @Override
    public void removeRole(String role, String username) {
        if (role == null) { // 입력값 자체에 대한 예외
            throw new IllegalArgumentException("삭제할 권한이 입력되지 않았습니다.");
        }
        if (username == null) {
            throw new IllegalArgumentException("username이 입력되지 않았습니다.");
        }

        User findUser = getUser(username);
        // 해당 role이 있는지 먼저 확인
        if (!findUser.getRoles().stream().anyMatch(r -> r.equals(role))) {
            throw new NoSuchElementException("해당 유저에게 해당 권한은 존재하지 않습니다."); // role 자체는 유효한 값이지만, 검색 결과 role이 존재하지 않으므로 NoSuchElementException
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
