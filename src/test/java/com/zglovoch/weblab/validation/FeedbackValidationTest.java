package com.zglovoch.weblab.validation;

import com.zglovoch.weblab.model.FeedbackMessage;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Тесты Bean Validation для FeedbackMessage.
 * Проверяет аннотации @NotBlank, @Email, @Size без запуска Spring-контекста.
 */
class FeedbackValidationTest {

    private static Validator validator;

    @BeforeAll
    static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    private FeedbackMessage validMessage() {
        FeedbackMessage msg = new FeedbackMessage();
        msg.setName("Иван Петров");
        msg.setEmail("ivan@example.com");
        msg.setMessage("Отличное кафе, буду рекомендовать всем друзьям!");
        return msg;
    }

    @Test
    @DisplayName("Корректное сообщение проходит валидацию без ошибок")
    void validMessage_noViolations() {
        Set<ConstraintViolation<FeedbackMessage>> violations =
                validator.validate(validMessage());

        assertThat(violations).isEmpty();
    }

    @Test
    @DisplayName("Пустое имя — ошибка валидации")
    void blankName_causesViolation() {
        FeedbackMessage msg = validMessage();
        msg.setName("");

        Set<ConstraintViolation<FeedbackMessage>> violations = validator.validate(msg);

        assertThat(violations).anyMatch(v -> v.getPropertyPath().toString().equals("name"));
    }

    @Test
    @DisplayName("Некорректный email — ошибка валидации")
    void invalidEmail_causesViolation() {
        FeedbackMessage msg = validMessage();
        msg.setEmail("not-an-email");

        Set<ConstraintViolation<FeedbackMessage>> violations = validator.validate(msg);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Пустой email — ошибка валидации")
    void blankEmail_causesViolation() {
        FeedbackMessage msg = validMessage();
        msg.setEmail("");

        Set<ConstraintViolation<FeedbackMessage>> violations = validator.validate(msg);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("email"));
    }

    @Test
    @DisplayName("Слишком короткое сообщение (< 10 символов) — ошибка валидации")
    void tooShortMessage_causesViolation() {
        FeedbackMessage msg = validMessage();
        msg.setMessage("Коротко");   // 7 символов

        Set<ConstraintViolation<FeedbackMessage>> violations = validator.validate(msg);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("message"));
    }

    @Test
    @DisplayName("Пустое сообщение — ошибка валидации")
    void blankMessage_causesViolation() {
        FeedbackMessage msg = validMessage();
        msg.setMessage("");

        Set<ConstraintViolation<FeedbackMessage>> violations = validator.validate(msg);

        assertThat(violations)
                .anyMatch(v -> v.getPropertyPath().toString().equals("message"));
    }

    @Test
    @DisplayName("Телефон не обязателен — null телефон не вызывает ошибки")
    void nullPhone_noViolation() {
        FeedbackMessage msg = validMessage();
        msg.setPhone(null);

        Set<ConstraintViolation<FeedbackMessage>> violations = validator.validate(msg);

        assertThat(violations)
                .noneMatch(v -> v.getPropertyPath().toString().equals("phone"));
    }

    @Test
    @DisplayName("Сообщение ровно 10 символов — граничное значение, ошибок нет")
    void messageTenChars_noViolation() {
        FeedbackMessage msg = validMessage();
        msg.setMessage("1234567890");   // ровно 10

        Set<ConstraintViolation<FeedbackMessage>> violations = validator.validate(msg);

        assertThat(violations)
                .noneMatch(v -> v.getPropertyPath().toString().equals("message"));
    }
}
