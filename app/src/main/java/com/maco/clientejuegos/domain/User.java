package com.maco.clientejuegos.domain;

/**
 * Created by Maco on 23/2/16.
 */
public class User {
    private final String email;
    private final int idUser;

    public User(String email, int idUser) {
        this.email=email;
        this.idUser=idUser;
    }

    public String getEmail() {
        return email;
    }

    public int getIdUser() {
        return idUser;
    }
}
