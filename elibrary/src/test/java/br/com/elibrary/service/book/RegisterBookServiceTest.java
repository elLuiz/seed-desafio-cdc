package br.com.elibrary.service.book;

import br.com.elibrary.model.author.Author;
import br.com.elibrary.model.category.Category;
import br.com.elibrary.service.author.AuthorRepository;
import br.com.elibrary.service.book.command.CreateBookCommand;
import br.com.elibrary.service.category.CategoryRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class RegisterBookServiceTest {
    @Mock
    AuthorRepository authorRepository;
    @Mock
    CategoryRepository categoryRepository;
    @Mock
    BookRepository repository;
    RegisterBookService registerBookService;

    @BeforeEach
    void setUp() {
        this.registerBookService = new RegisterBookService(repository, authorRepository, categoryRepository);
    }

    @Test
    void shouldRegisterBook() {
        Author author = new Author("C.S Lewis", "lewis@junit.com", "Just another great author.");
        Mockito.when(authorRepository.findById(1L))
                .thenReturn(Optional.of(author));
        Category category = new Category("RELIGION");
        Mockito.when(categoryRepository.findById(1L))
                .thenReturn(Optional.of(category));

        Mockito.doNothing().when(repository).add(Mockito.any());
        CreateBookCommand createBookCommand = CreateBookCommand.builder()
                .title("Designing Data Intensive applications")
                .summary("Do you want to build scalable and reliable software? This book is for you.")
                .tableOfContents("""
                1 - The quality of the software
                2 - Consensus
                3 - Batch Processing
                4 - Transactions
                """)
                .numberOfPages((short) 750)
                .publishAt(LocalDate.now().plusDays(10))
                .price(BigDecimal.valueOf(20.0))
                .isbn("023-92309320293")
                .authorId(1L)
                .categoryId(1L)
            .build();
        Assertions.assertNotNull(registerBookService.register(createBookCommand));
    }
}