import com.example.userservice.dao.UserDao;
import com.example.userservice.dao.UserDaoImpl;
import com.example.userservice.model.User;
import com.example.userservice.util.HibernateUtil;
import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@Testcontainers
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class IntegrationTests {

    @Container
    private static final PostgreSQLContainer<?> cont =
            new PostgreSQLContainer<>("postgres:latest")
            .withDatabaseName("testdb")
            .withUsername("testuser")
            .withPassword("testpass");

    private static UserDao userDao;

    @BeforeAll
    static void setup() {

        //Настройка хибера чтобы работал с testcontainers
        System.setProperty("hibernate.connection.url", cont.getJdbcUrl());
        System.setProperty("hibernate.connection.username", cont.getUsername());
        System.setProperty("hibernate.connection.password", cont.getPassword());

        userDao = new UserDaoImpl();
    }

    @Test
    @Order(1)
    void shouldSaveUser() {
        User user = new User("Test User", "test@example.com", 30);
        User savedUser = userDao.save(user);

        assertNotNull(savedUser.getId());
        assertEquals("Test User", savedUser.getName());
        assertEquals("test@example.com", savedUser.getEmail());
        assertEquals(30, savedUser.getAge());
    }

    @Test
    @Order(2)
    void shouldFindUserById() {
        User user = new User("Test User №2", "test2@example.com", 25);
        User savedUser = userDao.save(user);

        User foundUser = userDao.findById(savedUser.getId());

        assertNotNull(foundUser);
        assertEquals(savedUser.getId(), foundUser.getId());
        assertEquals("Test User №2", foundUser.getName());
    }

    @Test
    @Order(3)
    void shouldUpdatedUser() {
        User user = new User("Test User №3", "test3@example.com", 35);
        User savedUser = userDao.save(user);

        savedUser.setName("Updated Name");
        User updatedUser = userDao.update(savedUser);

        assertEquals("Updated Name", updatedUser.getName());
    }

    @Test
    @Order(4)
    void shouldDeleteUser() {
        User user = new User("Test User №4", "test4@example.com", 40);
        User savedUser = userDao.save(user);

        userDao.delete(savedUser);

        User deletedUser = userDao.findById(savedUser.getId());

        assertNull(deletedUser);
    }

    @Test
    @Order(5)
    void shouldFindAllUsers() {
        userDao.save(new User("User 1", "user1@example.com", 20));
        userDao.save(new User("User 2", "user2@example.com", 25));

        List<User> users = userDao.findAll();

        assertFalse(users.isEmpty());
        assertTrue(users.size() >= 2);
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.shutdown();
    }
}
