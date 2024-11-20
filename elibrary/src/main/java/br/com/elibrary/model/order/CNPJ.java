package br.com.elibrary.model.order;

import jakarta.persistence.Embeddable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class responsible for managing CNPJ (A brazilian document for enterprises).
 */
@Embeddable
public class CNPJ extends Document {
    CNPJ(String value) {
        super(value, "cnpj");
    }

    /**
     * Converts the raw CNPJ value into an already known format (xx.xxx.xxx/xxxx-xx)
     * @return A formatted CNPJ
     */
    @Override
    public String format() {
        Pattern pattern = Pattern.compile("(\\d{2})(\\d{3})(\\d{3})(\\d{4})(\\d{2})");
        Matcher matcher = pattern.matcher(value);
        return matcher.replaceAll("$1.$2.$3/$4-$5");
    }
}