package br.com.elibrary.service.order;

import br.com.elibrary.model.book.Book;
import br.com.elibrary.model.common.Money;
import br.com.elibrary.model.exception.DomainException;
import br.com.elibrary.model.order.OrderItem;
import br.com.elibrary.service.book.BookRepository;
import br.com.elibrary.service.order.command.OrderItemCommand;
import br.com.elibrary.service.order.command.RegisterOrderCommand;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class OrderItemMapper implements Converter<RegisterOrderCommand, Set<OrderItem>> {
    private final BookRepository bookRepository;

    public OrderItemMapper(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Converts an {@link br.com.elibrary.service.order.command.OrderDetailsCommand} into the {@link OrderItem} domain class.
     * <pre>For each bookId, the method retrieves the book's price from the database.</pre>
     * @param source The order command
     * @return A set of order items
     */
    @Override
    public Set<OrderItem> convert(RegisterOrderCommand source) {
        if (source.orderDetails() == null || source.orderDetails().items() == null || source.orderDetails().items().isEmpty()) {
            throw new DomainException("order.details.must.not.be.null");
        }
        Map<Long, Money> bookIdToPrice = bookRepository.loadAllWithinIds(source.orderDetails().items().stream().map(OrderItemCommand::bookId).toList())
                .stream()
                .collect(Collectors.toMap(Book::getId, Book::getPrice));
        return source.orderDetails().items()
                .stream()
                .map(orderItemCommand -> new OrderItem(orderItemCommand.bookId(), orderItemCommand.quantity(), bookIdToPrice.get(orderItemCommand.bookId())))
                .collect(Collectors.toSet());
    }
}
