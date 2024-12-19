package org.poo.commands.interest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.execution.Execute;
import org.poo.fileio.CommandInput;
import org.poo.mapper.Mappers;
import org.poo.userDetails.account.Account;

public final class AddInterest {
    private CommandInput input;
    private final Mappers mappers;
    private final ArrayNode output;
    public AddInterest(final CommandInput input, final ArrayNode output, final Mappers mappers) {
        this.input = input;
        this.mappers = mappers;
        this.output = output;
    }
    public void execute() {
        Account account = mappers.getAccountForIban(input.getAccount());
        // CHANGE POLIMORPHISM, ADD A METHOD THAT DOES IN CLASSIC RETURN ERROR
        // AND IN THE OTHER (SAVINGS) IT ACTUALLY DOES IT, CHECK ELSEWHERE THIS MIGHT APPLY TO
        if (account == null) {
            output.add(Execute.makeGeneralError("addInterest",
                    "Account not found",
                    input.getTimestamp()));
            return;
        }
        if (!account.getAccountType().equals("Savings")) {
            output.add(Execute.makeGeneralError("addInterest",
                    "This is not a savings account",
                    input.getTimestamp()));
            return;
        }
        double newBalance = account.getBalance() + account.getInterest() * account.getBalance();
        account.setBalance(newBalance);
    }
}
