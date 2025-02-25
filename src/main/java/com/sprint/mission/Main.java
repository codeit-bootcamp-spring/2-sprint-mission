package com.sprint.mission;

import com.sprint.mission.repository.UserRepository;
import com.sprint.mission.service.MessageService;
import com.sprint.mission.service.UserService;
import com.sprint.mission.service.UserServiceImpl;
import com.sprint.mission.view.input.MessageInput;
import com.sprint.mission.view.input.UserInput;
import com.sprint.mission.view.output.MessageOutput;
import com.sprint.mission.view.output.UserOutput;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Hello world!");


        UserRepository userRepository = new UserRepository();
        MessageOutput messageOutput = new MessageOutput();
        MessageService messageService = new MessageService(userRepository, messageOutput);
        MessageInput messageInput = new MessageInput(messageService);
        UserOutput userOutput = new UserOutput();
        UserService userService = new UserServiceImpl(userRepository, userOutput);
        UserInput userInput = new UserInput(userService);

        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n원하는 기능을 선택하세요:");
            System.out.println("1. 회원가입");
            System.out.println("2. 이메일로 회원 조회");
            System.out.println("3. 회원 전체 조회");
            System.out.println("4. 회원 정보 수정");
            System.out.println("5. 채팅하기");
            System.out.println("0. 프로그램 종료");
            System.out.print("> ");

            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    userInput.creatInput(); // 회원가입 실행
                    break;
                case "2":
                    userInput.getEmailInput(); // 이메일로 조회
                    break;
                case "3":
                    userInput.getAllInput(); // 전체 회원 조회 실행
                    break;
                case "4":
                    userInput.updateInput(); // 회원 정보 수정 실행
                    break;
                case "5":
                    messageInput.checkUser();  // 메신저 보내기
                case "0":
                    System.out.println("프로그램을 종료합니다.");
                    System.exit(0);
                default:
                    System.out.println("올바른 선택이 아닙니다. 다시 입력해주세요.");
            }
        }
    }

}