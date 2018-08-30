package com.tenpearls.auth.entities;

/**
 * Class to contain credentials.
 */
public class Credentials {

    private final String username;
    private final String password;

    /**
     * Creates a credential object with username and password.
     * @param username Username of the user.
     * @param password Password of the user.
     */
    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns username of the credentials.
     * @return Username.
     */
    public String getUsername() {
        return username;
    }


    /**
     * Returns password of the credentials.
     * @return Password.
     */
    public String getPassword() {
        return password;
    }
}
