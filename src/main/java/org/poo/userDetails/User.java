package org.poo.userDetails;

import org.poo.fileio.UserInput;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.poo.userDetails.account.Account;

import java.util.ArrayList;

@Data
public final class User {
    private final String firstName;
    private final String lastName;
    private final String email;
    private final ArrayList<Account> accounts;
    public User(final UserInput userInput) {
        firstName = userInput.getFirstName();
        lastName = userInput.getLastName();
        email = userInput.getEmail();
        accounts = new ArrayList<>();
    }
    public Account getAccount(final String IBAN) {
        for (Account account : accounts) {
            if (account.getIBAN().equals(IBAN)) {
                return account;
            }
        }
        return null;
    }
}
