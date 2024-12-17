package org.poo.userDetails.account;

public class ClassicAccount extends Account {
    public ClassicAccount(final String currency, final String IBAN) {
        super(currency, IBAN);
    }
    public String getAccountType() {
        return "classic";
    }
}
