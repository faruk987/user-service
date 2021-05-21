package org.acme.model;

public class UserWallet {
    private String username;
    private double wallet;

    public UserWallet() {
    }

    public UserWallet(String username, double wallet) {
        this.username = username;
        this.wallet = wallet;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getWallet() {
        return wallet;
    }

    public void setWallet(double wallet) {
        this.wallet = wallet;
    }
}
