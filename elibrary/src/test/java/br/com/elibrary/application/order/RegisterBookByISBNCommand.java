package br.com.elibrary.application.order;

/**
 * Utility class to create books by ISBN.
 * When using parametrized tests, it is not possible to use non-static dependencies, so this class
 * was created to make it possible to register many books orders in a single request.
 */
public record RegisterBookByISBNCommand(String isbn, Integer quantity) {
}
