package org.poo.commands;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.graph.ExchangeGraph;
import org.poo.userDetails.User;

public class PrintTransactions implements Command {
    private final User[] users;
    private final CommandInput input;
    private final ArrayNode output;
    public PrintTransactions(final CommandInput input, final User[] users, ArrayNode output) {
        this.users = users;
        this.input = input;
        this.output = output;
    }
    public void execute() {
        User requestedUser = Execute.searchUser(input.getEmail(), users);
        if (requestedUser == null) {
            throw new IllegalArgumentException("User not found");
        }
        ObjectNode objectNode = new ObjectMapper().createObjectNode();
        objectNode.put("command", "printTransactions");
        objectNode.set("output", requestedUser.getTransactions().deepCopy());
        objectNode.put("timestamp", input.getTimestamp());
        output.add(objectNode);
    }
}
