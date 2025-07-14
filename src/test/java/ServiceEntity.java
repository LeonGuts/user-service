import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;

import java.util.List;
import java.util.Optional;

public class ServiceEntity {

    private final UserDao userDao;

    public ServiceEntity(UserDao userDao) {
        this.userDao = userDao;
    }

    public User createUser(String name, String email, Integer age) {
        User user = new User(name, email,age);
        return userDao.save(user);
    }

    public Optional<User> getUserById(Long id) {
        User user = userDao.findById(id);
        return Optional.ofNullable(user);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }

    public User updateUser(Long id, String name, String email, Integer age) {
        User user = userNotFound(id);

        if (name != null) user.setName(name);
        if (email != null) user.setEmail(email);
        if (age != null) user.setAge(age);

        return userDao.update(user);
    }

    public void deleteUser(Long id) {
        User user = userNotFound(id);

        userDao.delete(user);
    }

    private User userNotFound(Long id) {
        User user = userDao.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found with id: " + id);
        }
        return user;
    }
}
