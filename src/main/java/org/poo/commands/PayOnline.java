package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.graph.ExchangeGraph;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.card.Card;

import java.lang.reflect.Array;

public class PayOnline implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;
    private final ExchangeGraph exchangeGraph;
    public PayOnline(final CommandInput input, final User[] users,
                     final ExchangeGraph exchangeGraph,
                     ArrayNode output) {
        this.users = users;
        this.input = input;
        this.exchangeGraph = exchangeGraph;
        this.output = output;
    }

    public void execute() {
        User requestedUser = Execute.searchUser(input.getEmail(), users);
        if (requestedUser == null) {
            throw new IllegalArgumentException("User " + input.getEmail() + " not found");
        }

        Card requestedCard = null;
        Account requestedAccount = null;
        for (Account account : requestedUser.getAccounts()) {
            for (Card card : account.getCards()) {
                if (card.getCardNumber().equals(input.getCardNumber())) {
                    requestedCard = card;
                    requestedAccount = account;
                    break;
                }
            }
        }
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        if (requestedCard == null) {
            objectNode.put("command", "payOnline");
            ObjectNode outputNode = new ObjectMapper().createObjectNode();
            outputNode.put("timestamp", input.getTimestamp());
            outputNode.put("description", "Card not found");
            objectNode.set("output", outputNode);
            objectNode.put("timestamp", input.getTimestamp());
            output.add(objectNode);
            return;
        }
        if (requestedCard.isFrozen()) {
            ObjectNode errorNode = new ObjectMapper().createObjectNode();
            errorNode.put("description", "The card is frozen");
            errorNode.put("timestamp", input.getTimestamp());
            requestedUser.getTransactions().add(errorNode);
            return;
        }
        String from = input.getCurrency();
        String to = requestedAccount.getCurrency();
        double convertedAmount = exchangeGraph.convertCurrency(from, to, input.getAmount());
        if (requestedAccount.getBalance() - convertedAmount < 0) {
            objectNode.put("timestamp", input.getTimestamp());
            objectNode.put("description", "Insufficient funds");
            requestedUser.getTransactions().add(objectNode);
        } else {
            objectNode.put("timestamp", input.getTimestamp());
            objectNode.put("description", "Card payment");
            objectNode.put("amount", convertedAmount);
            objectNode.put("commerciant", input.getCommerciant());
            requestedUser.getTransactions().add(objectNode);
            requestedCard.subtractFromBalance(convertedAmount, requestedAccount);
        }
    }
}
