package org.acme.model;

public class BetResult {
    private int betId;
    private String username;
    private double result;

    public BetResult() {
    }

    public BetResult(int betId, String username, double result) {
        this.betId = betId;
        this.username = username;
        this.result = result;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
