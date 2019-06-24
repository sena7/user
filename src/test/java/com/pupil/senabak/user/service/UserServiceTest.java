//package com.pupil.senabak.user.service;
//
//import com.pupil.senabak.user.entity.User;
//import com.pupil.senabak.user.exception.UserUnprocessableException;
//import org.junit.Rule;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.junit.rules.ExpectedException;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.test.context.junit.jupiter.SpringExtension;
//
//
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.Mockito.when;
//
//@ExtendWith(SpringExtension.class)
////@DataJpaTest
//public class UserServiceTest {
//
////    @MockBean
////    private UserRepository userRepository;
//
//    @Mock
//    private UserService userService;
//
////    @Rule
////    public ExpectedException thrown = ExpectedException.none();
////    @BeforeAll
////    public void init(){
////   //     userService = new UserService(userRepository);
////    }
//
//    @Test
//    public void testSaveUsernameNotUnique_DataIntegrityViolationException(){
//        //given(userRepository.save(any(User.class))).willThrow(DataIntegrityViolationException.class);
//
//       // when(userRepository.save(Mockito.any(User.class))).thenThrow(new DataIntegrityViolationException("test"));
//        //when(userRepository.userExists(Mockito.any(User.class))).thenReturn(true);
//        //when(userRepository.exists(any(User.class))).thenReturn(true);
//        User mockUser = new User(1L, "username01", "name01", "user01@email.com");
//        when(userService.usernameExists(mockUser)).thenReturn(true);
//        when(userService.emailExists(mockUser)).thenReturn(true);
//       // when(userService.usernameExists(mockUser)).thenReturn(true);
//       // when(userService.emailExists(mockUser)).thenReturn(true);
//        userService.createUser(mockUser);
//        assertThrows(UserUnprocessableException.class, () -> {
//
//        });
//
//    }
//
//}
