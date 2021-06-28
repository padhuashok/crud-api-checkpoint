package com.galvanize.crudapicheckpoint.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.galvanize.crudapicheckpoint.domain.User;
import com.galvanize.crudapicheckpoint.dto.UserResponseDTO;
import com.galvanize.crudapicheckpoint.repository.UserRepository;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/users")
public class UserController {

    @Autowired
    UserRepository repository;

    @GetMapping(value = "")
    public Iterable<User> getUsers(){
        return this.repository.findAll();
    }

    @PostMapping(value = "")
    public User create(@RequestBody User user){
        System.out.println("create "+user.getEmail());
        return this.repository.save(user);
    }

    @GetMapping(value = "/{id}")
    public User getById(@PathVariable Long id){
        return this.repository.findById(id).get();
    }

    @PatchMapping(value = "/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user){
        Optional<User> existingUser = this.repository.findById(id);
        if(existingUser.isPresent()) {
            existingUser.get().setEmail(user.getEmail());
            existingUser.get().setPassword(user.getPassword());
            this.repository.save(existingUser.get());
        }
        return existingUser.get();
    }

    @DeleteMapping(value = "/{id}")
    public String delete(@PathVariable Long id) throws JsonProcessingException {
        Optional<User> existingUser = this.repository.findById(id);
        if(existingUser.isPresent()) {
            this.repository.delete(existingUser.get());
        }
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode n = mapper.createObjectNode();
        n.put("count",this.repository.count());
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(n);
    }

    @PostMapping(value = "/authenticate")
    public UserResponseDTO authenticate(@RequestBody User user) {
        User existingUser = this.repository.findByEmail(user.getEmail());
        UserResponseDTO response = new UserResponseDTO();
        System.out.println("user email"+ user.getEmail());
        System.out.println("user email from db " + existingUser.getEmail());
        System.out.println("user password " + user.getPassword());
        System.out.println("user password from db "+existingUser.getPassword());
        if(existingUser != null ){
            if(existingUser.getPassword().equals(user.getPassword())){
            response.setAuthenticated(true);
            response.setUser(user);
        }else{
            response.setAuthenticated(false);
        }
    }
        return response;
}
}
