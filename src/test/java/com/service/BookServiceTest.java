package com.service;

import com.domain.Book;
import com.exception.BookException;
import com.repo.BookRepository;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.lang.reflect.Field;
import java.time.Duration;
import java.util.Optional;
import java.util.UUID;

@TestMethodOrder(OrderAnnotation.class) // Making the order method running by using @Order()
@ExtendWith(MockitoExtension.class) // @Using the Mockito Extension for Mocking the Object.
public class BookServiceTest {

    private static Book requestBook = new Book();

    private Book resultBook = new Book();

    static private BookService bookService;

    private static String bookIdGenerated = UUID.randomUUID().toString();

    @Mock
    private BookRepository bookRepository;

    @BeforeAll
    public static void setUp() {
        requestBook.setId(bookIdGenerated); // Assigned the bookId one time only when created book.
    }

    @BeforeEach // Running before each method execute.
    public void init() {
        bookService = new BookService(bookRepository); // Created instance of BookService to test.
        // Initialized book object.
        requestBook.setTaxType("flat");
        requestBook.setAuthor("Phea Soy");
        requestBook.setTitle("Hello Junit5");
        requestBook.setPrice(10.0);
        requestBook.setTax(0.5);
        requestBook.setTotalCost(requestBook.getPrice()+requestBook.getTax());
    }

    @FastTest // Define the tag. It will be use full to identify such as specification. Example @ProductionTest, @DevelopmentTest.
    @Order(1)
    @DisplayName("Test created book return ok.") // DisPlayName of method in console.
    public void testCreatedBook() {

        given(bookRepository.save(requestBook)).willReturn(requestBook);
        Book book = bookService.createBook(requestBook);
        assertNotNull(book);
    }

    @Test
    @DisplayName("Create book with throw exception") // Testing exception
    public void testCreateBookError() {
        requestBook.setTaxType("tax type error");

        given(bookRepository.save(requestBook)).willThrow(new BookException("Invalid Tax Type."));

        assertThrows(BookException.class,() -> bookService.createBook(requestBook));

        // Assigned the exception then check the detail message.
        Exception exception = assertThrows(BookException.class, ()->bookService.createBook(requestBook));
        assertEquals("Invalid Tax Type.",exception.getMessage());
    }

    @Test
    @Disabled("Coming soon....") // Disable the some case happen. We can build it later on.
    public void testDeleteBook() {
    }


    @Order(2)
    @RepeatedTest(3) // Repeating the testing time.
    @DisplayName("Test search book should be ok.")
    public void testSearchBookAfterCreated() {
        given(bookRepository.findBookById(bookIdGenerated)).willReturn(Optional.ofNullable(requestBook));
        Optional<Book> book = bookService.getBookById(bookIdGenerated);
        assertEquals(book.isPresent(),true);
    }

    @Test
    public void testCreateTheBookWithTimeoutReturnOK(){
        given(bookRepository.save(requestBook)).willReturn(resultBook);
        assertTimeout(Duration.ofMillis(2),() -> bookService.createBook(requestBook));
        //Asserts that execution of the supplied executable completes before the given timeout is exceeded.
    }

    @AfterAll// Running after each method execute.
    public static void finished() {
        System.out.println("Finished method running.");
        bookService.clearListBook();
    }



}
