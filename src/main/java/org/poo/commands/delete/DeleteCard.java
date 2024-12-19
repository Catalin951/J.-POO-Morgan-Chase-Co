package org.poo.commands.delete;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.mapper.Mappers;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.card.Card;

import java.util.Map;

public final class DeleteCard implements Command {
    private final User[] users;
    private final CommandInput input;
    private final Mappers mappers;
    public DeleteCard(final CommandInput input, final User[] users, Mappers mappers) {
        this.users = users;
        this.mappers = mappers;
        this.input = input;
    }
    public void execute() {

        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                int i = 0;
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(input.getCardNumber())) {
                        objectNode.put("timestamp", input.getTimestamp());
                        objectNode.put("description", "The card has been destroyed");
                        objectNode.put("card", card.getCardNumber());
                        objectNode.put("cardHolder", user.getEmail());
                        objectNode.put("account", account.getIBAN());
                        account.getCards().remove(i);
                        // CHECK IF ONE NEEDED OR BOTH
//                        account.getTransactions().add(objectNode);

                        user.getTransactions().add(objectNode);
                        return;
                    }
                    i++;
                }
            }
        }
    }
}

//
//
//package org.poo.commands.delete;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import org.poo.commands.Command;
//import org.poo.fileio.CommandInput;
//import org.poo.mapper.Mappers;
//import org.poo.userDetails.User;
//import org.poo.userDetails.account.Account;
//import org.poo.userDetails.card.Card;
//
//public final class DeleteCard implements Command {
//    private final User[] users;
//    private final CommandInput input;
//    private final Mappers mappers;
//
//    public DeleteCard(final CommandInput input, final User[] users, Mappers mappers) {
//        this.users = users;
//        this.input = input;
//        this.mappers = mappers;
//    }
//
//    public void execute() {
//
//        ObjectNode objectNode = new ObjectMapper().createObjectNode();
//        Card requestedCard = mappers.getCardForCardNumber(input.getCardNumber());
//        if (requestedCard == null) {
//            return;
//        }
//        Account account = mappers.getAccountForCard(requestedCard);
//        if (account == null) {
//            return;
//        }
//        User user = mappers.getUserForAccount(account);
//        if (user == null) {
//            return;
//        }
//
//        int i = 0;
//        for (Card card : account.getCards()) {
//            if (card.getCardNumber().equals(input.getCardNumber())) {
//                objectNode.put("timestamp", input.getTimestamp());
//                objectNode.put("description", "The card has been destroyed");
//                objectNode.put("card", card.getCardNumber());
//                objectNode.put("cardHolder", user.getEmail());
//                objectNode.put("account", account.getIBAN());
//                account.getCards().remove(i);
//                // CHECK IF ONE NEEDED OR BOTH
//                //account.getTransactions().add(objectNode);
//                user.getTransactions().add(objectNode);
//                return;
//            }
//            i++;
//        }
//    }
//}
