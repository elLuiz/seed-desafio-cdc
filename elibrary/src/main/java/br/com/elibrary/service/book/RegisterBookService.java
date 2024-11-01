package br.com.elibrary.service.book;

import br.com.elibrary.model.author.Author;
import br.com.elibrary.model.book.Book;
import br.com.elibrary.model.category.Category;
import br.com.elibrary.service.author.AuthorRepository;
import br.com.elibrary.service.book.command.CreateBookCommand;
import br.com.elibrary.service.category.CategoryRepository;
import br.com.elibrary.service.exception.EntityNotFound;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegisterBookService {
    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;
    private final CategoryRepository categoryRepository;

    public RegisterBookService(BookRepository bookRepository,
                               AuthorRepository authorRepository,
                               CategoryRepository categoryRepository) {
        this.bookRepository = bookRepository;
        this.authorRepository = authorRepository;
        this.categoryRepository = categoryRepository;
    }

    @Transactional
    public Book register(CreateBookCommand createBookCommand) {
        Author author = authorRepository.findById(createBookCommand.getAuthorId()).orElseThrow(() -> new EntityNotFound("author.not.found"));
        Category category = categoryRepository.findById(createBookCommand.getCategoryId()).orElseThrow(() -> new EntityNotFound("category.not.found"));
        Book book = Book.builder()
                .title(createBookCommand.getTitle())
                .summary(createBookCommand.getSummary())
                .tableOfContents(createBookCommand.getTableOfContents())
                .pages(createBookCommand.getNumberOfPages())
                .publishAt(createBookCommand.getPublishAt())
                .price(createBookCommand.getPrice())
                .isbn(createBookCommand.getIsbn())
                .author(author)
                .category(category)
                .build();
        bookRepository.add(book);
        return book;
    }
}