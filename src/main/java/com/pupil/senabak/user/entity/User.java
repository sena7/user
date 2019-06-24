package com.pupil.senabak.user.entity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.pupil.senabak.user.validator.UsernameConstraint;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import java.util.Objects;

import static javax.persistence.GenerationType.SEQUENCE;

/**
 * username, user, email represent the main types of attributes frequently used in abstraction of a user model.
 * Usually, they will be part of strict validations.
 * 1. username represents an attribute that might used as the identifiers for both business and users.
 * 2. email represents an attribute used as a contact point
 * 3. name represents an attribute that might be in a free form, but shouldn't be empty.
 *
 * See the validation annotations applied above each field.
 */
@JsonIgnoreProperties(ignoreUnknown = true, value = {"hibernateLazyInitializer","handler"}) //"username", "name" User fields are included to resolve exception JsonMappingException unable to find User with id - JSonMapping serializer - made getUserId endpoint to return 200, masking the exception, seems to be JPA repository problem. so not sure
//@JsonRootName(value = "user")
@Entity
@Table(
        name="user",
        uniqueConstraints=
        @UniqueConstraint(columnNames={"username", "email"})
)
public class User {

    @Id
    @GeneratedValue(strategy = SEQUENCE)
    private Long id;

    @Column(name = "username")
    @NotEmpty(message = "{username.notEmpty}") // covers both null and empty String
    @Size(min = 2, max = 50, message = "{username.length}")
    //@UsernameConstraint // Does the same job as @Pattern
    @Pattern(regexp = "[a-z]{3}[a-z0-9]*$" , message = "{username.pattern}")
    private String username;

    @Column(name = "name")
    @NotEmpty(message = "{name.notEmpty}") // covers both null and empty String
    @Size(min = 2, max = 50, message = "{name.length}")
    @Pattern(regexp = "^([a-zA-Z]+\\s)*[a-zA-Z]+$", message = "{name.pattern}") // only alphabets with a space between
    private String name;

    @Column(name = "email")
    @Email(message = "{email.notValid}")
    @NotEmpty(message = "{email.notEmpty}")
    private String email;

    // this default constructor is required by Mockito.any(User.class)
    public User(){super();}

    public User(Long id, String username, String name, String email) {
        this.id = id;
        this.username = username;
        this.name = name;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return getId().equals(user.getId()) &&
                getUsername().equals(user.getUsername()) &&
                getName().equals(user.getName()) &&
                getEmail().equals(user.getEmail());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getUsername(), getName(), getEmail());
    }
}
