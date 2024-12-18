package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

public final class DeleteAccount implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;

    public DeleteAccount(final CommandInput input, final User[] users, final ArrayNode output) {
        this.users = users;
        this.input = input;
        this.output = output;
    }

    public void execute() {
        User requestedUser = Execute.searchUser(input.getEmail(), users);
        if (requestedUser == null) {
            System.out.println("user " + input.getEmail() + " not found for delete");
            return;
        }
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", "deleteAccount");
        ObjectNode outputNode = new ObjectMapper().createObjectNode();
        String requestedIBAN = input.getAccount();
        Account requestedAccount = null;
        for (Account account : requestedUser.getAccounts()) {
            if (account.getIBAN().equals(requestedIBAN)) {
                requestedAccount = account;
                if (requestedAccount.getBalance() != 0) {
                   outputNode.put("error", "Account couldn't be deleted - see org.poo.transactions for details");
                   outputNode.put("timestamp", input.getTimestamp());
                   objectNode.set("output", outputNode);
                   objectNode.put("timestamp", input.getTimestamp());
                   output.add(objectNode);
                   return;
                }
                break;
            }
        }
        if (requestedAccount == null) {
            throw new IllegalArgumentException("Account not found");
        }
        requestedUser.getAccounts().remove(requestedAccount);

        outputNode.put("success", "Account deleted");
        outputNode.put("timestamp", input.getTimestamp());
        objectNode.put("timestamp", input.getTimestamp());
        objectNode.set("output", outputNode);
        output.add(objectNode);
    }
}
