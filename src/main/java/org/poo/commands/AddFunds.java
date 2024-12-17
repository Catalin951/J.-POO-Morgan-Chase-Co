package org.poo.commands;

import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

public class AddFunds {
    private final User[] users;
    private final CommandInput input;
    public AddFunds(final CommandInput input, final User[] users) {
        this.users = users;
        this.input = input;
    }
    public void execute() {
        String IBAN = input.getAccount();
        Account account = null;
        for (User user : users) {
            Account accountIteration = user.getAccount(IBAN);
            if (accountIteration != null) {
                account = accountIteration;
                break;
            }
        }
        if (account == null) {
            throw new IllegalArgumentException("Account not found");
        }
        account.addToBalance(input.getAmount());
    }
}
