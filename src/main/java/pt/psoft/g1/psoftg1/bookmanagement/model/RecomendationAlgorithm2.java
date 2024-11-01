package pt.psoft.g1.psoftg1.bookmanagement.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.bookmanagement.services.BookCountDTO;
import pt.psoft.g1.psoftg1.genremanagement.model.Genre;
import pt.psoft.g1.psoftg1.genremanagement.repositories.GenreRepository;
import pt.psoft.g1.psoftg1.genremanagement.services.GenreLendingsDTO;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Component("RecomendationAlgorithm2")
public class RecomendationAlgorithm2 implements Recomendation {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LendingRepository lendingRepository;

    @Value("${minAge}")
    private int minAge;

    @Value("${maxAge}")
    private int maxAge;


    @Override
    public List<Book> recommendBooks(ReaderDetails readerDetails, int numberOfBooks, int nGenres) {
        int age = Period.between(readerDetails.getBirthDate().getBirthDate(), LocalDate.now()).getYears();

        String genre;

        if (age < minAge) {
            genre = "children";
        } else if (age <  maxAge) {
            genre = "juvenile";
        } else {
            genre = lendingRepository.findMostBorrowedGenreByReader(readerDetails.getReader().getId());
        }
        Pageable pageable = PageRequest.of(0, numberOfBooks);
        return bookRepository.findXBooksByGenre(genre, pageable);
    }
}