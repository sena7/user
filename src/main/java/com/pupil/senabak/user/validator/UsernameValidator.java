package com.pupil.senabak.user.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UsernameValidator implements ConstraintValidator<UsernameConstraint, String> {

    @Override
    public void initialize(UsernameConstraint username) {
    }

    /**
     *
     * @param username
     * @param cxt
     * @return true if username only contains lowercase alphabets and numbers, andstart with at least 3 alphabets. Otherwise false
     */
    @Override
    public boolean isValid(String username, ConstraintValidatorContext cxt) {
        return username != null && username.matches("[a-z]{3}[a-z0-9]*$");
    }
}


