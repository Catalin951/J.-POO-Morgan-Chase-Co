package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

import java.sql.SQLOutput;

public final class SetMinBalance implements Command{
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;

    public SetMinBalance(final CommandInput input, final User[] users, final ArrayNode output) {
        this.users = users;
        this.input = input;
        this.output = output;
    }

    public void execute() {
        Account requestedAccount = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(input.getAccount())) {
                    requestedAccount = account;
                }
            }
        }
        if (requestedAccount == null) {
            // ADD ERROR
            System.out.println("Account not found in SETMINBALANCE");
            return;
        }
        requestedAccount.setMinBalance(input.getAmount());
    }
}
