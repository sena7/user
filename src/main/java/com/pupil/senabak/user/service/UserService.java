package com.pupil.senabak.user.service;

import com.pupil.senabak.user.entity.User;
import com.pupil.senabak.user.exception.UserUnprocessableException;
import com.pupil.senabak.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * implementation of the rules required beyond what simple repository methods are able to handle
 */
@Service
public class UserService {

    private UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    /**
     * implementation of business logic to determine the new user is unique.
     *
     * @param user
     * @return if any existing user is found with username OR email, return true, otherwise false.
     */
    public User createUser(User user){

        if(usernameExists(user) || emailExists(user)){

            throw new UserUnprocessableException("username or email is used by other user");
        }

        return userRepository.save(user);
    }

    /**
     *
     * @param user
     * @return true if username is found, otherwise false
     */
    public boolean usernameExists(User user){
        User example = new User();
        example.setUsername(user.getUsername());

        return userRepository.userExists(example);
    }

    /**
     *
     * @param user
     * @return true if email is found, otherwise false
     */
    public boolean emailExists(User user){
        User example = new User();
        example.setUsername(user.getUsername());

        return userRepository.userExists(example);
    }

    /**
     * implementation of business logic to determine the user update is acceptable
     * @param user
     * @param id
     * @return if updaed is allowed then save the user and return it. otherwise, null
     * @throws UserUnprocessableException with message
     */
    public User updateUser(User user, Long id) throws UserUnprocessableException{

        Optional<User> potentialUser = userRepository.findById(id);

        if(potentialUser.isPresent()){
            User oldUser = potentialUser.get();
            boolean userIdMatch = oldUser.getId().equals(user.getId());
            boolean userNameNotChanged = oldUser.getUsername().equals(user.getUsername());
            boolean emailNotChanged = oldUser.getEmail().equals(user.getEmail());
            boolean emailUnique = !emailExists(user);

            boolean isValidUser = (userIdMatch && userNameNotChanged);
            boolean updateAllowed = isValidUser && (emailNotChanged || emailUnique);


            if(updateAllowed){

                return userRepository.save(user);

            }else {
                String message;
                if(!isValidUser){
                    message = "User id does not match or username was changed";
                }else{
                    message = "Email belongs to another user";
                }

                throw new UserUnprocessableException(message);
            }
        }

        return null; // when user if not found with the id from path parameter
    }
}
