package org.poo.userDetails.account;

public final class SavingsAccount extends Account {
    private  double interestRate;
    public SavingsAccount(final String currency, final String IBAN,
                          final double interestRate) {
        super(currency, IBAN);
        this.interestRate = interestRate;
    }
    public String getAccountType() {
        return "savings";
    }
    public void changeInterest(final double interestRate) {
        this.interestRate = interestRate;
    }
    public double getInterest() {
        return interestRate;
    }
}
