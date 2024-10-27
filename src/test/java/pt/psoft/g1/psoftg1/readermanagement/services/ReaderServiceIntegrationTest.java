package pt.psoft.g1.psoftg1.readermanagement.services;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.security.access.AccessDeniedException;

import org.springframework.transaction.annotation.Transactional;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.util.HashSet;

@Transactional
@SpringBootTest
public class ReaderServiceIntegrationTest {

    @Autowired
    private ReaderRepository readerRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private ReaderService readerService;

    private ReaderDetails readerDetails;
    private Reader reader;

    @BeforeEach
    public void setUp(){
        reader = Reader.newReader("manuel@gmail.com", "Manuelino123!", "Manuel Sarapinto das Coives");
        userRepository.save(reader);

        readerDetails = new ReaderDetails(1,
                reader,
                "2000-01-01",
                "919191919",
                true,
                true,
                true,
                null,
                null);
        readerRepository.save(readerDetails);
    }

    @AfterEach
    public void tearDown(){
        readerRepository.delete(readerDetails);
        userRepository.delete(reader);
    }

    @Test
    public void testCreateDisabledAndDeleteUser(){
        User user = userService.create(new CreateUserRequest("test1@gmail.com", "Manuelino123!", "password", Role.READER,new HashSet<>()));
        assertNotNull(user);
        assertEquals(userRepository.findByUsername(user.getUsername()).get().getUsername(), user.getUsername());
        userService.delete(user.getId());
        assertFalse(user.isEnabled());
        userRepository.delete(user);
        assertTrue(userRepository.findByUsername(user.getUsername()).isEmpty());
    }

    @Test
    public void testNotAuthenticated(){
        assertThrows(AccessDeniedException.class, () -> userService.getAuthenticatedUser(null));
    }

    @Test
    void findAll(){
        assertEquals(readerService.findAll().toString(), readerRepository.findAll().toString());
    }

    @Test
    void testFindByUsername() {
        assertEquals(readerService.findByUsername(reader.getUsername()), readerRepository.findByUsername(reader.getUsername()));
    }

    @Test
    void testFindByPhoneNumber() {
        assertEquals(readerService.findByPhoneNumber(reader.getUsername()), readerRepository.findByPhoneNumber(reader.getUsername()));
    }



}
