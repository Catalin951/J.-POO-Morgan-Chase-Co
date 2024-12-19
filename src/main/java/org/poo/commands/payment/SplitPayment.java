package org.poo.commands.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.Command;
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
            splittingAccounts.addFirst(mappers.getAccountForIban(splittingIBAN));
        }

        if (splittingAccounts.size() != splittingIBANs.size()) {
            // ERROR
            throw new IllegalArgumentException("Not all accounts exist");
        }

        double splitAmount = input.getAmount() / splittingAccounts.size();
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("timestamp", input.getTimestamp());
        String formattedString = String.format("%.2f", input.getAmount()) + " " + input.getCurrency();
        String description = "Split payment of " + formattedString;
        objectNode.put("description", description);
        objectNode.put("currency", input.getCurrency());
        objectNode.put("amount", splitAmount);
        objectNode.set("involvedAccounts", involvedAccountsArray);

        ArrayList<Double> newBalances = new ArrayList<>();

        for (Account account : splittingAccounts) {
            String from = input.getCurrency();
            String to = account.getCurrency();
            double convertedAmount = splitAmount;
            if (!from.equals(to)) {
                convertedAmount = exchangeGraph.convertCurrency(from, to, splitAmount);
            }
            if (account.getBalance() < convertedAmount) {
                addTransactionFailure(splittingAccounts, involvedAccountsArray, splitAmount, description, account.getIBAN());
                return;
            }
            newBalances.add(account.getBalance() - convertedAmount);
        }
        int i = 0;
        for (Account account : splittingAccounts) {
            account.setBalance(newBalances.get(i));
            User splittingUser = mappers.getUserForAccount(account);
            splittingUser.getTransactions().add(objectNode);
            i++;
        }
    }
    private void addTransactionFailure(ArrayList<Account> splittingAccounts, ArrayNode involvedAccountsArray, double splitAmount, String description, String IBAN) {
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("timestamp", input.getTimestamp());
        objectNode.put("description", description);
        objectNode.put("currency", input.getCurrency());
        objectNode.put("amount", splitAmount);
        objectNode.set("involvedAccounts", involvedAccountsArray);
        objectNode.put("error", "Account " + IBAN
                + " has insufficient funds for a split payment.");
        for (Account account : splittingAccounts) {
            account.getTransactions().add(objectNode);
            mappers.getUserForAccount(account).getTransactions().add(objectNode);
        }
    }
}
