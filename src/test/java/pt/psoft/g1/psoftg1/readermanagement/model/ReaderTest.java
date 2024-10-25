package pt.psoft.g1.psoftg1.readermanagement.model;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.util.ReflectionTestUtils;
import pt.psoft.g1.psoftg1.exceptions.ConflictException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.readermanagement.services.UpdateReaderRequest;
import pt.psoft.g1.psoftg1.shared.model.Photo;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ReaderTest {
    @Test
    void ensureValidReaderDetailsAreCreated() {
        Reader mockReader = mock(Reader.class);
        assertDoesNotThrow(() -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, null, null));
    }

    @Test
    void ensureExceptionIsThrownForNullReader() {
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, null, "2010-01-01", "912345678", true, false, false, null, null));
    }

    @Test
    void ensureExceptionIsThrownForNullPhoneNumber() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", null, true, false, false, null, null));
    }

    @Test
    void ensureExceptionIsThrownForNoGdprConsent() {
        Reader mockReader = mock(Reader.class);
        assertThrows(IllegalArgumentException.class, () -> new ReaderDetails(123, mockReader, "2010-01-01", "912345678", false, false, false, null, null));
    }

    @Test
    void ensureGdprConsentIsTrue() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, null, null);
        assertTrue(readerDetails.isGdprConsent());
    }

    @Test
    void ensurePhotoCanBeNull_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, null, null);
        assertNull(readerDetails.getPhoto());
    }

    @Test
    void ensureValidPhoto() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, "readerPhotoTest.jpg", null);
        Photo photo = readerDetails.getPhoto();

        //This is here to force the test to fail if the photo is null
        assertNotNull(photo);

        String readerPhoto = photo.getPhotoFile();
        assertEquals("readerPhotoTest.jpg", readerPhoto);
    }

    @Test
    void ensureInterestListCanBeNullOrEmptyList_AkaOptional() {
        Reader mockReader = mock(Reader.class);
        ReaderDetails readerDetailsNullInterestList = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, "readerPhotoTest.jpg", null);
        assertNull(readerDetailsNullInterestList.getInterestList());

        ReaderDetails readerDetailsInterestListEmpty = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, "readerPhotoTest.jpg", new ArrayList<>());
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

        ReaderDetails readerDetails = new ReaderDetails(123, mockReader, "2010-01-01", "912345678", true, false, false, "readerPhotoTest.jpg", genreList);
        assertEquals(2, readerDetails.getInterestList().size());
    }



    @Test
    void ProtectedConstructor() throws NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<Reader> constructor = Reader.class.getDeclaredConstructor();
        constructor.setAccessible(true);
        Reader reader = constructor.newInstance();
        assertNotNull(reader, "The Reader instance must not be null");
    }



    @Test
    void applyPatchTest() {

        Reader reader = Mockito.mock(Reader.class);
        ReaderDetails readerDetails = Mockito.spy(new ReaderDetails(123, reader, "2010-01-01", "912345678", true, false, true, "initialPhoto.jpg", null));
        ReflectionTestUtils.setField(readerDetails, "version", 1L);


        UpdateReaderRequest updateRequest = Mockito.mock(UpdateReaderRequest.class);
        Mockito.when(updateRequest.getPassword()).thenReturn(null);
        Mockito.when(updateRequest.getUsername()).thenReturn(null);
        Mockito.when(updateRequest.getFullName()).thenReturn(null);
        Mockito.when(updateRequest.getBirthDate()).thenReturn(null);
        Mockito.when(updateRequest.getPhoneNumber()).thenReturn(null);
        Mockito.when(updateRequest.getMarketing()).thenReturn(false);




        readerDetails.applyPatch(1L, updateRequest, null, null);


        Mockito.verify(reader, Mockito.never()).setPassword(Mockito.anyString());
        Mockito.verify(reader, Mockito.never()).setUsername(Mockito.anyString());

        Mockito.verify(readerDetails, Mockito.never()).setMarketingConsent(Mockito.anyBoolean());
        // UpdateReaderRequest with non-null values
        Mockito.when(updateRequest.getPassword()).thenReturn("Password123");
        Mockito.when(updateRequest.getUsername()).thenReturn("Username123");
        Mockito.when(updateRequest.getFullName()).thenReturn("Full Name Updated");
        Mockito.when(updateRequest.getBirthDate()).thenReturn("2000-01-01");
        Mockito.when(updateRequest.getPhoneNumber()).thenReturn("987654321");
        Mockito.when(updateRequest.getMarketing()).thenReturn(true);
        Mockito.when(updateRequest.getThirdParty()).thenReturn(true);




        readerDetails.applyPatch(1L, updateRequest, null, null);

        Mockito.verify(reader, Mockito.times(1)).setPassword("Password123");
        Mockito.verify(reader, Mockito.times(1)).setUsername("Username123");
        Mockito.verify(reader, Mockito.times(1)).setName("Full Name Updated");;
        Mockito.verify(readerDetails, Mockito.times(1)).setMarketingConsent(true);
        Mockito.verify(readerDetails, Mockito.times(1)).setThirdPartySharingConsent(true);


    }


    @Test
    void applyPatchTestConflict() {
        ReaderDetails readerDetails = new ReaderDetails(123, Mockito.mock(Reader.class), "2010-01-01", "912345678", true, false, false, null, List.of(Mockito.mock(Genre.class)));
        ReflectionTestUtils.setField(readerDetails, "version", 2L);

        assertThrows(ConflictException.class, () -> readerDetails.applyPatch(1L, Mockito.mock(UpdateReaderRequest.class), null, null));
    }
     @Test
    void removePhotoTest()  {

        ReaderDetails readerDetails = new ReaderDetails(123, Mockito.mock(Reader.class), "2010-01-01", "912345678", true, false, false, null, List.of(Mockito.mock(Genre.class)));
        ReflectionTestUtils.setField(readerDetails, "version", 2L);
        assertThrows(ConflictException.class, () -> readerDetails.removePhoto(1L));

        ReflectionTestUtils.setField(readerDetails, "version", 1L);
        readerDetails.removePhoto(1L);
        assertNull(readerDetails.getPhoto());


    }

    @Test
    public void getPhoneNumberTest(){

        ReaderDetails readerDetails = new ReaderDetails(123, Mockito.mock(Reader.class), "2010-01-01", "912345678", true, false, false, null, null);
        assertEquals("912345678", readerDetails.getPhoneNumber());

    }

    @Test
    public void getReaderTest(){
        Reader reader = Mockito.mock(Reader.class);
        ReaderDetails readerDetails = new ReaderDetails(123,reader, "2010-01-01", "912345678", true, false, false, null, null);
        assertEquals(reader, readerDetails.getReader());
    }

    @Test
    public void getBirthDateTest(){
        ReaderDetails readerDetails = new ReaderDetails(123, Mockito.mock(Reader.class), "2010-01-01", "912345678", true, false, false, null, null);
        assertEquals("2010-1-1", readerDetails.getBirthDate().toString());
    }

    @Test
    public void getMarketingConsentTest(){
        ReaderDetails readerDetails = new ReaderDetails(123, Mockito.mock(Reader.class), "2010-01-01", "912345678", true, true, false, null, null);
        assertTrue(readerDetails.isMarketingConsent());
    }

    @Test
    public void isThirdPartySharingConsentTest(){
        ReaderDetails readerDetails = new ReaderDetails(123, Mockito.mock(Reader.class), "2010-01-01", "912345678", true, false, false, null, null);
        assertFalse(readerDetails.isThirdPartySharingConsent());
    }



}
