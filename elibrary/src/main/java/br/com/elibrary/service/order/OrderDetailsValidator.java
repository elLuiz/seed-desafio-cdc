package br.com.elibrary.service.order;

import br.com.elibrary.model.GenericEntity;
import br.com.elibrary.model.book.Book;
import br.com.elibrary.service.book.BookRepository;
import br.com.elibrary.service.order.command.OrderItemCommand;
import br.com.elibrary.service.order.command.RegisterOrderCommand;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class OrderDetailsValidator implements Validator {
    private final BookRepository bookRepository;

    public OrderDetailsValidator(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.isAssignableFrom(RegisterOrderCommand.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RegisterOrderCommand registerOrderCommand = (RegisterOrderCommand) target;
        if (registerOrderCommand.orderDetails() != null && registerOrderCommand.orderDetails().items() != null) {
            validateBooks(errors, registerOrderCommand);
        }
    }

    private void validateBooks(Errors errors, RegisterOrderCommand registerOrderCommand) {
        List<OrderItemCommand> items = registerOrderCommand.orderDetails().items();
        List<Long> bookIds = items.stream().map(OrderItemCommand::bookId).toList();
        List<Book> books = bookRepository.loadAllWithinIds(bookIds);
        validateAvailability(errors, books, bookIds);
        validateTotal(errors, books, registerOrderCommand);

    }

    private void validateAvailability(Errors errors, List<Book> books, List<Long> bookIds) {
        Map<Long, Book> idToBook = books
                .stream()
                .collect(Collectors.toMap(GenericEntity::getId, book -> book));
        for (int i = 0; i < bookIds.size(); i++) {
            Long bookId = bookIds.get(i);
            if (bookId != null && !idToBook.containsKey(bookId)) {
                errors.rejectValue("orderDetails.items[%d]".formatted(i), "book.does.not.exist", "book.does.not.exist");
            } else if (bookId != null) {
                Book book = idToBook.get(bookId);
                if (book != null && !book.isPublished()) {
                    errors.rejectValue("orderDetails.items[%d]".formatted(i), "book.not.available.yet", "book.not.available.yet");
                }
            }
        }
    }

    private void validateTotal(Errors errors, List<Book> books, RegisterOrderCommand registerOrderCommand) {
        if (!books.isEmpty() && !registerOrderCommand.matchesTotal(books)) {
            errors.rejectValue("orderDetails.total", "order.does.not.match.total", "order.does.not.match.total");
        }
    }
}