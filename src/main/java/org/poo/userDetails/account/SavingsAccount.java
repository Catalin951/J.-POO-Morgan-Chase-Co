package org.poo.userDetails.account;

public class SavingsAccount extends Account {
    private final double interestRate;
    public SavingsAccount(final String currency, final String IBAN,
                          final double interestRate) {
        super(currency, IBAN);
        this.interestRate = interestRate;
    }
    public String getAccountType() {
        return "savings";
    }
}
