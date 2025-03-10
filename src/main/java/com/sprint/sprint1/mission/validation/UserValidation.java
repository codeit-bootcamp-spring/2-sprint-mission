package com.sprint.sprint1.mission.validation;

import java.util.regex.Pattern;

public class UserValidation {

        private static final Pattern EMAIL_PATTERN =
                Pattern.compile("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$");


        /**
         * 사용자명 검증 (3~20자, 공백 포함 불가)
         */
        public static boolean isValidUsername(String username) {
            return username != null && username.length() >= 3 && username.length() <= 20 && !username.contains(" ");
        }

        /**
         * 이메일 검증
         */
        public static boolean isValidEmail(String email) {
            return email != null && EMAIL_PATTERN.matcher(email).matches();
        }

        /**
         * 비밀번호 검증 (8자 이상, 숫자+영문 포함)
         */
        public static boolean isValidPassword(String password) {
            return password != null &&
                    password.length() >= 8 &&
                    password.matches(".*[A-Za-z].*") && // 영문 포함
                    password.matches(".*\\d.*"); // 숫자 포함
        }

}
