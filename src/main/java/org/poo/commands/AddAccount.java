package org.poo.commands;

import org.poo.factories.AccountFactory;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

public final class AddAccount implements Command {
    private final User[] users;
    private final CommandInput input;
    public AddAccount(CommandInput input, User[] users) {
        this.users = users;
        this.input = input;
    }
    public void execute() {
        User requestedUser = null;
        for (User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                requestedUser = user;
            }
        }

        if (requestedUser == null) {
            System.out.println("User " + input.getEmail() + " not found");
            System.exit(1);
        }
        Account newAccount = AccountFactory.createAccount(input);
        requestedUser.getAccounts().addFirst(newAccount);

    }
}
