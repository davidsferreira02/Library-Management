package pt.psoft.g1.psoftg1.lendingmanagement.model;

import jakarta.persistence.Entity;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.PageRequest;
import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.bookmanagement.repositories.BookRepository;
import pt.psoft.g1.psoftg1.lendingmanagement.repositories.LendingRepository;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import org.springframework.data.domain.Pageable;
import pt.psoft.g1.psoftg1.shared.services.AppConfig;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Profile("alg2")
public class Alg2 implements RecommendationAlgorithm {

    private final BookRepository bookRepository;

    private final LendingRepository lendingRepository;

    private final AppConfig appConfig;


    public Alg2(BookRepository bookRepository, LendingRepository lendingRepository, AppConfig appConfig) {
        this.bookRepository = bookRepository;
        this.lendingRepository = lendingRepository;
        this.appConfig = appConfig;
    }

    @Override
    public List<Book> recommendBooks(ReaderDetails readerDetails, int numberOfBooks) {
        int age = Period.between(readerDetails.getBirthDate().getBirthDate(), LocalDate.now()).getYears();

        String genre;

        if (age < appConfig.getAge().getMin()) {
            genre = "children";
        } else if (age <  appConfig.getAge().getMax()) {
            genre = "juvenile";
        } else {
            genre = lendingRepository.findMostBorrowedGenreByReader(readerDetails.getReader().getId());
        }
        Pageable pageable = PageRequest.of(0, numberOfBooks);
        return bookRepository.findXBooksByGenre(genre, pageable);
    }

}
