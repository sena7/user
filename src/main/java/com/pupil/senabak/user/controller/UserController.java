package com.pupil.senabak.user.controller;

import com.pupil.senabak.user.entity.User;
import com.pupil.senabak.user.repository.UserRepository;
import com.pupil.senabak.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import java.util.*;


/**
 * Simple errors/exceptions are handled within each controller method. They can be optimised by organised into a responseEntity wrapper
 * The others are handled in CustomResponseEntityExceptionHandler
 *
 */
@RestController()
@Validated
public class UserController {

    private final static String USER_NOT_FOUND = "There is no user with the ID";
    private final static String USER_DELETED = "User was successfully deleted";
    private final static String USER_NOT_DELETED = "User could not be deleted";


    private UserService userService;

    private UserRepository userRepository;

    public UserController(UserService userSerivce, UserRepository userRepository){
        this.userRepository = userRepository;
        this.userService = userSerivce;
    }
    /**
     *
     * @param user
     * @param bindingResult validation result attached to the user. It is required to handle ConstraintViolationException
     * @return
     */
    @PostMapping("/users/create")
    public ResponseEntity<Object> createUser(@Valid @RequestBody User user, BindingResult bindingResult) {
//
        User savedUser = userService.createUser(user);

        return Objects.nonNull(savedUser) ?
                ResponseEntity.status(HttpStatus.CREATED).body(savedUser)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    /**
     *
     * @param id
     * @return if user is found, response with the user, otherwise http status NO_CONTENT with message
     * @Min(1) will prevent non positive values. ConstraintViolationException will be handled
     */
    @GetMapping("/users/find/{id}")
    public ResponseEntity<Object> getUserById(@PathVariable() @Min(1) Long id)
    {
       Optional<User> user = userRepository.findById(id);
       return user.isPresent() ?
                ResponseEntity.status(HttpStatus.OK).body(user.get())
                : ResponseEntity.status(HttpStatus.NO_CONTENT).body(USER_NOT_FOUND);
    }

    /**
     *
     * @return if users are found, response with the users, otherwise http status NO_CONTENT with message
     */
    @GetMapping("/users")
    public ResponseEntity<Object> getAllUsers(){
        List<User> users = userRepository.findAll();

        return Objects.nonNull(users) && users.size() > 0 ?
                ResponseEntity.status(HttpStatus.OK).body(users)
                : ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     *
     * @param user
     * @param id
     * @return If user is not valid or id is not positive then response with ContraintViolationException message.
     * if user is saved, then response with http status ACCEPTED with the saved user.
     * if user couldn't be saved then response with UserUnprocessableException message
     * else (user is not found with the id) response with http status UNPROCESSABLE_ENTITY with message
     */
    @PutMapping("/users/update/{id}")
    public ResponseEntity<Object> updateUser(@Valid @RequestBody User user, @PathVariable @Min(1) Long id){
        User savedUser = userService.updateUser(user, id);
        return Objects.nonNull(savedUser) ?
                ResponseEntity.status(HttpStatus.ACCEPTED).body(savedUser)
                : ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(USER_NOT_FOUND);
    }

    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<Object> deleteUser(@PathVariable @Min(1) Long id){
        Optional<User> potentialUser = userRepository.findById(id);

        if(potentialUser.isPresent()){
            userRepository.deleteById(id);
        } else {
            return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(USER_NOT_FOUND);
        }

        return !userRepository.findById(id).isPresent() ?
                ResponseEntity.status(HttpStatus.ACCEPTED).body(USER_DELETED)
                : ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(USER_NOT_DELETED);
    }


}
