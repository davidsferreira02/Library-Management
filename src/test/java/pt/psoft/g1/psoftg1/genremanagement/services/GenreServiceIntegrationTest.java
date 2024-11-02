package pt.psoft.g1.psoftg1.genremanagement.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.bookmanagement.services.GenreBookCountDTO;
import pt.psoft.g1.psoftg1.exceptions.NotFoundException;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.shared.services.Page;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
@SpringBootTest
public class GenreServiceIntegrationTest {

    @Autowired
    private GenreServiceImpl genreService;

    @Autowired
    private GenreRepository genreRepository;

    private Genre genre;

    @BeforeEach
    void setUp() {
        genreRepository.deleteAll();
        genre = new Genre("Science Fiction");
        genreRepository.save(genre);
    }

    @Test
    void getLendingsPerMonthLastYearByGenre() {
        List<GenreLendingsPerMonthDTO> serviceResult = genreService.getLendingsPerMonthLastYearByGenre();
        List<GenreLendingsPerMonthDTO> repositoryResult = genreRepository.getLendingsPerMonthLastYearByGenre();
        assertEquals(serviceResult, repositoryResult);
    }

    @Test
    void save() {
        assertEquals(genreService.save(genre).getGenre().toString(), genreRepository.save(genre).getGenre().toString());
    }

    @Test
    void findAll(){
        assertEquals(genreService.findAll().toString(), genreRepository.findAll().toString());
    }

    @Test
    void findByString() {
        assertEquals(genreService.findByString("Science Fiction").get().getGenre().toString(), genreRepository.findByString("Science Fiction").get().getGenre().toString());
    }

    @Test
    void findTop5GenreByBookCount() {
        Pageable pageable = PageRequest.of(0, 5);
        List<GenreBookCountDTO> serviceResult = genreService.findTopGenreByBooks();
        org.springframework.data.domain.Page<GenreBookCountDTO> repositoryResult = genreRepository.findTop5GenreByBookCount(pageable);



        assertEquals(repositoryResult.getContent(), serviceResult);
    }


    @Test
    void getAverageLendings(){
        GetAverageLendingsQuery query = new GetAverageLendingsQuery();
        query.setYear(2023);
        query.setMonth(5);
        Page page = new Page(1, 10);
        assertEquals(genreService.getAverageLendings(query, page),genreRepository.getAverageLendingsInMonth(LocalDate.of(query.getYear(), query.getMonth(), 1),page));
    }


    @Test
    void GetLendingsAverageDurationPerMonthExceptionTest() {
        String start = "01-2023-01";
        String end = "2023-12-31";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            genreService.getLendingsAverageDurationPerMonth(start, end);
        });

        assertEquals("Expected format is YYYY-MM-DD", thrown.getMessage());
    }

    @Test
    void GetLendingsAverageDurationPerMonthExceptionTest2() {
        String start = "2023-12-31";
        String end = "2023-01-01";

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            genreService.getLendingsAverageDurationPerMonth(start, end);
        });

        assertEquals("Start date cannot be after end date", thrown.getMessage());
    }

    @Test
    void GetLendingsAverageDurationPerMonthExceptionTest3() {
        String start = "2023-01-01";
        String end = "2023-12-31";


        NotFoundException thrown = assertThrows(NotFoundException.class, () -> {
            genreService.getLendingsAverageDurationPerMonth(start, end);
        });

        assertEquals("No objects match the provided criteria", thrown.getMessage());
    }
}
*/


