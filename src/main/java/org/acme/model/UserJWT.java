package org.acme.model;

public class UserJWT {
    private String username;
    private String accessToken;

    public UserJWT(String username, String jwt) {
        this.username = username;
        this.accessToken = jwt;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String jwt) {
        this.accessToken = jwt;
    }
}
