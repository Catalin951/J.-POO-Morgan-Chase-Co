package org.poo.execution;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.*;
import org.poo.commerciants.CommerciantType;
import org.poo.exchange.Exchange;
import org.poo.factories.AccountFactory;
import org.poo.fileio.CommandInput;
import org.poo.graph.ExchangeGraph;
import org.poo.mapper.Mappers;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.userDetails.card.Card;

import java.util.ArrayList;
import java.util.Map;

public final class Execute {
    private final ArrayNode output;
    private final User[] users;
    private final Exchange[] exchanges;
    private final ArrayList<CommerciantType> commerciantTypes;
    private final CommandInput[] commands;

    public Execute(final ArrayNode output, final User[] users, final Exchange[] exchanges, final CommandInput[] commands) {
        this.output = output;
        this.users = users;
        this.exchanges = exchanges;
        this.commerciantTypes = new ArrayList<>();
        this.commands = commands;
    }

    public void execution() {
        ExchangeGraph exchangeGraph = new ExchangeGraph(exchanges);
        Mappers mappers = new Mappers();
        for (User user : users) {
            mappers.addEmailToUser(user.getEmail(), user);
        }
        for (CommandInput command : commands) {
            switch (command.getCommand()) {
                case "printUsers":
                    new PrintUsers(command, users, output).execute();
                    break;
                case "printTransactions":
                    new PrintTransactions(command, users, output).execute();
                    break;
                case "addAccount":
                    new AddAccount(command, mappers).execute();
                    break;
                case "addFunds":
                    new AddFunds(command, mappers).execute();
                    break;
                case "createCard":
                    new CreateCard(command, mappers).execute();
                    break;
                case "createOneTimeCard":
                    new CreateOneTimeCard(command, mappers).execute();
                    break;
                case "deleteAccount":
                    new DeleteAccount(command, users, output).execute();
                    break;
                case "deleteCard":
                    new DeleteCard(command, users).execute();
                    break;
                case "payOnline":
                    new PayOnline(command, users, exchangeGraph, output).execute();
                    break;
                case "sendMoney":
                    new SendMoney(command, users, exchangeGraph, output).execute();
                    break;
                case "setAlias":
                    new SetAlias(command, users).execute();
                    break;
                case "setMinimumBalance":
                    new SetMinBalance(command, users, output).execute();
                    break;
                case "checkCardStatus":
                    new CheckCardStatus(command, users, output).execute();
                    break;
                case "changeInterestRate":
                    new ChangeInterestRate(command, users, output).execute();
                    break;
                case "splitPayment":
                    new SplitPayment(command, users, exchangeGraph, output, mappers).execute();
                    break;
                default:
                    break;
            }
        }
    }
    public static User searchUser(final String email, final User[] users) {
        User requestedUser = null;
        for (User user : users) {
            if (user.getEmail().equals(email)) {
                requestedUser = user;
            }
        }
        return requestedUser;
    }

    public static Account searchAccount(final String IBAN, final ArrayList<Account> accounts) {
        Account requestedAccount = null;
        for (Account account : accounts) {
            if (account.getIBAN().equals(IBAN)) {
                requestedAccount = account;
            }
        }
        return requestedAccount;
    }

    public static Account searchAccountWithCard(final String cardNumber, final User[] users) {
        for (User user : users) {
            for (Account account : user.getAccounts()) {
                for (Card card : account.getCards()) {
                    if (card.getCardNumber().equals(cardNumber)) {
                       return account;
                    }
                }
            }
        }
        return null;
    }
}

