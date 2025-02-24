package com.sprint.mission.discodeit.entity;

public class User extends BaseEntity implements Comparable<User> {
    private String userName;
    private String userEmail;
    private String password;

    public User(String userName, String userEmail, String password) {
        super();
        validateUserName(userName);
        validateUserEmail(userEmail);
        validatePassword(password);
        this.userName = userName;
        this.userEmail = userEmail;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public String getPassword() {
        return password;
    }

    public void updateUserName(String newUserName) {
        validateUserName(newUserName);
        this.userName = newUserName;
        super.updateUpdatedAt();
    }

    public void updateUserPassword(String newPassword) {
        validatePassword(newPassword);
        this.password = newPassword;
        super.updateUpdatedAt();
    }

    private void validateUserName(String userName) {
        if (userName == null || userName.trim().isEmpty()) {
            throw new IllegalArgumentException("Username 은 null 이거나 공백일 수 없다!!!");
        }
    }
    private void validateUserEmail(String userEmail) {
        if (userEmail == null || userEmail.trim().isEmpty()) {
            throw new IllegalArgumentException("UserEmail 은 null 이거나 공백일 수 없다!!!");
        }

        // 이메일 패턴 검증 예외처리 추가로 필요 + 중복확인
    }
    private void validatePassword(String password) {
        if (password == null || password.trim().isEmpty()) {
            throw new IllegalArgumentException("password 은 null 이거나 공백일 수 없다!!!");
        }

        // 비밀번호 패턴 검증 예외처리 추가로 필요
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return super.getId().equals(user.getId());
    }

    @Override
    public int hashCode() {
        return super.getId().hashCode();
    }

    @Override
    public String toString() {
        return "\nUser\n"
                + "userName: " + userName + '\n'
                + "userEmail: " + userEmail + '\n'
                + "password: " + password + '\n'
                + "id: " + super.getId() + '\n'
                + "createdAt: " + super.getCreatedAt() + '\n'
                + "updatedAt: " + super.getUpdatedAt() + '\n';
    }

    @Override
    public int compareTo(User other) {
        return this.userName.compareTo(other.userName);
    }
}
