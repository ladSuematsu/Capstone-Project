package com.ladsuematsu.capstoneproject.core.entity;

public class UserAuth {

    private final String username;
    private final String email;

    public UserAuth(String username, String email) {
        this.username = username;
        this.email = email;
    }

    public boolean isValid() {
        return username != null
                && !username.isEmpty()
                && email != null
                && !email.isEmpty();
    }

}
