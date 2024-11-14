package br.com.elibrary.model.order;

import br.com.elibrary.model.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.Embeddable;

import java.util.Objects;


/**
 * Class responsible for managing CPF (A brazilian document for citizens).
 */
@Embeddable
@DiscriminatorColumn(name = "type")
public abstract class Document {
    @Column(name = "document")
    protected String value;

    private Document() {}

    protected Document(String value) {
        this.value = value;
    }

    public static Document create(String value) {
        if (isInvalid(value)) {
            throw new DomainException("invalid.document.format");
        } else if (value.length() == 11) {
            return new CPF(value);
        } else {
            return new CNPJ(value);
        }
    }

    private static boolean isInvalid(String value) {
        return value == null ||
                value.length() < 11 ||
                value.length() > 14 ||
                !value.matches("\\d{11,14}");
    }

    public abstract String format();

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Document document = (Document) o;
        return Objects.equals(value, document.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }
}