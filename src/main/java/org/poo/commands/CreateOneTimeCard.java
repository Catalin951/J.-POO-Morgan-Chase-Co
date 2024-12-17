package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.card.OneTimeCard;
import org.poo.utils.Utils;

public final class CreateOneTimeCard implements Command {
    private final User[] users;
    private final CommandInput input;
    public CreateOneTimeCard(final CommandInput input, final User[] users) {
        this.users = users;
        this.input = input;
    }
    public void execute() {
        User requestedUser = Execute.searchUser(input.getEmail(), users);

        if (requestedUser == null) {
            throw new RuntimeException("User not found");
        }

        Account account = requestedUser.getAccount(input.getAccount());
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("timestamp", input.getTimestamp());
        if (account == null) {
            objectNode.put("description", "User isn't owner");
            requestedUser.getTransactions().add(objectNode);
            return;
        }
        String cardNumber = Utils.generateCardNumber();

        objectNode.put("description", "new card created");
        objectNode.put("card", cardNumber);
        objectNode.put("cardHolder", requestedUser.getEmail());
        objectNode.put("account", account.getIBAN());

        requestedUser.getTransactions().add(objectNode);
        account.getTransactions().add(objectNode);

        account.getCards().addLast(new OneTimeCard(cardNumber));
    }
}
