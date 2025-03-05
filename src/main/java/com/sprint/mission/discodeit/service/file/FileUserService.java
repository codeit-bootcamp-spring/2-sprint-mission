
public class FileUserService implements UserService {
    private static final File Ufile;
    // user 목록
    private Map<UUID, User> usermap = new HashMap<>();


    // Create - 생성
    @Override
    public void CreateUser(String name) {
        // 생성자를 통해 id 생성
        User user = new User(name);
        usermap.put(user.getId(), user);
    }

    // Read - 읽기, 조회
    @Override
    public Map<UUID, User> getAllUser(){
        return usermap;
    }
    @Override
    public Optional<User> getoneUser(UUID id){
        return Optional.ofNullable(usermap.get(id));
    }
    // Update - 수정

    @Override
    public void UpdateUser(String newName, UUID id) {
        usermap.get(id).updateUser(newName);
    }

    @Override
    public void DeleteUser(UUID id) {
        usermap.remove(id);
    }
}
