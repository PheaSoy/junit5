# junit5
This Project demo about Junit5 with Mockito running together.

## Getting Start
This docs will show you some features of junit5. 
* @BeforeAll
* @BeforeEach
* @DisplayName
* @FastTest
* @RepeatTest
* @DisableTest
* @AfterEach
* @AfterAll
* assertThrows
* assertTimeOut

### Setup
Adding the dependencies in your pom.xml file for maven.
I am using the Junit 5.4.0 and Mockito 2.23.4.

Junit dependencies
````
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-api</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>
    
    <dependency>
        <groupId>org.junit.jupiter</groupId>
        <artifactId>junit-jupiter-engine</artifactId>
        <version>${junit.version}</version>
        <scope>test</scope>
    </dependency>

````


Mockito Extension dependency

```

<dependency>
    <groupId>org.mockito</groupId>
    <artifactId>mockito-junit-jupiter</artifactId>
    <version>${mockito.version}</version>
    <scope>test</scope>
</dependency>

```

### Usage

This is my BookRepository class.

```
public class BookRepository {

    private static final Logger LOGGER = LoggerFactory.getLogger(BookRepository.class);

    private final List<String> TAX_TYPE = Arrays.asList("flat", "percentage");

    private List<Book> books = new ArrayList<>();

    public Book save(Book requestBook) {
        -
        -
        -
    }

    public Optional<Book> findBookById(String id) {
        return books.stream().filter(book -> book.getId().equals(id)).findFirst();
    }

    public void clearAll() {
        books.clear();
    }
}

```

The BookService is a class contain the logic and inject the BookRepository as constructor injection class for CRUD.

```
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
``` 

#### Writing the BookServiceTest

```
   @TestMethodOrder(OrderAnnotation.class)
   @ExtendWith(MockitoExtension.class)
   public class BookServiceTest {
   
        private static Book requestBook = new Book();
    
        private Book resultBook = new Book();
    
        static private BookService bookService;
    
        private static String bookIdGenerated = UUID.randomUUID().toString();
    
        @Mock
        private BookRepository bookRepository;
   }
```

To be able to run the Mockito in the junit5, junit5 provide the @ExtendWith to extend with the  3th party extension.

* @ExtendWith(MockitoExtension.class) for Mockito extension.

* @TestMethodOrder to running the order of the test method we want. There are 3 type test order method:

        1.  Alphanumeric: sorts test methods alphanumerically based on their names and formal parameter lists.
        2.  OrderAnnotation: sorts test methods numerically based on values specified via the @Order annotation.
        3.  Random: orders test methods pseudo-randomly and supports configuration of a custom seed.

#### Setup and initial for the test

```
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
```

* Test create the book with @FastTest annotation

```
    @FastTest // Define the tag. It will be use full to identify such as specification. Example @ProductionTest, @DevelopmentTest.
    @Order(1)
    @DisplayName("Test created book return ok.") // DisPlayName of method in console.
    public void testCreatedBook() {
        given(bookRepository.save(requestBook)).willReturn(requestBook);
        Book book = bookService.createBook(requestBook);
        assertNotNull(book);
    }
```

* Test create the book with throw exception
```
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
```
assertThrows is powerful for exception testing. it's also support java8 lambda expression.

* Test disable for create a test with @Disabled

```
    @Test
    @Disabled("Coming soon....") // Disable the some case happen. We can build it later on.
    public void testDeleteBook() {
    }
```

* Test repeated test method with @RepeatedTest
```
    @Order(2)
    @RepeatedTest(3) // Repeating the testing time.
    @DisplayName("Test search book should be ok.")
    public void testSearchBookAfterCreated() {
        given(bookRepository.findBookById(bookIdGenerated)).willReturn(Optional.ofNullable(requestBook));
        Optional<Book> book = bookService.getBookById(bookIdGenerated);
        assertEquals(book.isPresent(),true);
    }
```

* Test a method with assertTimeout
````
@Test
public void testCreateTheBookWithTimeoutReturnOK(){
    given(bookRepository.save(requestBook)).willReturn(resultBook);
    assertTimeout(Duration.ofMillis(2),() -> bookService.createBook(requestBook));
    //Asserts that execution of the supplied executable completes before the given timeout is exceeded.
}
````
If the  execution of bookService.createBook(requestBook) bigger than given timeout 2 millisecond will return false.
 
But if bookService.createBook(requestBook) execute less than 2 millisecond will return true.
* Clear everything of the test with @AfterAll
```  
@AfterAll// Running after each method execute.
public static void finished() {
   System.out.println("Finished method running.");
   bookService.clearListBook();
  }

```
