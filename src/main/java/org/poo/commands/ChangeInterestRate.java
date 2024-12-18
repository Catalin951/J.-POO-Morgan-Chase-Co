package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.fileio.CommandInput;
import org.poo.graph.ExchangeGraph;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

public final class ChangeInterestRate implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;

    public ChangeInterestRate(final CommandInput input, final User[] users,
                              final ArrayNode output) {
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
            //do smt
            return;
        }
        String Type = requestedAccount.getAccountType();
        if (Type.equals("classic")) {
            //do smt else
            return;
        }
        requestedAccount.changeInterest(input.getInterestRate());
    }
}
