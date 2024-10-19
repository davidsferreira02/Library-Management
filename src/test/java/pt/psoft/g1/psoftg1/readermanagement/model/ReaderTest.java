package pt.psoft.g1.psoftg1.readermanagement.model;

import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;
import pt.psoft.g1.psoftg1.authormanagement.model.Author;
import pt.psoft.g1.psoftg1.authormanagement.services.UpdateAuthorRequest;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.usermanagement.model.Librarian;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

public class ReaderTest {
    @Test
    void ensureValidReaderDetailsAreCreated() {
        Reader mockReader = mock(Reader.class);
        assertDoesNotThrow(() -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null, null));
    }

    @Test
    void ensureExceptionIsThrownForNullReader() {
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, null, "2010-01-01", "912345678", true, false, false,null,null));
    }

    @Test
    void ensureExceptionIsThrownForNullPhoneNumber() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", null, true, false, false,null,null));
    }

    @Test
    void ensureExceptionIsThrownForNoGdprConsent() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", false, false, false,null,null));
    }

    @Test
    void ensureGdprConsentIsTrue() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null,null);
        assertTrue(readerDetails.isGdprConsent());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,null,null);
        assertNull(readerDetails.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",null);
        Photo photo = readerDetails.getPhoto();

        //This is here to force the test to fail if the photo is null
        assertNotNull(photo);

        String readerPhoto = photo.getPhotoFile();
        assertEquals("readerPhotoTest.jpg", readerPhoto);
    }

    @Test
    void ensureInterestListCanBeNullOrEmptyList_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetailsNullInterestList = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",null);
        assertNull(readerDetailsNullInterestList.getInterestList());

        ReaderDetails readerDetailsInterestListEmpty = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",new ArrayList<>());
        assertEquals(0, readerDetailsInterestListEmpty.getInterestList().size());
    }

    @Test
    void ensureInterestListCanTakeAnyValidGenre() {
        Reader mockReader = mock(Reader.class);
        Genre g1 = new Genre("genre1");
        Genre g2 = new Genre("genre2");
        List<Genre> genreList = new ArrayList<>();
        genreList.add(g1);
        genreList.add(g2);

        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false,"readerPhotoTest.jpg",genreList);
        assertEquals(2, readerDetails.getInterestList().size());
    }


    // Testes unitários

    @Test
    void ensureExceptionIsThrownForNullUsername() {
        assertThrows(IllegalArgumentException.class, () -> new Reader(null, "password"));
    }



    @Test
    void getReader(){
        Reader reader = new Reader("username", "Password95");
        ReaderDetails readerDetails=new ReaderDetails(123, reader, "2010-01-01", "912345678", true, false, false,null,null);
        assertEquals(reader, readerDetails.getReader());
    }
    @Test
    void ensureBirthDateIsCorrectlyRetrieved() {
        Reader reader = new Reader("username", "Password95");
        ReaderDetails readerDetails = new ReaderDetails(123, reader, "2010-01-01", "912345678", true, false, false, null, null);
        BirthDate expectedBirthDate = new BirthDate("2010-01-01");
        assertEquals(expectedBirthDate.toString(), readerDetails.getBirthDate().toString());
    }

    @Test
    void getPhoneNumber(){
        Reader reader = new Reader("username", "Password95");
        ReaderDetails readerDetails=new ReaderDetails(123, reader, "2010-01-01", "912345678", true, false, false,null,null);
        assertEquals("912345678", readerDetails.getPhoneNumber().toString());
    }

    @Test
    void getMarketingConsent() {
        Reader reader = new Reader("username", "Password95");
        ReaderDetails readerDetails = new ReaderDetails(123, reader, "2010-01-01", "912345678", true, false, false, null, null);
        assertFalse(readerDetails.isMarketingConsent());
    }

    @Test
    void getThirdPartySharingConsent(){
        Reader reader = new Reader("username", "Password95");
        ReaderDetails readerDetails=new ReaderDetails(123, reader, "2010-01-01", "912345678", true, false, false,null,null);
        assertFalse(readerDetails.isThirdPartySharingConsent());
    }


    @Test
    void applyPatchReaderTest() {
        Reader reader = new Reader("username", "Password95");
        MultipartFile mockPhoto = mock(MultipartFile.class);
        List<Genre> genreList = List.of(new Genre("genre1"), new Genre("genre2"));

        UpdateReaderRequest updateReaderRequest = new UpdateReaderRequest();
        updateReaderRequest.setNumber("123");
        updateReaderRequest.setUsername("newUsername");
        updateReaderRequest.setPassword("newPassword123");
        updateReaderRequest.setFullName("full name updated");
        updateReaderRequest.setBirthDate("2000-01-01");
        updateReaderRequest.setPhoneNumber("987654321");
        updateReaderRequest.setMarketing(true);
        updateReaderRequest.setThirdParty(true);
        updateReaderRequest.setInterestList(List.of("genre1", "genre2"));
        updateReaderRequest.setPhoto(mockPhoto);

        ReaderDetails readerDetails = new ReaderDetails(123, reader, "2010-01-01", "912345678", true, false, false, null, genreList);

        ReflectionTestUtils.setField(readerDetails, "version", 1L);

        readerDetails.applyPatch(readerDetails.getVersion(), updateReaderRequest, null, null);

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        assertEquals("newUsername", reader.getUsername());
        assertTrue(passwordEncoder.matches("newPassword123", reader.getPassword()));

        // Compare components of the birth date directly
        LocalDate birthDate = readerDetails.getBirthDate().getBirthDate();
        assertEquals(2000, birthDate.getYear());
        assertEquals(1, birthDate.getMonthValue());
        assertEquals(1, birthDate.getDayOfMonth());

        assertEquals("987654321", readerDetails.getPhoneNumber());
        assertTrue(readerDetails.isMarketingConsent());
        assertTrue(readerDetails.isThirdPartySharingConsent());
        assertEquals(2, readerDetails.getInterestList().size());
        assertNull(readerDetails.getPhoto());
    }

    @Test
    void ProtectedConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Reader> constructor =Reader.class.getDeclaredConstructor();


        constructor.setAccessible(true);


        Reader reader  = constructor.newInstance();

        assertNotNull(reader, "The Reader instance must not be null");
    }
}
