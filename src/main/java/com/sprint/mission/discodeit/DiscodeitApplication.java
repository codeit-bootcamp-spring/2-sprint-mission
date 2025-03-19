package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.auth.AuthLoginRequestDto;
import com.sprint.mission.discodeit.dto.auth.AuthLoginResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.BinaryContentCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.user.UserCreateResponseDto;
import com.sprint.mission.discodeit.dto.user.UserUpdateRequestDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.NoSuchElementException;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

		UserService userService = context.getBean(UserService.class);
		BinaryContentService binaryContentService = context.getBean(BinaryContentService.class);
		AuthService authService = context.getBean(AuthService.class);
		UserStatusService userStatusService = context.getBean(UserStatusService.class);

		List<BinaryContent> profiles = setUpProfiles(binaryContentService);
		List<UserCreateResponseDto> userDtos = setupUsers(userService, profiles);
		printUser1(userService, userDtos.get(0));
		printUsers(userService, userDtos);
		deleteUser(userService, userDtos, binaryContentService, profiles);
		printUsers(userService, userDtos);
		updateUser1(userService, userDtos.get(0));
		printUser1(userService, userDtos.get(0));

		System.out.println();

		testSuccessfulLogin(authService, userStatusService, userDtos.get(0));
		testFailedLogin(authService);
	}

	private static void testSuccessfulLogin(AuthService authService, UserStatusService userStatusService, UserCreateResponseDto user1Dto) {
		System.out.println("===================== 유저 1 로그인 - 성공 케이스 =====================");
		System.out.println("첫 로그인 이전 유저 1의 lastLoginAt: " + userStatusService.findByUserId(user1Dto.id()).getLastLoginAt());
		System.out.println("===================== 유저 1 로그인 시도 =====================");
		AuthLoginResponseDto authResponse = authService.login(new AuthLoginRequestDto("user1", "password1"));
		System.out.println("로그인 성공: " + authResponse);
		System.out.println("로그인 이후 유저 1의 lastLoginAt: " + userStatusService.findByUserId(authResponse.id()).getLastLoginAt());
	}

	private static void testFailedLogin(AuthService authService) {
		System.out.println("===================== 유저 1 로그인 - 실패 케이스 =====================");
		try {
			authService.login(new AuthLoginRequestDto("user1", "wrongpassword"));
		} catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
		}
	}
	private static void updateUser1(UserService userService, UserCreateResponseDto user1Dto) {
		userService.update(new UserUpdateRequestDto(user1Dto.id(), "user1", user1Dto.email(), "password1", user1Dto.profileId()));
		System.out.println("===================== 유저 1 닉넴 영어로 바꿈 =====================");

	}

	private static void printUser1(UserService userService, UserCreateResponseDto user1Dto) {
		System.out.println("===================== 유저 1 find로 출력 =====================");
		System.out.println(userService.find(user1Dto.id()));
	}

	private static List<BinaryContent> setUpProfiles(BinaryContentService binaryContentService) {
		BinaryContent profile1 = binaryContentService.create(new BinaryContentCreateRequestDto("유저1 사진"));
		BinaryContent profile2 = binaryContentService.create(new BinaryContentCreateRequestDto("유저2 사진"));
		BinaryContent profile3 = binaryContentService.create(new BinaryContentCreateRequestDto("유저3 사진"));
		return List.of(profile1, profile2, profile3);
	}

	private static List<UserCreateResponseDto> setupUsers(UserService userService, List<BinaryContent> profiles) {
		UserCreateResponseDto user1Dto = userService.create(new UserCreateRequestDto("유저1", "user1@example.com", "password1", profiles.get(0).getId()));
		UserCreateResponseDto user2Dto = userService.create(new UserCreateRequestDto("유저2", "user2@example.com", "password2", profiles.get(1).getId()));
		UserCreateResponseDto user3Dto = userService.create(new UserCreateRequestDto("유저3", "user3@example.com", "password3", profiles.get(2).getId()));
		UserCreateResponseDto user4Dto = userService.create(new UserCreateRequestDto("유저4", "user4@example.com", "password4", null));

		System.out.println("===================== 유저 4명 생성 =====================");
		return List.of(user1Dto, user2Dto, user3Dto, user4Dto);
	}

	private static void printUsers(UserService userService, List<UserCreateResponseDto> userDtos) {
		System.out.println("===================== 유저 findAll로 출력 =====================");
		userService.findAll().forEach(System.out::println);
	}

	private static void deleteUser(UserService userService, List<UserCreateResponseDto> userDtos, BinaryContentService binaryContentService, List<BinaryContent> profiles) {
		userService.delete(userDtos.get(3).id());
		System.out.println("===================== 유저4 삭제 =====================");
		userService.delete(userDtos.get(2).id());
		System.out.println("===================== 유저3 삭제 =====================");
		System.out.println("===================== 유저3 사진 없는지 확인 =====================");
        try {
            binaryContentService.find(profiles.get(2).getId());
        } catch (NoSuchElementException e) {
			System.out.println(e.getMessage());
        }
		System.out.println("===================== 유저 2명 남음 =====================");

    }
}
