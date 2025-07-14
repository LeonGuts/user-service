
import com.example.userservice.dao.UserDao;
import com.example.userservice.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UnitTest {

    @Mock
    private UserDao userDao;

    @InjectMocks
    private ServiceEntity serviceEntity;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User("Test User", "test@example.com", 30);
        testUser.setId(1L);
    }

    @Test
    void shouldCreateUser() {
        when(userDao.save(any(User.class))).thenReturn(testUser);

        User createdUser = serviceEntity.createUser("Test User", "test@example.com", 30);

        Assertions.assertNotNull(createdUser);
        Assertions.assertEquals("Test User", createdUser.getName());
        verify(userDao, times(1)).save(any(User.class));
    }

    @Test
    void shouldGetUserById() {
        when(userDao.findById(1L)).thenReturn(testUser);

        Optional<User> foundUser = serviceEntity.getUserById(1L);

        assertTrue(foundUser.isPresent());
        assertEquals("Test User", foundUser.get().getName());
        verify(userDao, times(1)).findById(1L);
    }

    @Test
    void shouldReturnEmptyWhenUserNotFound() {
        when(userDao.findById(999L)).thenReturn(null);

        Optional<User> foundUser = serviceEntity.getUserById(999L);

        assertTrue(foundUser.isEmpty());
        verify(userDao, times(1)).findById(999L);
    }

    @Test
    void shouldGetAllUsers() {
        List<User> users = Arrays.asList(
                new User("User №1", "user1@example.com", 20),
                new User("User №2", "user2@example.com",25)
        );

        when(userDao.findAll()).thenReturn(users);

        List<User> result = serviceEntity.getAllUsers();

        assertEquals(2, result.size());
        verify(userDao, times(1)).findAll();
    }

    @Test
    void shouldUpdateUser() {
        User updatedUser = new User("Updated Name", "updated@example.com", 35);
        updatedUser.setId(1L);

        when(userDao.findById(1L)).thenReturn(testUser);
        when(userDao.update(any(User.class))).thenReturn(updatedUser);

        User result = serviceEntity.updateUser(1L, "Updated Name", "updated@example.com", 35);

        assertEquals("Updated Name", result.getName());
        assertEquals("updated@example.com", result.getEmail());

        verify(userDao, times(1)).findById(1L);
        verify(userDao, times(1)).update(any(User.class));
    }

    @Test
    void shouldDeleteUser() {
        when(userDao.findById(1L)).thenReturn(testUser);
        doNothing().when(userDao).delete(testUser);

        serviceEntity.deleteUser(1L);

        verify(userDao, times(1)).findById(1L);
        verify(userDao, times(1)).delete(testUser);
    }
}
