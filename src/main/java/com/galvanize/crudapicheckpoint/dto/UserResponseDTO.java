package com.galvanize.crudapicheckpoint.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.galvanize.crudapicheckpoint.domain.User;

@JsonInclude(value = JsonInclude.Include.NON_EMPTY, content = JsonInclude.Include.NON_NULL)
public class UserResponseDTO {
    private boolean authenticated;
    private User user;

    public UserResponseDTO() {

    }

    public boolean isAuthenticated() {
        return authenticated;
    }
    public UserResponseDTO(User user){
        this.user = user;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
