package org.acme.model;

public class BetResult {
    private int betId;
    private String userEmail;
    private double result;

    public BetResult() {
    }

    public BetResult(int betId, String userEmail, double result) {
        this.betId = betId;
        this.userEmail = userEmail;
        this.result = result;
    }

    public int getBetId() {
        return betId;
    }

    public void setBetId(int betId) {
        this.betId = betId;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
