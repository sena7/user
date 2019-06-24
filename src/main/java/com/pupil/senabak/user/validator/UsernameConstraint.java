package com.pupil.senabak.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UsernameValidator.class)
@Target( { ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface UsernameConstraint {
    String message() default "Username should only contain lowercase alphabets and number, and begin with at least three lowercase alphabets";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
