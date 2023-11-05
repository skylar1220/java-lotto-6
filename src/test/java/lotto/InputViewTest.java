package lotto;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import camp.nextstep.edu.missionutils.Console;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

public class InputViewTest {
    @AfterEach
    void closeConsole() {
        Console.close();
    }

    @DisplayName("숫자가 아닌 값을 입력하면 예외를 반환한다.")
    @ParameterizedTest
    @ValueSource(strings = {"a", "a1", "-2"})
    void check_inputPurchase_number(String rawPurchase) {
        System.setIn(createUserInput(rawPurchase));
        assertThatThrownBy(InputView::inputPurchase)
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("숫자로 입력해야합니다.");
    }
    @DisplayName("공백을 입력하면 예외를 반환한다.")
    @ParameterizedTest
    @EmptySource
    void check_inputPurchase_null_blank(String empty) {
        NumberValidator numberValidator = new NumberValidator();
        assertThatThrownBy(() -> numberValidator.validate(empty))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("공백은 입력할 수 없습니다.");
    }

    InputStream createUserInput(String input) {
        return new ByteArrayInputStream(input.getBytes());
    }
}
