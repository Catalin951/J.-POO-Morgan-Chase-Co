package org.poo.factories;

import org.poo.fileio.CommandInput;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.account.ClassicAccount;
import org.poo.userDetails.account.SavingsAccount;
import org.poo.utils.Utils;

public class AccountFactory {
    public static Account createAccount(final CommandInput command) {
        String accountType = command.getAccountType();
        String IBAN = Utils.generateIBAN();
        String currency = command.getCurrency();

        return switch (accountType) {
            case "savings" -> {
                double interestRate = command.getInterestRate();
                yield new SavingsAccount(currency, IBAN, interestRate);
            }
            case "classic" -> new ClassicAccount(currency, IBAN);
            default -> throw new IllegalArgumentException("Invalid account type: " + accountType);
        };
    }
}
