package br.com.elibrary.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class Cellphone {
    private static final String PHONE_FORMAT = "(\\d)?(\\d{4})(\\d{4})";

    @Column(name = "phone_area_code", nullable = false)
    private int code;

    @Column(name = "phone_number", nullable = false)
    private String phoneNumber;

    private Cellphone() {}

    public Cellphone(Integer code, String phoneNumber) {
        this.code = code;
        this.phoneNumber = phoneNumber;
    }

    public String format() {
        return "(%d) %s".formatted(code, getFormattedNumber());
    }

    private String getFormattedNumber() {
        Pattern pattern = Pattern.compile(PHONE_FORMAT);
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.replaceAll("$1 $2-$3").trim();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cellphone cellphone = (Cellphone) o;
        return Objects.equals(code, cellphone.code) && Objects.equals(phoneNumber, cellphone.phoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(code, phoneNumber);
    }
}