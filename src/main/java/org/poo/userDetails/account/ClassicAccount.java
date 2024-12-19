package org.poo.userDetails.account;

public final class ClassicAccount extends Account {
    public ClassicAccount(final String currency, final String IBAN) {
        super(currency, IBAN);
    }
    public String getAccountType() {
        return "classic";
    }
    public void changeInterest(final double interest) {}
    public double getInterest() {
        return 0;
    }
}
