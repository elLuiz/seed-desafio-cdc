package br.com.elibrary.model.order;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

class CellphoneTest {
    @ParameterizedTest
    @MethodSource("provideValidCellphones")
    void shouldCreateCellPhone(Integer code, String phoneNumber, String expectedFormat) {
        Cellphone cellphone = new Cellphone(code, phoneNumber);

        Assertions.assertEquals(expectedFormat, cellphone.format());
    }

    static Stream<Arguments> provideValidCellphones() {
        return Stream.of(
                Arguments.of(34, "02029485", "(34) 0202-9485"),
                Arguments.of(34, "999991111", "(34) 9 9999-1111")
        );
    }
}