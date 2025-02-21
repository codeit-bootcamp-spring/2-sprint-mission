package com.sprint.mission.discodeit.main;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.DuplicatedUserException;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {
    public static void main(String[] args) {
        // User Service 테스트
        UserService userService = JCFUserService.getInstance();

        // 유저 등록 테스트
        System.out.println("=========== 유저 생성 및 유저 리스트 조회 테스트 ===========");
        System.out.println("=========== 예상 결과: 유저 7명 나옴 ===========");
        User user1 = userService.createUser("Han", "Han@gmail.com", "", "hello I'm sam");
        User user2 = userService.createUser("Kim", "Kim@gmail.com", "dog pic", "I like dogs");
        User user3 = userService.createUser("Nick", "Nick@ggg.io", "cat pic", "I love cats");
        User user4 = userService.createUser("Jack", "Jack@harlow.co", "", "");
        User user5 = userService.createUser("Jamie", "Jamie@naver.com", "korean flag", "I am Korean");
        User user6 = userService.createUser("Mr.delete", "delete@naver.com", "XXXXX", "I am gonna be deleted soon");
        User user7 = userService.createUser("Oreo", "delete2@naver.com", "", "");

        try {
            User user8 = userService.createUser("나오지 마", "Jamie@naver.com", "korean", "I am Korean"); // 이메일 중복이므로 생성 안 하고 null 반환
        } catch (DuplicatedUserException e) {
            System.out.println(e.getMessage());
        }

        userService.getUsers().stream().forEach(System.out::println);
        System.out.println("=========== 유저 생성 및 전체 조회 테스트 끝 ===========");
        System.out.println();

        // 유저 읽기
        System.out.println("=========== 특정 유저 get 테스트 ===========");
        System.out.println("=========== 이메일로 조회: 'Kim' ===========");
        System.out.println(userService.getUserByEmail(user2.getEmail()));
        System.out.println("=========== id로 조회: 'Han' ===========");
        System.out.println(userService.getUserById(user1.getId()));
        System.out.println("=========== 특정 유저 get 테스트 끝 ===========");
        System.out.println();

        // 유저 업데이트
        System.out.println("=========== 유저 update 테스트 ===========");
        System.out.println("=========== 'Nick 유저 업데이트' ===========");
        System.out.println("=========== 'Nick 수정 이전' ===========");
        System.out.println(userService.getUserById(user3.getId()));
        System.out.println("=========== 'Nick 의 닉네임을 Nice로 변경, 상태 메세지 빈칸 만들기 ' ===========");
        userService.updateUser(user3.getId(), "Nice", null, "");
        System.out.println(userService.getUserById(user3.getId()));
        System.out.println("=========== 유저 update 테스트 끝 ===========");

        // 유저 삭제
        System.out.println("=========== 유저 삭제 테스트 ===========");
        System.out.println("=========== 삭제 이전: 7명 ===========");
        userService.getUsers().stream().forEach(System.out::println);
        System.out.println("=========== 유저 'Oreo'를 id로 삭제 ===========");
        System.out.println("'Oreo' 삭제 성공 여부: " + userService.deleteUserById(user7.getId()));
        System.out.println("=========== 유저 'Mr.delete'를 이메일로 삭제 ===========");
        System.out.println("'Mr.delete' 삭제 성공 여부: " + userService.deleteUserByEmail(user6.getEmail()));
        System.out.println("=========== 삭제 이후: 5명이여야 함 ===========");
        userService.getUsers().stream().forEach(System.out::println);

    }
}
