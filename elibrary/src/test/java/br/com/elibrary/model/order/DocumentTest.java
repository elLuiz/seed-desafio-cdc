package br.com.elibrary.model.order;

import br.com.elibrary.model.exception.DomainException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

class DocumentTest {
    @ParameterizedTest(name = "Should not create document with invalid format")
    @ValueSource(strings = {"", "1029383838292929", "A109Aaa", "  "})
    void shouldNotCreateDocumentWithInvalidSize() {
        Assertions.assertThrows(DomainException.class, () -> Document.create(""), "invalid.document.format");
    }

    @Test
    void shouldCreateCPF() {
        Document document = Document.create("10385744400");

        Assertions.assertInstanceOf(CPF.class, document);
        Assertions.assertEquals("103.857.444-00", document.format());
    }

    @Test
    void shouldCreateCNPJ() {
        Document document = Document.create("01000000111133");

        Assertions.assertInstanceOf(CNPJ.class, document);
        Assertions.assertEquals("01.000.000/1111-33", document.format());
    }
}