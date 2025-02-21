package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

import java.util.Scanner;
import java.util.UUID;

public class JavaApplication_Switch {
    public static void main(String[] args) {
        JCFUserService jcfUserService = new JCFUserService();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                System.out.println("\n***** 사용자 조회 시스템 *****");
                System.out.println("1. 사용자 등록");
                System.out.println("2. 사용자 조회");
                System.out.println("3. 모든 사용자 조회");
                System.out.println("4. 사용자 수정");
                System.out.println("5. 사용자 삭제");
                System.out.println("0. 종료하기");
                System.out.print("번호를 선택하세요: ");

                int choice = Integer.parseInt(scanner.nextLine());

                switch (choice) {
                    case 1 -> {
                        System.out.print("사용자 이름 입력: ");
                        String username = scanner.nextLine().trim(); //.trim()을 사용하면 공백을 제거
                        if (username.isEmpty()) {
                            System.out.println("이름을 입력해야 합니다.");
                            continue;
                        }
                        User newUser = new User(username);
                        jcfUserService.create(newUser);
                        System.out.println("등록 완료! 이름: " + newUser.getUserName() + " ID: " + newUser.getId());
                    }
                    case 2 -> {
                        System.out.print("조회할 사용자 ID 입력: ");
                        try {
                            UUID id = UUID.fromString(scanner.nextLine());
                            User check = jcfUserService.read(id).orElse(null);
                            System.out.println(check != null ? "사용자: " + check.getUserName() : "사용자를 찾을 수 없습니다.");
                        } catch (IllegalArgumentException e) {
                            System.out.println("유요한 UUID 형식을 입력하세요");
                        }
                    }
                    case 3 -> {
                        System.out.println("=== 전체 사용자 목록 ===");
                        jcfUserService.readAll().forEach(u -> System.out.println("이름: " + u.getUserName() + " ID: " + u.getId()));
                    }
                    case 4 -> {
                        System.out.print("수정할 사용자 ID 입력: ");
                        try {
                            UUID id = UUID.fromString(scanner.nextLine());
                            if (jcfUserService.read(id).isPresent()) {
                                System.out.print("새로운 사용자 이름 입력: ");
                                String newName = scanner.nextLine().trim();
                                if (newName.isEmpty()) {
                                    System.out.println("이름을 입력해야 합니다.");
                                    continue;
                                }
                                User updateUser = new User(newName);
                                jcfUserService.update(id, updateUser);
                                System.out.println("사용자 정보가 업데이트되었습니다.");
                            } else {
                                System.out.println("사용자를 찾을 수 없습니다.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("유효한 UUID 형식을 입력하세요.");
                        }
                    }
                    case 5 -> {
                        System.out.print("삭제할 사용자 ID 입력: ");
                        try {
                            UUID id = UUID.fromString(scanner.nextLine());
                            if (jcfUserService.read(id).isPresent()) {
                                jcfUserService.delete(id);
                                System.out.println("삭제 완료!");
                            } else {
                                System.out.println("해당 ID의 사용자를 찾을 수 없습니다.");
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println("유효한 UUID 형식을 입력하세요.");
                        }
                    }
                    case 0 -> {
                        System.out.println("프로그램 종료");
                        break;
                    }
                    default -> System.out.println("잘못된 입력입니다. 0~5 사이의 숫자를 입력하세요.");
                }
            } catch (NumberFormatException e) {
                System.out.println("숫자를 입력하세요.");
            }
        }
    }
}