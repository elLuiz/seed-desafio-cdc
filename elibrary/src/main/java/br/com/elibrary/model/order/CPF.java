package br.com.elibrary.model.order;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Embeddable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
@DiscriminatorValue("cpf")
public class CPF extends Document {
    CPF(String value) {
        super(value);
    }

    /**
     * Converts the raw CPF value into an already known format (xxx.xxx.xxx-xx)
     * @return A formatted CNPJ
     */
    @Override
    public String format() {
        Pattern pattern = Pattern.compile("(\\d{3})(\\d{3})(\\d{3})(\\d{2})");
        Matcher matcher = pattern.matcher(value);
        return matcher.replaceAll("$1.$2.$3-$4");
    }
}