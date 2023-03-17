package io.github.alexfx1.domain.validation;

import io.github.alexfx1.domain.validation.constraintValidation.NotEmptyListValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
@Constraint(validatedBy = NotEmptyListValidator.class)
public @interface NotEmptyList {
    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
    String message() default "Lista nao pode ser vazia.";
}
