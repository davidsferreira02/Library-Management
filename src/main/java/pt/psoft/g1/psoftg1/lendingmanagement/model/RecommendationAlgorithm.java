package pt.psoft.g1.psoftg1.lendingmanagement.model;

import pt.psoft.g1.psoftg1.bookmanagement.model.Book;
import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;
import pt.psoft.g1.psoftg1.usermanagement.model.Reader;

import java.util.List;

public interface RecommendationAlgorithm {
    List<Book> recommendBooks(ReaderDetails reader, int numberOfBooks);
}