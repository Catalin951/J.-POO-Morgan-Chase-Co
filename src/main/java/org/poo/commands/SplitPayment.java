package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.graph.ExchangeGraph;
import org.poo.mapper.Mappers;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

import java.util.ArrayList;
import java.util.List;

public class SplitPayment implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;
    private final ExchangeGraph exchangeGraph;
    private final Mappers mappers;
    public SplitPayment(final CommandInput input, final User[] users,
                        final ExchangeGraph exchangeGraph, final ArrayNode output,
                        final Mappers mappers) {
        this.users = users;
        this.input = input;
        this.exchangeGraph = exchangeGraph;
        this.output = output;
        this.mappers = mappers;
    }
    public void execute() {
        List<String> splittingIBANs = input.getAccounts();
        String currency = input.getCurrency();
        ArrayList<Account> splittingAccounts = new ArrayList<>();
        ArrayNode involvedAccountsArray = new ObjectMapper().createArrayNode();

        for (String splittingIBAN : splittingIBANs) {
            involvedAccountsArray.add(splittingIBAN);
            splittingAccounts.add(mappers.getAccountForIban(splittingIBAN));
        }

        if (splittingAccounts.size() != splittingIBANs.size()) {
            // ERROR
            throw new IllegalArgumentException("Not all accounts exist");
        }

        double splitAmount = input.getAmount() / splittingAccounts.size();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("timestamp", input.getTimestamp());
        String formattedString = String.format("%.2f", input.getAmount()) + " " + input.getCurrency();
        objectNode.put("description", "Split payment of " + formattedString);
        objectNode.put("currency", input.getCurrency());
        objectNode.put("amount", splitAmount);
        objectNode.set("involvedAccounts", involvedAccountsArray);

        for (Account account : splittingAccounts) {
            String from = input.getCurrency();
            String to = account.getCurrency();
            double convertedAmount = exchangeGraph.convertCurrency(from, to, splitAmount);
            if (account.getBalance() < splitAmount) {
                //do error for all accounts
                return;
            }
            account.setBalance(account.getBalance() - convertedAmount);
            User splittingUser = mappers.getUserForAccount(account);
//            splittingUser.getTransactions()
        }
        for (Account account : splittingAccounts) {
            User splittingUser = mappers.getUserForAccount(account);
            splittingUser.getTransactions().add(objectNode);
        }
    }
}
