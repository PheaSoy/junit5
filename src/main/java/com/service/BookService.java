package com.service;

import com.domain.Book;
import com.exception.BookException;
import com.repo.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Hero is the main entity we'll be using to . . .
 * <p>
 * Please see the {@link com.domain.Book} class for true identity
 *
 * @author Phea Soy
 */

public class BookService {

    private static final Logger LOGGER
            = LoggerFactory.getLogger(BookService.class);

    final BookRepository bookRepository;

    public BookService(BookRepository bookRepository){
        this.bookRepository =bookRepository;
    }

    Book createBook(Book requestBook) {
        return bookRepository.save(requestBook);
    }

    Optional<Book> getBookById(String id) {
        return bookRepository.findBookById(id);
    }
    public void clearListBook(){
        bookRepository.clearAll();
    }

}
