package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.execution.Execute;
import org.poo.factories.AccountFactory;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

public final class AddAccount implements Command {
    private final User[] users;
    private final CommandInput input;

    public AddAccount(final CommandInput input, final User[] users) {
        this.users = users;
        this.input = input;
    }

    public void execute() {
        User requestedUser = Execute.searchUser(input.getEmail(), users);

        if (requestedUser == null) {
            System.out.println("User " + input.getEmail() + " not found");
            System.exit(1);
        }
        Account newAccount = AccountFactory.createAccount(input);

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("timestamp", input.getTimestamp());
        objectNode.put("description", "New account created");

        // CHECk IF THIS HAS TO BE ADDDED TOO
//        newAccount.getTransactions().add(objectNode);
        requestedUser.getTransactions().add(objectNode);

        requestedUser.getAccounts().addLast(newAccount);
    }
}
