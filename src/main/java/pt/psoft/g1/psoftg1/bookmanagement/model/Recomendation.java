package pt.psoft.g1.psoftg1.bookmanagement.model;

import java.util.List;

public interface Recomendation {

    List<Book> recommendBooks(int numberOfBooks, int genre);

}
