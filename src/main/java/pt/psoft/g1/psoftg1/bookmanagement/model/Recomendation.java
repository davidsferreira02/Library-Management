package pt.psoft.g1.psoftg1.bookmanagement.model;

import pt.psoft.g1.psoftg1.readermanagement.model.ReaderDetails;

import java.util.List;

public interface Recomendation {
    List<Book> recommendBooks(ReaderDetails reader, int numberOfBooks, int genre);
}
