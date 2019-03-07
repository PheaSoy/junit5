package com.repo;


import com.domain.Book;
import com.exception.BookException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Stream;

public class BookRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRepository.class);

    private final List<String> TAX_TYPE = Arrays.asList("flat", "percentage");

    private List<Book> books = new ArrayList<>();

    public void init() {

        Book book = new Book();
        Stream.of("Java Cloud Native", "Spring Boot Magic").forEach(
                title -> {
                    book.setId(UUID.randomUUID().toString());
                    book.setTitle(title);
                    book.setAuthor("Jaslong");
                    book.setPrice((Math.random() * (100 - 50)) + 50);
                    book.setTaxType("flat");
                    book.setTax(0.0);
                    book.setTotalCost(book.getPrice());
                    book.setCreatedAt(new Date());
                    books.add(book);
                }

        );
    }

    public Book save(Book requestBook) {

        Date createdAt = new Date();
        double totalCost = 0;
        if (!(TAX_TYPE.stream().filter(bk -> bk.equals(requestBook.getTaxType())).findFirst().isPresent())) {
            throw new BookException("Invalid Tax Type.");
        }

        if (requestBook.getTaxType().equals("flat")) {
            totalCost = requestBook.getPrice() + requestBook.getTax();
        } else {
            double taxPrice = (requestBook.getTax() * 100) / requestBook.getPrice();
            totalCost = requestBook.getPrice() + taxPrice;
        }
        Book book = new Book();
        book.setId(requestBook.getId());
        book.setAuthor(requestBook.getAuthor());
        book.setPrice(requestBook.getPrice());
        book.setCreatedAt(createdAt);
        book.setTitle(requestBook.getTitle());
        book.setTotalCost(totalCost);
        book.setTaxType(requestBook.getTaxType());
        books.add(book); // added to the list;
        return book;
    }

    public Optional<Book> findBookById(String id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst();
    }

    public void clearAll() {
        books.clear();
    }
}
