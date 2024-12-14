package org.poo.commands;

import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.factories.AccountFactory;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.card.ClassicCard;
import org.poo.utils.Utils;

public final class CreateCard implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;
    public CreateCard(CommandInput input, User[] users, ArrayNode output) {
        this.users = users;
        this.input = input;
        this.output = output;
    }
    public void execute() {
        User requestedUser = null;
        for (User user : users) {
            if (user.getEmail().equals(input.getEmail())) {
                requestedUser = user;
            }
        }

        if (requestedUser == null) {
            throw new RuntimeException("User not found");
        }

        Account account = requestedUser.getAccount(input.getAccount());
        if (account == null) {
            //add transaction that : Dacă utilizatorul nu este proprietarul contului, se va adăuga o tranzacție specifică care semnalează acest lucru.
            return;
        }
        account.getCards().addFirst(new ClassicCard(Utils.generateCardNumber()));
    }
}