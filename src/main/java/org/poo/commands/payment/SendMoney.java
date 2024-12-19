package org.poo.commands.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.commands.Command;
import org.poo.fileio.CommandInput;
import org.poo.graph.ExchangeGraph;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;

public class SendMoney implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;
    private final ExchangeGraph exchangeGraph;
    public SendMoney(final CommandInput input, final User[] users,
                     final ExchangeGraph exchangeGraph,
                     ArrayNode output) {
        this.users = users;
        this.input = input;
        this.exchangeGraph = exchangeGraph;
        this.output = output;
    }
    public void execute() {
        User payer = null;
        Account payerAccount = null;
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(input.getAccount())) {
                    payer = user;
                    payerAccount = account;
                    break;
                }
            }
            if (payer != null) {
                break;
            }
        }

        User receiver = null;
        Account receiverAccount = null;
        for (User user : users) {
            Account mappedAccount = user.getAccountFromAlias(input.getAccount());
            if (mappedAccount != null) {
                receiver = user;
                receiverAccount = mappedAccount;
                break;
            }
            for (Account account : user.getAccounts()) {
                if (account.getIBAN().equals(input.getReceiver())) {
                    receiver = user;
                    receiverAccount = account;
                    break;
                }
            }
            if (receiver != null) {
                break;
            }
        }
        if (payer == null || receiver == null) {
            System.out.println("the account " + input.getAccount() + " not found SENDMONEY" + input.getTimestamp());
            return;
        }
        String from = payerAccount.getCurrency();
        String to = receiverAccount.getCurrency();
        double convertedAmount = input.getAmount();
        if (!from.equals(to)) {
            convertedAmount = exchangeGraph.convertCurrency(from, to, input.getAmount());
        }
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        if (payerAccount.getBalance() - input.getAmount() < 0) {
            objectNode.put("timestamp", input.getTimestamp());
            objectNode.put("description", "Insufficient funds");
            payer.getTransactions().add(objectNode);
            payerAccount.getTransactions().add(objectNode);
        } else {
            objectNode.put("timestamp", input.getTimestamp());
            objectNode.put("description", input.getDescription());
            objectNode.put("senderIBAN", payerAccount.getIBAN());
            objectNode.put("receiverIBAN", receiverAccount.getIBAN());
            objectNode.put("amount", input.getAmount() + " " + from);
            objectNode.put("transferType", "sent");
            payer.getTransactions().add(objectNode);

            ObjectNode receiverNode = objectNode.deepCopy();
            receiverNode.put("transferType", "received");
            receiverNode.put("amount", convertedAmount + " " + to);
            receiver.getTransactions().add(receiverNode);
            receiverAccount.getTransactions().add(receiverNode);

            payerAccount.getTransactions().add(objectNode);
            payerAccount.setBalance(payerAccount.getBalance() - input.getAmount());
            receiverAccount.setBalance(receiverAccount.getBalance() + convertedAmount);
        }
    }
}
