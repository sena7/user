package com.pupil.senabak.user.repository;

import com.pupil.senabak.user.entity.User;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.support.ExampleMatcherAccessor;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.domain.ExampleMatcher.GenericPropertyMatchers.ignoreCase;

/**
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     *
     * @param user template of user to find the matching records in database
     * @return true if matching records found, otherwise false
     */
    default boolean userExists(User user){
        List<String> userFieldNames = Arrays.stream(User.class.getDeclaredFields()).map(Field::getName).collect(Collectors.toList());

        userFieldNames.forEach(System.out::println);

        ExampleMatcher modelMatcher = ExampleMatcher.matching()
                .withIgnoreNullValues()
                .withIgnorePaths("id");

        userFieldNames.forEach(fieldName -> modelMatcher.withIgnoreCase(fieldName));

        Example<User> example = Example.of(user, modelMatcher);
        return this.exists(example);
    }

}
