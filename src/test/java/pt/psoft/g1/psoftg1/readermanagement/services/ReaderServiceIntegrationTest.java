package pt.psoft.g1.psoftg1.readermanagement.services;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.boot.test.context.SpringBootTest;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.access.AccessDeniedException;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.readermanagement.repositories.ReaderRepository;
import pt.psoft.g1.psoftg1.readermanagement.services.ReaderService;
import pt.psoft.g1.psoftg1.shared.model.ForbiddenName;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.shared.repositories.ForbiddenNameRepository;
import pt.psoft.g1.psoftg1.shared.repositories.PhotoRepository;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;
import pt.psoft.g1.psoftg1.usermanagement.model.Role;
import pt.psoft.g1.psoftg1.usermanagement.model.User;
import pt.psoft.g1.psoftg1.usermanagement.repositories.UserRepository;
import pt.psoft.g1.psoftg1.usermanagement.services.CreateUserRequest;
import pt.psoft.g1.psoftg1.usermanagement.services.UserService;

import java.time.LocalDate;
import java.util.*;

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
    private ReaderServiceImpl readerService;


    @Autowired
    private ForbiddenNameRepository forbiddenNameRepository;
    @Autowired
    private PhotoRepository photoRepository;

    private ReaderDetails readerDetails;
    private Reader reader;

    @BeforeEach
    public void setUp() {
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
    public void tearDown() {
        readerRepository.delete(readerDetails);
        userRepository.delete(reader);
    }


    @Test
    public void testNotAuthenticated() {
        assertThrows(AccessDeniedException.class, () -> userService.getAuthenticatedUser(null));
    }

    @Test
    void findAll() {
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

    @Test
    void shouldIgnorePhotoUpdateIfNewPhotoProvidedButNoURI() {
        Long readerId = readerDetails.getReader().getId();
        long desiredVersion = readerDetails.getVersion();

        MultipartFile newPhoto = new MockMultipartFile("newPhoto", "new-photo.jpg", "image/jpeg", "image data".getBytes());

        UpdateReaderRequest updateRequest = new UpdateReaderRequest();
        updateRequest.setPhoto(newPhoto);

        ReaderDetails updatedReader = readerService.update(readerId, updateRequest, desiredVersion, null);

        assertNull(updatedReader.getPhoto());

    }

    @Test
    void testSearchReaders() {
        // Setup
        pt.psoft.g1.psoftg1.shared.services.Page page = new pt.psoft.g1.psoftg1.shared.services.Page(1, 10);
        SearchReadersQuery query = new SearchReadersQuery("Manuel", "", "");

        // Execute
        List<ReaderDetails> result = readerService.searchReaders(page, query);

        // Verify
        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("Manuel Sarapinto das Coives", result.get(0).getReader().getName().toString());
    }



    @Test
    void testFindByReaderNumber_ExistingReader() {
        Optional<ReaderDetails> foundReader = readerService.findByReaderNumber(readerDetails.getReaderNumber());
        assertTrue(foundReader.isPresent());
        assertEquals(readerDetails.getReaderNumber(), foundReader.get().getReaderNumber());
        assertEquals("Manuel Sarapinto das Coives", foundReader.get().getReader().getName().toString());
    }


    @Test
    void testCreateReaderWithDuplicateUsername() {

        userRepository.save(new Reader("duplicate_user@example.com", "AnotherPassword123"));


        CreateReaderRequest createReaderRequest = new CreateReaderRequest();
        createReaderRequest.setUsername("duplicate_user@example.com");
        createReaderRequest.setPassword("SecurePassword123");
        createReaderRequest.setFullName("John Smith");
        createReaderRequest.setBirthDate("1990-01-01");
        createReaderRequest.setPhoneNumber("123456789");


        assertThrows(ConflictException.class, () -> readerService.create(createReaderRequest, null));
    }

    @Test
    void testCreateReaderWithForbiddenName() {

        ForbiddenName forbiddenName = new ForbiddenName("forbidden");
        forbiddenNameRepository.save(forbiddenName);


        CreateReaderRequest createReaderRequest = new CreateReaderRequest();
        createReaderRequest.setUsername("new_user@example.com");
        createReaderRequest.setPassword("SecurePassword123");
        createReaderRequest.setFullName("John forbidden Smith");
        createReaderRequest.setBirthDate("1990-01-01");
        createReaderRequest.setPhoneNumber("123456789");


        assertThrows(IllegalArgumentException.class, () -> readerService.create(createReaderRequest, null));
    }

}
