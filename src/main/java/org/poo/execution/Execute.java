package org.poo.execution;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import org.poo.commands.AddFunds;
import org.poo.commands.CreateCard;
import org.poo.commerciants.CommerciantType;
import org.poo.exchange.Exchange;
import org.poo.factories.AccountFactory;
import org.poo.fileio.CommandInput;
import org.poo.userDetails.User;
import org.poo.userDetails.account.Account;
import org.poo.commands.AddAccount;
import java.util.ArrayList;

public final class Execute {
    private final ArrayNode output;
    private final ObjectMapper mapper;
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
        mapper = new ObjectMapper();
    }

    public void Execution() {
        for (CommandInput command : commands) {
            switch (command.getCommand()) {
                case "addAccount":
                    new AddAccount(command, users).execute();
                    break;
                case "addFunds":
                    new AddFunds(command, users).execute();
                    break;
                case "createCard":
                    new CreateCard(command, users, output).execute();
                    break;
            }
        }
    }
}

