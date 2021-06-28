package com.galvanize.crudapicheckpoint.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.galvanize.crudapicheckpoint.domain.User;
import com.galvanize.crudapicheckpoint.repository.UserRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

import java.io.File;
import java.nio.file.Paths;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTest {
    @Autowired
    MockMvc mvc;

    @Autowired
    UserRepository repository;

    User u = new User();

    @Test
    public void testGetUsers() throws Exception{
        u.setEmail("abc@gmail.com");
        repository.save(u);
        this.mvc.perform(get("/users").
                accept(MediaType.APPLICATION_JSON)).
                andExpect(status().isOk()).
                andExpect(jsonPath("[0]").exists()).
                andExpect(jsonPath("[0].email", is("abc@gmail.com")));
    }

    @Test @Transactional
    @Rollback
    public void testCreateUsers() throws Exception{
        User u = new User();
        u.setEmail("abc@gmail.com");
        u.setPassword("test");
        this.mvc.perform(post("/users").
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                content(new ObjectMapper().writeValueAsString(u))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.id").exists()).
                andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test @Transactional @Rollback
    public void testGetUsersById() throws Exception{
        u.setEmail("abc@gmail.com");
        u.setPassword("test");
        repository.save(u);
        this.mvc.perform(get(String.format("/users/%d",u.getId()))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.email").value(u.getEmail()));

    }

    @Test @Transactional @Rollback
    public void testUpdateUser() throws Exception{
        String updateEmail="newemail@xyz.com";
        u.setEmail("abc@gmail.com");
        u.setPassword("test");
        repository.save(u);
        u.setEmail(updateEmail);
        this.mvc.perform(patch("/users/1").
                accept(MediaType.APPLICATION_JSON).
                contentType(MediaType.APPLICATION_JSON).
                content(new ObjectMapper().writeValueAsString(u))).
                andExpect(status().isOk()).
                andExpect(jsonPath("$.email",is(updateEmail))).
                andExpect(jsonPath("$.password").doesNotExist());
    }

    @Test @Transactional @Rollback
    public void deleteUser() throws Exception {
        List<User> users = new ObjectMapper().
                readValue((new String(this.getClass().getResourceAsStream("/static/test-data.json").readAllBytes())),new TypeReference<List<User>>(){});
        this.repository.saveAll(users);
        this.mvc.perform(delete("/users/2")).andExpect(jsonPath("$.count").exists());
    }

    @Test @Transactional @Rollback
    public void testAuthenticate() throws Exception{
        u.setEmail("abc@gmail.com");
        u.setPassword("test");
        repository.save(u);
        this.mvc.perform(post("/users/authenticate").
                contentType(MediaType.APPLICATION_JSON).
                content(new ObjectMapper().writeValueAsString(u)).
                accept(MediaType.APPLICATION_JSON)).andExpect(jsonPath("$.authenticated",is(true)));
    }

    private String getJSON(String path) throws Exception {
        return new String (this.getClass().getResourceAsStream(path).readAllBytes());
    }

}
