package br.com.elibrary.model.order;

import br.com.elibrary.model.exception.DomainException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;


/**
 * Class responsible for managing CPF (A brazilian document for citizens).
 */
@Embeddable
public class Document {
    @Column(name = "document_value", nullable = false)
    protected String value;
    @Column(name = "type", nullable = false)
    private String type;

    protected Document() {}

    protected Document(String value) {
        this.value = value;
    }

    public Document(String value, String type) {
        this.value = value;
        this.type = type;
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

    public String format() {
        if ("cpf".equals(type)) {
            return ((CPF) this).format();
        } else {
            return ((CNPJ) this).format();
        }
    }

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