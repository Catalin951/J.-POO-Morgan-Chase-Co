package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

public class SetAlias implements Command {
    private final User[] users;
    private final CommandInput input;
    public SetAlias(final CommandInput input, final User[] users) {
        this.users = users;
        this.input = input;
    }
    public void execute() {
        User requestedUser = Execute.searchUser(input.getEmail(), users);
        if (requestedUser == null) {
            throw new IllegalArgumentException("User not found in SetAlias");
        }
        for(Account account : requestedUser.getAccounts()) {
            if(account.getIBAN().equals(input.getAccount())) {
                requestedUser.setAlias(input.getAlias(), account);
                break;
            }
        }
    }
}
