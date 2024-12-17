package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.jgrapht.*;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.*;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.card.Card;

import java.lang.reflect.Array;

public class PayOnline implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;
    private final Graph<String, DefaultWeightedEdge> exchangeGraph;
    public PayOnline(final CommandInput input, final User[] users,
                     final Graph<String, DefaultWeightedEdge> exchangeGraph,
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

        String from = input.getCurrency();
        String to = requestedAccount.getCurrency();
        double convertedAmount = convertCurrency(exchangeGraph, from, to, input.getAmount());

        if (requestedAccount.getBalance() - convertedAmount < 0) {
            System.out.println("Insufficient funds");
            // TO CHECK HOW TO DO THIS
        } else {
            requestedAccount.subtractFromBalance(convertedAmount);
        }
    }

    public double convertCurrency(Graph<String, DefaultWeightedEdge> graph, String fromCurrency, String toCurrency, double amount) {
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path = dijkstra.getPath(fromCurrency, toCurrency);

        // Step 2: Check if a path exists
        if (path == null) {
            System.out.println("No conversion path exists between " + fromCurrency + " and " + toCurrency);
            return -1;
        }

        // Step 3: Calculate the total exchange rate (product of weights on the path)
        double totalRate = 1.0;
        for (DefaultWeightedEdge edge : path.getEdgeList()) {
            double weight = graph.getEdgeWeight(edge);
            totalRate *= weight;
        }

        // Step 4: Perform the conversion
        double convertedAmount = amount * totalRate;

        // Output for debugging
        System.out.println("Path from " + fromCurrency + " to " + toCurrency + ": " + path.getVertexList());
        System.out.println("Total Rate: " + totalRate);
        System.out.println("Converted Amount: " + convertedAmount);

        return convertedAmount;
    }
}
