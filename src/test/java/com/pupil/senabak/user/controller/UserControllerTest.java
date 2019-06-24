package com.pupil.senabak.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pupil.senabak.user.entity.User;
import com.pupil.senabak.user.exception.UserUnprocessableException;
import com.pupil.senabak.user.repository.UserRepository;
import com.pupil.senabak.user.service.UserService;
import org.junit.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.BindingResult;
import java.util.*;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Tests for UPDATE,  is not complete due to the time constraint.
 */
@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
public class UserControllerTest {

    private static final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService mockUserService;

    @MockBean
    private UserRepository mockUserRepository;

    @BeforeAll
    public void setup() {
    }

    @Test
    public void testGetUserById_Ok() throws Exception{
        User user = new User(1L, "username01", "name", "user01@email.com");
        when(this.mockUserRepository.findById(1L)).thenReturn(Optional.of(user));

        this.mockMvc.perform(get("/users/find/1"))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("username01")))
                .andExpect(jsonPath("$.name", is("name")));

        verify(mockUserRepository, times(1)).findById(1L);
    }

    @Test
    public void testGetAllUsers() throws Exception{
        List<User> mockedUserList = new ArrayList<>();
        mockedUserList.add(new User(1L, "username01", "name01", "user01@email.com"));
        mockedUserList.add(new User(2L, "username02", "name01", "user02@email.com"));
        when(this.mockUserRepository.findAll()).thenReturn(mockedUserList);


        this.mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].username", is("username01")))
                .andExpect(jsonPath("$[0].name", is("name01")))
                .andExpect(jsonPath("$[1].id", is(2)))
                .andExpect(jsonPath("$[1].username", is("username02")))
                .andExpect(jsonPath("$[1].name", is("name01")));

        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    public void testGetAllUsers_noContent() throws Exception{
        when(this.mockUserRepository.findAll()).thenReturn(null);

        this.mockMvc.perform(get("/users"))
                .andExpect(status().isNoContent());

        verify(mockUserRepository, times(1)).findAll();
    }

    @Test
    public void testCreateUser() throws Exception{

        User mockUser = new User(1L, "username01", "Jane Doe", "user01@email.com");
        when(this.mockUserService.createUser(Mockito.any(User.class))).thenReturn(mockUser);

        this.mockMvc.perform(post("/users/create")
                            .content(mapper.writeValueAsString(mockUser))
                            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.username", is("username01")))
                .andExpect(jsonPath("$.name", is("Jane Doe")));
    }

    @Test
    public void testCreateUser_usernameNotUnique_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, "username01", "Jane Doe", "user01@email.com");
        when(this.mockUserService.createUser(Mockito.any(User.class))).thenThrow(UserUnprocessableException.class);
        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    public void testCreateUser_usernameEmpty_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, "", "Jane Doe", "user01@email.com");
        when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("errors", hasItem("createUser.user.username: username must not be empty")))
                .andExpect(jsonPath("errors", hasItem("createUser.user.username: username must be between 2 to 50 characters")));
    }

    @Test
    public void testCreateUser_usernameNull_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, null, "Jane Doe", "user01@email.com");
        when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("errors", hasItem("createUser.user.username: username must not be empty")));
    }

    @Test
    public void testCreateUser_usernameTooShort_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, "u", "Jane Doe", "user@email.com");
        when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("errors", hasItem("createUser.user.username: username must be between 2 to 50 characters")))
        ;
    }

    @Test
    public void testCreateUser_usernameTooLong_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, "username001username001username001username001username001username001username001username001username001username001username001username001", "Jane Doe", "email.com");
        when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("errors", hasItem("createUser.user.username: username must be between 2 to 50 characters")));
    }

    @Test
    public void testCreateUser_nameNull_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, "username01", null, "user01@email.com");
        when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(mockUser);
        //when(this.mockUserRepository.userExists(Mockito.any(User.class))).thenReturn(false);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("status", is("422 (Unprocessable Entity)")))
                .andExpect(jsonPath("errors", hasItem("createUser.user.name: name must not be empty")));
    }

    @Test
    public void testCreateUser_nameEmpty_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, "username01", "", "user01@email.com");
        when(this.mockUserRepository.save(Mockito.any(User.class))).thenReturn(mockUser);

        BindingResult result = mock(BindingResult.class);
        when(result.hasErrors()).thenReturn(false);

        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("status", is("422 (Unprocessable Entity)")))
                .andExpect(jsonPath("errors", hasItem("createUser.user.name: name must not be empty")))
                .andExpect(jsonPath("errors", hasItem("createUser.user.name: name must be between 2 to 50 characters")));
    }

    @Test
    public void testCreateUser_emailInvalid_unprocessibleEntity() throws Exception{

        User mockUser = new User(1L, "username01", "Jane Doe", "invalidemail.com");

        this.mockMvc.perform(post("/users/create")
                .content(mapper.writeValueAsString(mockUser))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("status", is("422 (Unprocessable Entity)")))
                .andExpect(jsonPath("errors", hasItem("createUser.user.email: enter a valid email address")));
    }
}
