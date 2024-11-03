package pt.psoft.g1.psoftg1.usermanagement.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.EditUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import jakarta.persistence.EntityManager;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private ForbiddenNameRepository forbiddenNameRepository;

    @Test
    @Transactional
    public void UpdateTest() {

        User user = new User("maria@example.com", "Password123");
        userRepository.save(user);
        entityManager.flush();


        Long userId = user.getId();


        Set<String> role = Set.of("READER");
        EditUserRequest editUserRequest = new EditUserRequest("mariadjnfdsj", "João", "Password145", role);


        userService.update(userId, editUserRequest);


        User updatedUser = userRepository.findById(userId).orElseThrow();
        assertEquals(editUserRequest.getUsername(), updatedUser.getUsername());
    }

    @Test
    @Transactional
    public void deleteTest() {
        User user = new User("maria@example.com", "Password123");
        userRepository.save(user);  // Save user and let database assign ID
        entityManager.flush();  // Ensure the user is persisted to the database


        User deletedUser = userService.delete(user.getId());


        assertFalse(deletedUser.isEnabled());
        User userFromDb = userRepository.findById(user.getId()).orElseThrow();
        assertFalse(userFromDb.isEnabled());
    }

    @Test
    @Transactional
    public void create() {
        forbiddenNameRepository.save(new ForbiddenName("forbidden"));


        CreateUserRequest request = new CreateUserRequest("Username", "Password123", "Valid Name");
        request.setRole("READER");

        User createdUser = userService.create(request);

        User userFromDb = userRepository.findById(createdUser.getId()).orElseThrow();
        assertEquals(request.getUsername(), userFromDb.getUsername());
        assertTrue(userFromDb.isEnabled());
        assertNotNull(userFromDb.getPassword() );
    }
}
